package com.javaadvancedg9.JavaAdvancedG9.controller;

import com.javaadvancedg9.JavaAdvancedG9.dto.*;
import com.javaadvancedg9.JavaAdvancedG9.dto.response.ResponseData;
import com.javaadvancedg9.JavaAdvancedG9.dto.response.ResponseError;
import com.javaadvancedg9.JavaAdvancedG9.dto.response.ResponseTourDetail;
import com.javaadvancedg9.JavaAdvancedG9.entity.Image;
import com.javaadvancedg9.JavaAdvancedG9.repository.ImageRepository;
import com.javaadvancedg9.JavaAdvancedG9.repository.TourStartRepository;
import com.javaadvancedg9.JavaAdvancedG9.service.BookingService;
import com.javaadvancedg9.JavaAdvancedG9.service.ImageService;
import com.javaadvancedg9.JavaAdvancedG9.service.TourService;
import com.javaadvancedg9.JavaAdvancedG9.service.UserService;
import com.javaadvancedg9.JavaAdvancedG9.utilities.SessionUtilities;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class HomeController {


    public final TourService tourService;

    private final TourStartRepository tourStartRepository;

    private final ImageService imageService;

    private final UserService userService;

    private final BookingService bookingService;

    private final HttpServletRequest request;


//    @GetMapping("")
//    ModelAndView index() {
//        ModelAndView mdv = new ModelAndView("user/index");
//
//        Page<TourDTO> tourPage = this.tourService.findAllTour(null, null, null, null, null, PageRequest.of(0, 6));
//
//        List<TourDTO> tours = tourPage.getContent();
//
//        mdv.addObject("tours", tours);
//
//
//        return mdv;
//    }


    // Trong HomeController.java
    @GetMapping("/tours/search")
    public ResponseData<?> searchPublicTours(
            @RequestParam(value = "destination", required = false) String destination,
            @RequestParam(value = "priceFrom", required = false) Long priceFrom,
            @RequestParam(value = "priceTo", required = false) Long priceTo,
            // Thêm tham số tour_type
            @RequestParam(value = "tour_type", required = false) Integer tourType,
            @RequestParam(value = "sortBy", defaultValue = "recommended") String sortBy,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "6") int size // Giảm size xuống 6 để mỗi mục không quá dài
    ) {
        Sort sort;
        if ("price-asc".equals(sortBy)) {
            sort = Sort.by("price").ascending();
        } else if ("price-desc".equals(sortBy)) {
            sort = Sort.by("price").descending();
        } else {
            sort = Sort.by("id").descending();
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        // Truyền tourType vào service
        Page<TourDTO> tourPage = tourService.findAllTourAdmin( destination, priceFrom, priceTo, null, tourType, pageable);

        return new ResponseData<>(HttpStatus.OK.value(), "Thành công", tourPage);
    }
//
//    @GetMapping("/tour/trong-nuoc")
//    ModelAndView tourTrongNuoc(@RequestParam(value = "page", required = false, defaultValue = "1") Integer pageIndex,
//                               @RequestParam(value = "tour_name", required = false) String tour_name,
//                               @RequestParam(value = "price", required = false) Long price,
//                               @RequestParam(value = "departing_at", required = false) String departing_at) {
//        ModelAndView mdv = new ModelAndView("user/tour1");
//        Long price_from = null;
//        Long price_to = null;
//        if (price != null) {
//            price_from = price == 0 ? null : (price == 1 ? 0 : (price == 2 ? 10000000l : 50000000l));
//
//            price_to = price == 0 ? null : (price == 1 ? 10000000l : (price == 2 ? 50000000l : 500000000));
//        }
//
//        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
//
//        Date departing_at_value = null;
//        try {
//            departing_at_value = departing_at != null ? format.parse(departing_at) : null;
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        Page<TourDTO> tourPage = this.tourService.findAllTour(tour_name, price_from, price_to,
//                departing_at_value, 1, PageRequest.of(pageIndex-1, 12));
//
//        List<TourDTO> tours = tourPage.getContent();
//
//        mdv.addObject("tours", tours);
//        return mdv;
//    }

//    @GetMapping("/tour/{id}")
//    ModelAndView tourDetail(@PathVariable(name = "id", required = true) Long id) {
//        ModelAndView mdv = new ModelAndView("user/tour-detail");
//
//        TourDTO tour = this.tourService.findTourById(id);
//        List<Image> imageList = this.imageRepository.findByTourId(id);
//
//
//        List<TourStartDTO> listDate = this.tourStartRepository.getDateStartByTourId(id);
//
//        mdv.addObject("tour", tour);
//        mdv.addObject("listDate", listDate);
//        mdv.addObject("imageList", imageList);
//
//        return mdv;
//    }
    @GetMapping("/tour/{id}")
    ResponseData<?> tourDetail(@PathVariable(name = "id") Long id){
        if (id != null) return new ResponseData<>(HttpStatus.OK.value(), "Lấy thông tin tour thành công",
                ResponseTourDetail.builder()
                        .tour(tourService.findTourById(id))
                        .images(imageService.findByTourId(id))
                        .tourStart(tourStartRepository.getDateStartByTourId(id))
                        .build());
        return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Lấy thông tin không thành công ");
    }

//    @GetMapping("/login")
//    ModelAndView login() {
//        if(this.userService.checkLogin()) {
//            return this.account();
//        }
//        ModelAndView mdv = new ModelAndView("user/login");
//
//        return mdv;
//    }
    @GetMapping("/login")
    ResponseData<?> login(){
        if(this.userService.checkLogin()) {
            return new ResponseData<>(HttpStatus.OK.value(), "Bạn đã đăng nhập", SessionUtilities.getUser());
        }
        return new ResponseError(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập");
    }

//    @GetMapping("/register")
//    ModelAndView register() {
//
//        if(this.userService.checkLogin()) {
//            return this.account();
//        }
//
//        ModelAndView mdv = new ModelAndView("user/register");
//
//        return mdv;
//    }
    @PostMapping("/register")
    ResponseData<?> register() {

        if(this.userService.checkLogin()) {
            return new ResponseData<>(HttpStatus.OK.value(), "Bạn đã đăng nhập", SessionUtilities.getUser());
        }

        return new ResponseError(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập");
    }


//    @PostMapping("/login")
//    ModelAndView loginAction(LoginDTO login) {
//
//
//        ModelAndView mdv = new ModelAndView("redirect:/account");
//
//        if(!this.userService.login(login)) {
//            ModelAndView mdvErr = new ModelAndView("user/login");
//            mdvErr.addObject("err", "Sai thông tin đăng nhập");
//            return mdvErr;
//        }
//
//        mdv.addObject("user", SessionUtilities.getUser());
//
//        return mdv;
//    }
    @PostMapping("/login")
    ResponseData<?> loginAction(@RequestBody LoginDTO login) {

        if(!this.userService.login(login)) {
            return new ResponseError(HttpStatus.UNAUTHORIZED.value(), "Sai thông tin đăng nhập");
        }

        return new ResponseData<>(HttpStatus.OK.value(), "Đăng nhập thành công", SessionUtilities.getUser());
    }





    @GetMapping("/logout")
    ResponseData<?> logout() {
        SessionUtilities.setUser(null);
        SessionUtilities.setUsername(null);
        return new ResponseData<>(HttpStatus.OK.value(), "Đăng xuất thành công");
    }

    @GetMapping("/account")
    ResponseData<?> account() {

        if(SessionUtilities.getUsername()==null) {
            return new ResponseError(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập");
        }

        return new ResponseData<>(HttpStatus.OK.value(), "Lấy thông tin tài khoản thành công", SessionUtilities.getUser());
    }


    @GetMapping("/changepassword")
    ResponseData<?> changePassword(ChangePasswordDTO changePasswordDTO) {

        if(!this.userService.checkLogin()) {
            return new ResponseError(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập");
        }

        return new ResponseData<>(HttpStatus.OK.value(), "Lấy thông tin thay đổi mật khẩu thành công", changePasswordDTO);
    }


    @PostMapping("/changePassword")
    ResponseData<?> changePasswordAction(@RequestBody ChangePasswordDTO changePasswordDTO) {

        if(changePasswordDTO.getNewPassword()!=null && changePasswordDTO.getOldPassword()!=null) {
            if(this.userService.changePassword(changePasswordDTO)) {
                return new ResponseData<>(HttpStatus.OK.value(), "Thay đổi mật khẩu thành công");
            }
        }

        return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Mật khẩu cũ không đúng");
    }

    @PostMapping("/updateAccount")
    ResponseData<?> updateAccountAction(@RequestBody UpdateUserDTO updateUserDTO) {

        log.info("update account:{}",updateUserDTO);

        if(this.userService.updateUser(updateUserDTO)) {
            return new ResponseData<>(HttpStatus.OK.value(), "Cập nhật thành công!");
        }else {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Có lỗi xảy ra!");
        }

    }

    @GetMapping("/tour/booking/{id}")
    ModelAndView booking(@PathVariable("id") Long tour_id,
                         @RequestParam String departing_at,
                         @RequestParam Integer so_nguoi,
                         @RequestParam Long total_fee ) {

        log.info("ngay khoi hanh log: {}",departing_at);
        ModelAndView mdv = new ModelAndView("user/booking-checkout");


        if(tour_id==null || departing_at==null || so_nguoi==null || total_fee==null) {
            return new ModelAndView("redirect:/err");
        }

        if(SessionUtilities.getUsername()==null) {
            ModelAndView loginView = new ModelAndView("redirect:/login");
            return loginView;
        }

        if(this.tourService.findTourById(tour_id)==null) {
            return new ModelAndView("redirect:/error");
        }

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Date departing_at_value = null;
        try {
            departing_at_value = departing_at != null ? format.parse(departing_at.split(" ")[0]) : null;
        } catch (ParseException e) {
            e.printStackTrace();
            return new ModelAndView("redirect:/error");
        }

        mdv.addObject("user",SessionUtilities.getUser());
        TourDTO tour = this.tourService.findTourById(tour_id);
        mdv.addObject("tour",tour);
        mdv.addObject("departing_at",departing_at_value);
        mdv.addObject("so_nguoi",so_nguoi);
        mdv.addObject("total_fee",total_fee);

        return mdv;

    }


    @PostMapping(value = "/tour/booking/{tour_id}")
    ModelAndView bookingAction(@PathVariable("tour_id") Long tour_id,
                               @RequestParam("number_of_people") Integer number_of_people,
                               @RequestParam("departing_at") String departing_at,
                               @RequestParam("notes") String notes,
                               @RequestParam("payment_method") Integer payment_method) {


        ModelAndView mdv = new ModelAndView();

        BookingDTO bookingDTO = new BookingDTO(); 

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Date departing_at_value = null;
        try {
            departing_at_value = departing_at != null ? format.parse(departing_at.split(" ")[0]) : null;
        } catch (ParseException e) {
            e.printStackTrace();
            return new ModelAndView("redirect:/error");
        }

        bookingDTO.setNumber_of_people(number_of_people);
        bookingDTO.setNotes(notes);
        bookingDTO.setDeparting_at(departing_at_value);
        bookingDTO.setPayment_method(payment_method);
        bookingDTO.setTour_id(tour_id);
        bookingDTO.setUser_id(SessionUtilities.getUser().getId());


        if(this.bookingService.addNewBooking(bookingDTO)) {
            mdv.setViewName("redirect:/account");
            return mdv;
        }

        mdv.setViewName("redirect:/error");
        return mdv;
    }


    @GetMapping("/error")
    public String error() {
        return "user/error";
    }

    @GetMapping("/user/tour")
    ModelAndView userTour(){

        if(!this.userService.checkLogin()){
            return new ModelAndView("redirect:/login");
        }

        ModelAndView mdv = new ModelAndView("user/user-booking-list");

        List<BookingDTO> bookingList = this.bookingService.findBookingByUserId(SessionUtilities.getUser().getId());

        mdv.addObject("bookingList",bookingList);

        return mdv;

    }

    @GetMapping("/user/booking/{id}")
    ModelAndView userBookingDetai(@PathVariable Long id) {

        if(!this.userService.checkLogin()){
            return new ModelAndView("redirect:/login");
        }

        ModelAndView mdv = new ModelAndView();

        BookingDTO booking = this.bookingService.getBookingById(id);

        if(booking!=null) {
            mdv.setViewName("user/user-booking");
            mdv.addObject("booking",booking);
            return mdv;
        }

        mdv.setViewName("redirect:/error");
        return mdv;
    }

    @GetMapping("/user/booking/cancel/{id}")
    ModelAndView cancelTour(@PathVariable Long id) {

        if(id==null) {
            return new ModelAndView("redirect:/error");
        }

        String referrer = request.getHeader("Referer");


        log.info("Url trước đó : {}",referrer);

        BookingDTO bookingDTO = this.bookingService.getBookingById(id);

        if(bookingDTO!=null) {

            if(bookingDTO.getStatus()==0 || bookingDTO.getStatus()==1){
                this.bookingService.approveBooking(id,4);
            }

        }else {
            return new ModelAndView("redirect:/error");
        }

        return new ModelAndView("redirect:/user/tour" );
    }

    @GetMapping("/about")
    public String about() {
        return "/user/about";
    }

    @GetMapping("/tin-tuc")
    public String news() {
        return "/user/tin-tuc";
    }

}

