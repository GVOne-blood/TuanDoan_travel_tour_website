package com.javaadvancedg9.JavaAdvancedG9.controller;

import com.javaadvancedg9.JavaAdvancedG9.dto.*;
import com.javaadvancedg9.JavaAdvancedG9.entity.Image;
import com.javaadvancedg9.JavaAdvancedG9.repository.ImageRepository;
import com.javaadvancedg9.JavaAdvancedG9.repository.TourStartRepository;
import com.javaadvancedg9.JavaAdvancedG9.service.BookingService;
import com.javaadvancedg9.JavaAdvancedG9.service.TourService;
import com.javaadvancedg9.JavaAdvancedG9.service.UserService;
import com.javaadvancedg9.JavaAdvancedG9.utilities.SessionUtilities;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class HomeController {


    public TourService tourService;

    private TourStartRepository tourStartRepository;

    private ImageRepository imageRepository;

    private UserService userService;

    private BookingService bookingService;

    private HttpServletRequest request;


    @GetMapping("")
    ModelAndView index() {
        ModelAndView mdv = new ModelAndView("user/index");

        Page<TourDTO> tourPage = this.tourService.findAllTour(null, null, null, null, null, PageRequest.of(0, 6));

        List<TourDTO> tours = tourPage.getContent();

        mdv.addObject("tours", tours);


        return mdv;
    }

    @GetMapping("/tour/trong-nuoc")
    ModelAndView tourTrongNuoc(@RequestParam(value = "page", required = false, defaultValue = "1") Integer pageIndex,
                               @RequestParam(value = "tour_name", required = false) String tour_name,
                               @RequestParam(value = "price", required = false) Long price,
                               @RequestParam(value = "departing_at", required = false) String departing_at) {
        ModelAndView mdv = new ModelAndView("user/tour1");
        Long price_from = null;
        Long price_to = null;
        if (price != null) {
            price_from = price == 0 ? null : (price == 1 ? 0 : (price == 2 ? 10000000l : 50000000l));

            price_to = price == 0 ? null : (price == 1 ? 10000000l : (price == 2 ? 50000000l : 500000000));
        }

        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");

        Date departing_at_value = null;
        try {
            departing_at_value = departing_at != null ? format.parse(departing_at) : null;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Page<TourDTO> tourPage = this.tourService.findAllTour(tour_name, price_from, price_to,
                departing_at_value, 1, PageRequest.of(pageIndex-1, 12));

        List<TourDTO> tours = tourPage.getContent();

        mdv.addObject("tours", tours);
        return mdv;
    }

    @GetMapping("/tour/ngoai-nuoc")
    ModelAndView tourNgoaiNuoc(@RequestParam(value = "page", required = false, defaultValue = "1") Integer pageIndex,
                               @RequestParam(value = "tour_name", required = false) String tour_name,
                               @RequestParam(value = "price", required = false) Long price,
                               @RequestParam(value = "departing_at", required = false) String departing_at) {

        Long price_from = null;
        Long price_to = null;
        if (price != null) {
            price_from = price == 0 ? null : (price == 1 ? 0 : (price == 2 ? 10000000l : 50000000l));

            price_to = price == 0 ? null : (price == 1 ? 10000000l : (price == 2 ? 50000000l : 500000000l));
        }

        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");

        Date departing_at_value = null;
        try {
            departing_at_value = departing_at != null ? format.parse(departing_at) : null;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ModelAndView mdv = new ModelAndView("user/tour2");

        Page<TourDTO> tourPage = this.tourService.findAllTour(tour_name, price_from, price_to, departing_at_value, 2,
                PageRequest.of(pageIndex-1, 12));

        List<TourDTO> tours = tourPage.getContent();

        mdv.addObject("tours", tours);
        return mdv;
    }

    @GetMapping("/tour/{id}")
    ModelAndView tourDetail(@PathVariable(name = "id", required = true) Long id) {
        ModelAndView mdv = new ModelAndView("user/tour-detail");

        TourDTO tour = this.tourService.findTourById(id);
        List<Image> imageList = this.imageRepository.findByTourId(id);


        List<TourStartDTO> listDate = this.tourStartRepository.getDateStartByTourId(id);

        mdv.addObject("tour", tour);
        mdv.addObject("listDate", listDate);
        mdv.addObject("imageList", imageList);

        return mdv;
    }

    @GetMapping("/login")
    ModelAndView login() {
        if(this.userService.checkLogin()) {
            return this.account();
        }
        ModelAndView mdv = new ModelAndView("user/login");

        return mdv;
    }

    @GetMapping("/register")
    ModelAndView register() {

        if(this.userService.checkLogin()) {
            return this.account();
        }

        ModelAndView mdv = new ModelAndView("user/register");

        return mdv;
    }


    @PostMapping("/login")
    ModelAndView loginAction(LoginDTO login) {


        ModelAndView mdv = new ModelAndView("redirect:/account");

        if(!this.userService.login(login)) {
            ModelAndView mdvErr = new ModelAndView("user/login");
            mdvErr.addObject("err", "Sai thông tin đăng nhập");
            return mdvErr;
        }

        mdv.addObject("user", SessionUtilities.getUser());

        return mdv;
    }

    @PostMapping("/register")
    ModelAndView registerAction(RegisterDTO user) {


        ModelAndView mdv = new ModelAndView("user/login");

        if(!this.userService.register(user)) {
            ModelAndView mdvErr = new ModelAndView("user/register");
            mdvErr.addObject("err", "Đăng ký thất bại");
            return mdvErr;
        }
        mdv.addObject("message", "Đăng ký thành công vui lòng đăng nhập");
        return mdv;
    }

    @GetMapping("/logout")
    ModelAndView logout() {
        SessionUtilities.setUser(null);
        SessionUtilities.setUsername(null);
        return this.index();
    }

    @GetMapping("/account")
    ModelAndView account() {
        ModelAndView mdv = new ModelAndView();

        if(SessionUtilities.getUsername()==null) {
            ModelAndView loginView = new ModelAndView("redirect:/login");
            return loginView;
        }

        mdv.setViewName("user/account");
        mdv.addObject("user", SessionUtilities.getUser());

        return mdv;
    }

    @GetMapping("/changepassword")
    ModelAndView changePassword(ChangePasswordDTO changePasswordDTO) {

        ModelAndView mdv = new ModelAndView();
        if(!this.userService.checkLogin()) {
            mdv.setViewName("redirect:/login");
            return mdv;
        }
        mdv.setViewName("user/changepassword");
        return mdv;

    }

    @PostMapping("/changePassword")
    ModelAndView changePasswordAction(ChangePasswordDTO changePasswordDTO) {

        if(changePasswordDTO.getNewPassword()!=null && changePasswordDTO.getOldPassword()!=null) {
            if(this.userService.changePassword(changePasswordDTO)) {
                ModelAndView accountView = this.account();
                accountView.addObject("message","Thay đổi mật khẩu thành công");
                return accountView;
            }
        }

        ModelAndView mdv = new ModelAndView("user/changepassword");
        mdv.addObject("err","Mật khẩu cũ không đúng");

        return mdv;
    }

    @PostMapping("/updateAccount")
    ModelAndView updateAccountAction(UpdateUserDTO updateUserDTO) {

        log.info("update account:{}",updateUserDTO);

        if(this.userService.updateUser(updateUserDTO)) {
            return this.account().addObject("message","Cập nhật thành công!");
        }else {
            return this.account().addObject("message","Có lỗi xảy ra!");
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

