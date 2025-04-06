package com.javaadvancedg9.JavaAdvancedG9.repository;

import com.javaadvancedg9.JavaAdvancedG9.dto.BookingDTO;
import com.javaadvancedg9.JavaAdvancedG9.dto.BookingDetailDTO;
import com.javaadvancedg9.JavaAdvancedG9.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>{

    @Query(value = "SELECT new com.javaadvancedg9.JavaAdvancedG9.dto.BookingDTO(b.id,b.user_id,b.tour_id,t.tour_name,b.number_of_people,b.departing_at,b.total_fee,b.status,b.payment_method,b.notes,b.booking_at ) "
            + " FROM Booking b JOIN Tour t ON b.tour_id = t.id  "
            + " WHERE ( :status IS NULL OR b.status = :status ) "
            + " AND ( :tour_name IS NULL OR :tour_name = '' OR t.tour_name LIKE %:tour_name% ) "
            + " ORDER BY b.id ")
    public Page<BookingDTO> findAllBooking(@Param("status") Integer status, @Param("tour_name") String tour_name, Pageable pageable);

    @Query(value = "SELECT new com.javaadvancedg9.JavaAdvancedG9.dto.BookingDTO(b.id,b.user_id,b.tour_id,t.tour_name,b.number_of_people,b.departing_at,b.total_fee,b.status,b.payment_method,b.notes,b.booking_at) "
            + " FROM Booking  b JOIN User u ON b.user_id = u.id JOIN Tour t ON b.tour_id = t.id  WHERE b.user_id = :userId")
    List<BookingDTO> findBookingByUserId(@Param("userId")Long userId);


    @Query(value = "SELECT new com.javaadvancedg9.JavaAdvancedG9.dto.BookingDTO(b.id,b.user_id,b.tour_id,t.tour_name,b.number_of_people,b.departing_at,b.total_fee,b.status,b.payment_method,b.notes,b.booking_at) "
            + " FROM Booking  b JOIN User u ON b.user_id = u.id JOIN Tour t ON b.tour_id = t.id  WHERE b.user_id = :userId AND b.status !=3 AND b.status!=4")
    List<BookingDTO> checkBookingByUserId(@Param("userId")Long userId);

    @Query(value = "SELECT new com.javaadvancedg9.JavaAdvancedG9.dto.BookingDTO(b.id,b.user_id,b.tour_id,t.tour_name,b.number_of_people,b.departing_at,b.total_fee,b.status,b.payment_method,b.notes,b.booking_at) "
            + " FROM Booking  b  JOIN Tour t ON b.tour_id = t.id  WHERE b.tour_id = :tour_id")
    Page<BookingDTO> findBookingByTourId(@Param("tour_id") Long tour_id,Pageable pageable);

    @Query(value = "SELECT new com.javaadvancedg9.JavaAdvancedG9.dto.BookingDTO(b.id,b.user_id,b.tour_id,t.tour_name,b.number_of_people,b.departing_at,b.total_fee,b.status,b.payment_method,b.notes,b.booking_at) "
            + " FROM Booking  b  JOIN Tour t ON b.tour_id = t.id  WHERE b.id = :id")
    BookingDTO findBookingById(@Param("id") Long id);

    @Query(value = "SELECT new com.javaadvancedg9.JavaAdvancedG9.dto.BookingDetailDTO(b.id,b.user_id,u.fullname,u.phone,b.tour_id,t.tour_name,b.number_of_people,b.departing_at,b.total_fee,b.status,b.payment_method,b.notes,b.booking_at) "
            + " FROM Booking  b  JOIN Tour t ON b.tour_id = t.id JOIN User u ON b.user_id = u.id WHERE b.id = :id")
    BookingDetailDTO findBookingDetailById(@Param("id") Long id);

}

