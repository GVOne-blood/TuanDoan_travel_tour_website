package com.javaadvancedg9.JavaAdvancedG9.controller.api;

import com.javaadvancedg9.JavaAdvancedG9.dto.response.ResponseData;
import com.javaadvancedg9.JavaAdvancedG9.dto.UpdateUserDTO;
import com.javaadvancedg9.JavaAdvancedG9.utilities.ConvertUserToDto;
import com.javaadvancedg9.JavaAdvancedG9.dto.UserDTO;
import com.javaadvancedg9.JavaAdvancedG9.entity.User;
import com.javaadvancedg9.JavaAdvancedG9.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/getAll")
    public ResponseData getAllUser(
            @RequestParam(value = "sdt",required = false) String sdt,
            @RequestParam(value = "email",required = false) String email,
            @RequestParam(value = "fullname",required = false) String fullname,
            @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize,
            @RequestParam("pageIndex") Integer pageIndex
    ) {
        if(!this.userService.checkAdminLogin()) {
            return new ResponseData("Không có quyền truy cập",null);
        }

        Page<UserDTO> page = this.userService.findAllUser(sdt,email,fullname, PageRequest.of(pageIndex,pageSize));

        return new ResponseData("Thành công",page.getContent());
    }

    @GetMapping("/{id}")
    public ResponseData getOneUser(@PathVariable("id") Long id) {

        if(!this.userService.checkAdminLogin()) {
            return new ResponseData("Không có quyền truy cập",null);
        }

        if(this.userService.findUserById(id)!=null) {
            return new ResponseData("Thành công", ConvertUserToDto.convertUsertoDto(this.userService.findUserById(id)));
        }
        return new ResponseData("Thất bại",null);
    }

    @PutMapping("/update/{id}")
    public ResponseData updateUser(@PathVariable("id") Long id, @RequestBody UpdateUserDTO updateUserDTO) {

        if(!this.userService.checkAdminLogin()) {
            return new ResponseData("Không có quyền truy cập",null);
        }

        User user = this.userService.findUserById(id);

        if(user!=null) {
            if(this.userService.updateUserByAdmin(updateUserDTO,id)) {
                return new ResponseData("Cập nhật thành công",this.userService.findUserById(id));
            }
        }
        return new ResponseData("Cập nhật thất bại",null);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseData deleteUser(@PathVariable("id") Long id){


        if(!this.userService.checkAdminLogin()) {
            return new ResponseData("Không có quyền truy cập",null);
        }

        User user = this.userService.findUserById(id);
        if(user!=null) {

            if(this.userService.deleteUserById(id)) {
                return new ResponseData("Xóa thành công",null);
            }
        }

        return new ResponseData("Không thể xóa người dùng này",null);
    }

    @PutMapping("/update/resetPass/{id}")
    public ResponseData resetPass(@PathVariable("id") Long id) {

        if(!this.userService.checkAdminLogin()) {
            return new ResponseData("Không có quyền truy cập",null);
        }

        if(this.userService.resetPass(id)) {
            return new ResponseData("Khôi phục mật khẩu mặc định thành công",null);
        }
        return new ResponseData("Khôi phục mật khẩu mặc định thất bại",null);

    }
}

