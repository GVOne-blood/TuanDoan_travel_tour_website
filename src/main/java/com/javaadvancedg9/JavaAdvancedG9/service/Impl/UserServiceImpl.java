package com.javaadvancedg9.JavaAdvancedG9.service.Impl;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.javaadvancedg9.JavaAdvancedG9.dto.*;
import com.javaadvancedg9.JavaAdvancedG9.dto.response.TokenResponse;
import com.javaadvancedg9.JavaAdvancedG9.entity.User;
import com.javaadvancedg9.JavaAdvancedG9.enumtype.Role;
import com.javaadvancedg9.JavaAdvancedG9.enumtype.TokenType;
import com.javaadvancedg9.JavaAdvancedG9.repository.BookingRepository;
import com.javaadvancedg9.JavaAdvancedG9.repository.UserRepository;
import com.javaadvancedg9.JavaAdvancedG9.service.JwtService;
import com.javaadvancedg9.JavaAdvancedG9.service.MailService;
import com.javaadvancedg9.JavaAdvancedG9.service.UserService;
import com.javaadvancedg9.JavaAdvancedG9.utilities.ConvertUserToDto;
import com.javaadvancedg9.JavaAdvancedG9.utilities.PasswordEncoderUtil;
import com.javaadvancedg9.JavaAdvancedG9.utilities.SessionUtilities;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final JwtService jwtService;
    private final MailService mailService;

    @Override
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public void confirmUser(Long userId, String token) throws MessagingException, UnsupportedEncodingException {
        User user = findUserById(userId);

        if (jwtService.isValid(token, user, TokenType.RESET_TOKEN)){
            return;
        }
        return;
    }

    @Override
    public Page<UserDTO> findAllUser(String phone, String email, String fullname, Pageable pageable) {

        Page<User> page = userRepository.findAll(phone,email,fullname,Role.USER, pageable);

        Page<UserDTO> pageUserDTO = new PageImpl<>(
                page.getContent().stream().map(user ->  {

                    UserDTO userDTO = ConvertUserToDto.convertUsertoDto(user);
                    return userDTO;
                }).collect(Collectors.toList()),
                page.getPageable(),
                page.getTotalElements()
        );

        return pageUserDTO;
    }

    @Override
    public User findUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()) {
            return user.get();
        }
        return null;
    }

    @Override
    public User findUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if(user!=null) {
            return user;
        }
        return null;
    }

    @Override
    public boolean saveUser(User user) {

        if(this.userRepository.save(user)!=null) {
            return true;
        }

        return false;
    }

    @Override
    public boolean updateUser(UpdateUserDTO updateUserDTO) {
        if(SessionUtilities.getUser()!=null) {
            Long user_id = SessionUtilities.getUser().getId();

            User user = this.userRepository.findById(user_id).get();

            user.setPhone(updateUserDTO.getPhone());
            user.setUsername(updateUserDTO.getUsername());
            user.setEmail(updateUserDTO.getEmail());
            user.setAddress(updateUserDTO.getAddress());
            user.setFullname(updateUserDTO.getFullname());
            user.setGender(updateUserDTO.getGender());

            this.userRepository.save(user);

            SessionUtilities.setUser(ConvertUserToDto.convertUsertoDto(user));

            return true;

        }

        return false;
    }

    @Override
    public boolean deleteUserById(Long id) {
        Optional<User> user = this.userRepository.findById(id);
        if(user.isPresent()) {
            if(this.bookingRepository.findBookingByUserId(id)==null || this.bookingRepository.findBookingByUserId(id).size()==0) {
                this.userRepository.deleteById(id);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean login(LoginDTO user) {

        User userCheck = this.findUserByUsername(user.getUsername());

        if(userCheck==null) {
            return false;
        }

        log.info("userCheck:{}",userCheck.getUsername());

        if(BCrypt.checkpw(user.getPassword(), userCheck.getPassword())) {
            SessionUtilities.setUsername(userCheck.getUsername());
            SessionUtilities.setUser(ConvertUserToDto.convertUsertoDto(userCheck));

            log.info("userCheck:{}",SessionUtilities.getUsername());
            return true;
        }


        return false;
    }

    @Override
    public boolean register(RegisterDTO newUser) throws MessagingException, UnsupportedEncodingException {


        User user= new User();
        user.setUsername(newUser.getUsername());
        user.setFullname(newUser.getFullname());
        user.setPassword(PasswordEncoderUtil.encode(newUser.getPassword()));
        user.setEmail(newUser.getEmail());
        user.setRole(Role.USER);
        user.setPhone(newUser.getPhone());

        userRepository.save(user);
        if (user.getId() != null){
            mailService.sendConfirmationEmail(user.getEmail(), user.getId(), jwtService.generateResetToken(user) );
        }
        else return false;

        return true;
    }

    @Override
    public boolean checkLogin() {
        return SessionUtilities.getUsername()!=null;
    }

    @Override
    public boolean changePassword(ChangePasswordDTO changePasswordDTO) {
        if(SessionUtilities.getUser()!=null) {
            Long user_id = SessionUtilities.getUser().getId();

            User user = this.userRepository.findById(user_id).get();

            if(BCrypt.checkpw(changePasswordDTO.getOldPassword(),user.getPassword()) && changePasswordDTO.getNewPassword()!=null) {
                user.setPassword(BCrypt.hashpw(changePasswordDTO.getNewPassword(), BCrypt.gensalt(10)));
                this.userRepository.save(user);
                return true;
            }
            return false;

        }
        return false;
    }

    @Override
    public boolean updateUserByAdmin(UpdateUserDTO updateUserDTO,Long id) {

        User user = this.userRepository.findById(id).get();
        if(user!=null) {
            user.setPhone(updateUserDTO.getPhone());
            user.setUsername(updateUserDTO.getUsername());
            user.setEmail(updateUserDTO.getEmail());
            user.setAddress(updateUserDTO.getAddress());
            user.setFullname(updateUserDTO.getFullname());
            user.setGender(updateUserDTO.getGender());

            this.userRepository.save(user);

            return true;
        }

        return false;
    }

    @Override
    public boolean adminLogin(LoginDTO user) {
        User userCheck = this.findUserByUsername(user.getUsername());

        if(userCheck==null) {
            return false;
        }

        log.info("userCheck:{}",userCheck.getUsername());

        if(BCrypt.checkpw(user.getPassword(), userCheck.getPassword()) && userCheck.getRole()==Role.ADMIN) {

            SessionUtilities.setAdmin(ConvertUserToDto.convertUsertoDto(userCheck));

            log.info("userCheck:{}",SessionUtilities.getAdmin().getUsername());

            return true;
        }


        return false;
    }

    @Override
    public boolean checkAdminLogin() {
        return SessionUtilities.getAdmin()!=null;
    }

    @Override
    public void adminLogout() {
        SessionUtilities.setAdmin(null);
    }

    @Override
    public boolean resetPass(Long id) {
        User user = this.userRepository.findById(id).get();

        user.setPassword(BCrypt.hashpw("123@123a", BCrypt.gensalt(10)));

        if(this.userRepository.save(user)!=null) {
            return true;
        }

        return false;
    }

    public void encodeAllPassword(){
        List<User> users = userRepository.findAll();
        for (User user : users){
            user.setPassword(PasswordEncoderUtil.encode(user.getPassword()));
            userRepository.save(user);
        }
    }
}
