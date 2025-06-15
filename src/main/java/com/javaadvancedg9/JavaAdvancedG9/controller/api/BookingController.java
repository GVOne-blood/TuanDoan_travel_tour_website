package com.javaadvancedg9.JavaAdvancedG9.controller.api;
import com.javaadvancedg9.JavaAdvancedG9.dto.BookingDTO;
import com.javaadvancedg9.JavaAdvancedG9.dto.response.ResponseError;
import com.javaadvancedg9.JavaAdvancedG9.dto.response.VNPayResponse;
import com.javaadvancedg9.JavaAdvancedG9.dto.response.ResponseData;
import com.javaadvancedg9.JavaAdvancedG9.service.BookingService;
import com.javaadvancedg9.JavaAdvancedG9.service.PaymentService;
import com.javaadvancedg9.JavaAdvancedG9.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/booking")
public class BookingController {


    private final BookingService bookingService;
    private final UserService userService;

    private final PaymentService paymentService;
    @GetMapping("/vn-pay")
    public ResponseData<VNPayResponse> pay(HttpServletRequest request) {
        return new ResponseData<>(HttpStatus.OK.value(), "Success", paymentService.createVnPayPayment(request));
    }
//    @GetMapping("/vn-pay-callback")
//    public ResponseData<VNPayResponse> payCallbackHandler(HttpServletRequest request) {
//        String status = request.getParameter("vnp_ResponseCode");
//        if (status.equals("00")) {
//            return new ResponseData<>(HttpStatus.OK.value(), "Success", new VNPayResponse("00", "Success", ""));
//        } else {
//            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "Failed", null);
//        }
//    }

    @GetMapping("/vn-pay-callback")
    public void payCallbackHandler(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String status = request.getParameter("vnp_ResponseCode");

        // THAY ĐỔI URL NÀY BẰNG URL TRANG CHỦ FRONTEND CỦA BẠN
        String frontendHomeUrl = "http://localhost:63342/TuanDoan_travel_tour_website/index.html";

        // =================================================================
        // NƠI QUAN TRỌNG: Cập nhật trạng thái đơn hàng trong Database
        // Dựa vào vnp_TxnRef (mã đơn hàng của bạn) và 'status'
        // Ví dụ:
        // String orderId = request.getParameter("vnp_TxnRef");
        // bookingService.updatePaymentStatus(orderId, status);
        // =================================================================

        if ("00".equals(status)) {
            // THÀNH CÔNG: Ra lệnh cho trình duyệt chuyển hướng về trang chủ
            response.sendRedirect(frontendHomeUrl + "?vnp_ResponseCode=00");
        } else {
            // THẤT BẠI: Chuyển hướng về trang chủ với mã lỗi
            response.sendRedirect(frontendHomeUrl + "?vnp_ResponseCode=" + status);
        }
    }

    @GetMapping("/getAllBooking")
    public ResponseData<?> getAllBooking(@RequestParam(value="status",required = false) Integer status,
                                      @RequestParam(value = "status",required = false) String tour_name,
                                      @RequestParam(value="pageSize", required = false, defaultValue = "10") Integer pageSize,
                                      @RequestParam(value = "pageIndex", required = false) Integer pageIndex){
        Page<BookingDTO> pageFake = this.bookingService.findAllBooking(status,tour_name, PageRequest.of(pageIndex,pageSize));

        return new ResponseData<>("Thành công",pageFake.getContent());

    }

    @GetMapping("/{id}")
    public ResponseData<?> getOneBooking(@PathVariable("id") Long id) {
        return new ResponseData<>("Thành công",this.bookingService.getBookingById(id));
    }
    @GetMapping("/detail/{id}")
    public ResponseData<?> getOneDetailBooking(@PathVariable("id") Long id) {
        if(!this.userService.checkAdminLogin()) {
            return new ResponseData<>("Không có quyền truy cập",null);
        }
        return new ResponseData<>("Thành công",this.bookingService.getBookingDetailById(id));
    }

    @PutMapping("/approve/{id}")
    public ResponseData<?> changeStatus(@PathVariable("id") Long id, @RequestParam("status") Integer status) {




        if(this.bookingService.approveBooking(id,status)) {
            return new ResponseData<>("Cập nhật thành công",null);
        }
        return new ResponseError(HttpStatus.BAD_GATEWAY.value(), "Cập nhật thất bại",null);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseData<?> deleteBooking(@PathVariable("id") Long id) {

        if(!this.userService.checkAdminLogin()) {
            return new ResponseData<>("Không có quyền truy cập",null);
        }

        if(this.bookingService.deleteBooking(id)) {
            return new ResponseData<>("Xóa thành công",null);
        }

        return new ResponseData<>("Chỉ có thể xóa tour đã hoàn thành và đã hủy",null);
    }

}