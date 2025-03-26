package com.javaadvancedg9.JavaAdvancedG9.controller.api;

import com.javaadvancedg9.JavaAdvancedG9.dto.BookingDTO;
import com.javaadvancedg9.JavaAdvancedG9.dto.response.ResponseData;
import com.javaadvancedg9.JavaAdvancedG9.service.BookingService;
import com.javaadvancedg9.JavaAdvancedG9.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @GetMapping("/getAllBooking")
    public ResponseData getAllBooking(@RequestParam(value="status",required = false) Integer status,
                                      @RequestParam(value = "status",required = false) String tour_name,
                                      @RequestParam(value="pageSize",defaultValue = "10") Integer pageSize,
                                      @RequestParam("pageIndex") Integer pageIndex){


        if(!this.userService.checkAdminLogin()) {
            return new ResponseData("Không có quyền truy cập",null);
        }

        Page<BookingDTO> page = this.bookingService.findAllBooking(status,tour_name, PageRequest.of(pageIndex,pageSize));

        return new ResponseData("Thành công",page.getContent());

    }

    @GetMapping("/{id}")
    public ResponseData<?> getOneBooking(@PathVariable("id") Long id) {
        if(!this.userService.checkAdminLogin()) {
            return new ResponseData("Không có quyền truy cập",null);
        }
        return new ResponseData("Thành công",this.bookingService.getBookingById(id));
    }
    @GetMapping("/detail/{id}")
    public ResponseData getOneDetailBooking(@PathVariable("id") Long id) {
        if(!this.userService.checkAdminLogin()) {
            return new ResponseData("Không có quyền truy cập",null);
        }
        return new ResponseData("Thành công",this.bookingService.getBookingDetailById(id));
    }

    @PutMapping("/approve/{id}")
    public ResponseData changeStatus(@PathVariable("id") Long id, @RequestParam("status") Integer status) {

        if(!this.userService.checkAdminLogin()) {
            return new ResponseData("Không có quyền truy cập",null);
        }

        if(this.bookingService.approveBooking(id,status)) {
            return new ResponseData("Cập nhật thành công",null);
        }
        return new ResponseData("Cập nhật thất bại",null);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseData deleteBooking(@PathVariable("id") Long id) {

        if(!this.userService.checkAdminLogin()) {
            return new ResponseData("Không có quyền truy cập",null);
        }

        if(this.bookingService.deleteBooking(id)) {
            return new ResponseData("Xóa thành công",null);
        }

        return new ResponseData("Chỉ có thể xóa tour đã hoàn thành và đã hủy",null);
    }

}