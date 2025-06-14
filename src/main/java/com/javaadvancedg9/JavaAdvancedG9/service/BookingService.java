package com.javaadvancedg9.JavaAdvancedG9.service;

import java.io.FileNotFoundException;
import java.util.List;

import com.javaadvancedg9.JavaAdvancedG9.dto.BookingDTO;
import com.javaadvancedg9.JavaAdvancedG9.dto.BookingDetailDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface BookingService {

    Page<BookingDTO> findAllBooking(Integer status, String tour_name, Pageable pageable);

    List<BookingDTO> findBookingByUserId(Long userId);

    Page<BookingDTO> findBookingByTourId(Long tour_Id,Pageable pageable);

    boolean addNewBooking(BookingDTO newBooking) throws FileNotFoundException;

    boolean approveBooking(Long bookingId,Integer status);

    boolean deleteBooking(Long id);

    BookingDTO getBookingById(Long id);

    BookingDetailDTO getBookingDetailById(Long id);
}
