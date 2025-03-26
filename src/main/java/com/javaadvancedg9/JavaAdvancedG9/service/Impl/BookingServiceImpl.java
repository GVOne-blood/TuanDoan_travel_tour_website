package com.javaadvancedg9.JavaAdvancedG9.service.Impl;

import java.util.List;
import java.util.Optional;

import com.javaadvancedg9.JavaAdvancedG9.dto.BookingDTO;
import com.javaadvancedg9.JavaAdvancedG9.dto.BookingDetailDTO;
import com.javaadvancedg9.JavaAdvancedG9.dto.TourDTO;
import com.javaadvancedg9.JavaAdvancedG9.entity.Booking;
import com.javaadvancedg9.JavaAdvancedG9.repository.BookingRepository;
import com.javaadvancedg9.JavaAdvancedG9.repository.TourRepository;
import com.javaadvancedg9.JavaAdvancedG9.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TourRepository tourRepository;

    @Override
    public Page<BookingDTO> findAllBooking(Integer status,String tour_name,Pageable pageable) {
        return this.bookingRepository.findAllBooking(status,tour_name, pageable);
    }

    @Override
    public List<BookingDTO> findBookingByUserId(Long userId) {
        return this.bookingRepository.findBookingByUserId(userId);
    }

    @Override
    public Page<BookingDTO> findBookingByTourId(Long tour_Id,Pageable pageable) {
        return this.bookingRepository.findBookingByTourId(tour_Id, pageable);
    }

    @Override
    public boolean addNewBooking(BookingDTO newBooking) {


        List<BookingDTO> checkBooking  = this.bookingRepository.checkBookingByUserId(newBooking.getUser_id());
        if(checkBooking.size()>0) {
            return false;
        }

        TourDTO tourDTO = this.tourRepository.findTourById(newBooking.getTour_id());

        Booking booking = new Booking();
        booking.setNumber_of_people(newBooking.getNumber_of_people());
        booking.setDeparting_at(newBooking.getDeparting_at());
        booking.setTotal_fee(tourDTO.getPrice()*newBooking.getNumber_of_people());
        booking.setTour_id(newBooking.getTour_id());
        booking.setUser_id(newBooking.getUser_id());
        booking.setNumber_of_people(newBooking.getNumber_of_people());
        booking.setNotes(newBooking.getNotes());
        booking.setPayment_method(newBooking.getPayment_method());
        booking.setStatus(0);

        this.bookingRepository.save(booking);

        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean approveBooking(Long bookingId,Integer status) {
        Optional<Booking> booking  = this.bookingRepository.findById(bookingId);

        if(booking.isPresent()) {
            Booking bookingUpdated = booking.get();
            bookingUpdated.setStatus(status);
            this.bookingRepository.save(bookingUpdated);
            return true;
        }

        return false;
    }

    @Override
    public boolean deleteBooking(Long id) {

        BookingDTO booking = this.getBookingById(id);

        if(booking.getStatus()==3) {
            this.bookingRepository.deleteById(id);
            return true;
        }

        return false;
    }

    @Override
    public BookingDTO getBookingById(Long id) {
        return this.bookingRepository.findBookingById(id);
    }

    @Override
    public BookingDetailDTO getBookingDetailById(Long id) {
        return this.bookingRepository.findBookingDetailById(id);
    }

}