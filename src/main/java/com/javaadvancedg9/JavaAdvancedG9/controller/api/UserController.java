package com.javaadvancedg9.JavaAdvancedG9.controller.api;

import com.javaadvancedg9.JavaAdvancedG9.dto.RegisterDTO;
import com.javaadvancedg9.JavaAdvancedG9.dto.response.ResponseData;
import com.javaadvancedg9.JavaAdvancedG9.dto.UpdateUserDTO;
import com.javaadvancedg9.JavaAdvancedG9.dto.response.ResponseError;
import com.javaadvancedg9.JavaAdvancedG9.service.MailService;
import com.javaadvancedg9.JavaAdvancedG9.utilities.ConvertUserToDto;
import com.javaadvancedg9.JavaAdvancedG9.dto.UserDTO;
import com.javaadvancedg9.JavaAdvancedG9.entity.User;
import com.javaadvancedg9.JavaAdvancedG9.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final MailService mailService;

    @GetMapping("/getAll")
    public ResponseData<?> getAllUser(
            @RequestParam(value = "phone",required = false) String phone,
            @RequestParam(value = "email",required = false) String email,
            @RequestParam(value = "fullname",required = false) String fullname,
            @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize,
            @RequestParam("pageIndex") Integer pageIndex
    ) {

        Page<UserDTO> page = this.userService.findAllUser(phone,email,fullname, PageRequest.of(pageIndex,pageSize));

        return new ResponseData<>("Thành công",page.getContent());
    }

    @GetMapping("/{id}")
    public ResponseData<?> getOneUser(@PathVariable("id") Long id) {


        if(this.userService.findUserById(id)!=null) {
            return new ResponseData<>(HttpStatus.OK.value(), "Thành công", ConvertUserToDto.convertUsertoDto(this.userService.findUserById(id)));
        }
        return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Thất bại",null);
    }

    @PostMapping("/register")
    @Transactional
    public ResponseData<?> createUser(@RequestBody RegisterDTO registerDTO) {
           try{
               userService.register(registerDTO);
           }
           catch (Exception e) {
               return new ResponseData<>("Đăng ký thất bại: " + e.getMessage(), null);
           }
        return new ResponseData<>("Đăng ký thành công", true);
    }

    @GetMapping("/send-mail")
    public ResponseData<String> sendMail(@RequestParam String to,
                                         @RequestParam String subject,
                                         @RequestParam String content,
                                         @RequestParam(required = false) MultipartFile[] files) {
        try {
            String result = mailService.sendEmail(to, subject, content, files);
            return new ResponseData<>(HttpStatus.OK.value(), "Email sent successfully", result);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to send email: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseData<?> updateUser(@PathVariable("id") Long id, @RequestBody UpdateUserDTO updateUserDTO) {

        User user = this.userService.findUserById(id);

        if(user!=null) {
            if(this.userService.updateUserByAdmin(updateUserDTO,id)) {
                return new ResponseData<>("Cập nhật thành công",this.userService.findUserById(id));
            }
        }
        return new ResponseData<>("Cập nhật thất bại",null);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseData<?> deleteUser(@PathVariable("id") Long id){


        User user = this.userService.findUserById(id);
        if(user!=null) {

            if(this.userService.deleteUserById(id)) {
                return new ResponseData<>("Xóa thành công",null);
            }
        }

        return new ResponseData<>("Không thể xóa người dùng này",null);
    }

    @PutMapping("/update/resetPass/{id}")
    public ResponseData<?> resetPass(@PathVariable("id") Long id) {

        if(this.userService.resetPass(id)) {
            return new ResponseData<>("Khôi phục mật khẩu mặc định thành công",null);
        }
        return new ResponseData<>("Khôi phục mật khẩu mặc định thất bại",null);

    }
}

