package com.javaadvancedg9.JavaAdvancedG9.repository;

import com.javaadvancedg9.JavaAdvancedG9.dto.TourDTO;
import com.javaadvancedg9.JavaAdvancedG9.entity.Tour;
import org.springframework.data.domain.Pageable;
import java.util.Date;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface TourRepository extends JpaRepository<Tour, Long>,JpaSpecificationExecutor<Tour> {




//    @Query(value= "SELECT new com.javaadvancedg9.JavaAdvancedG9.dto.TourDTO(t.id,t.tour_name,t.tour_introduction,t.tour_duration,t.tour_details,t.end_at,t.departing_at,t.destination,t.tour_type,t.tour_illustration,t.departure_point,t.status,t.price) FROM Tour t "
//            + " WHERE (:tour_name IS NULL OR :tour_name='' OR t.tour_name LIKE CONCAT('%', :tour_name, '%'))"
//            + " AND ( :tour_type IS NULL OR t.tour_type = :tour_type )"
//            + " AND ( :departing_at IS NULL OR t.departing_at = :departing_at )"
//            + " AND ( :price_from IS NULL OR  :price_to IS NULL OR (t.price BETWEEN :price_from AND :price_to))  "
//            + " ORDER BY t.id ")
//    Page<TourDTO> findAllAdmin(
//            @Param("tour_name") String tour_name,
//            @Param("price_from") Long price_from,
//            @Param("price_to") Long price_to,
//            @Param("departing_at") Date departing_at,
//            @Param("tour_type") Integer tour_type,
//            Pageable pageable
//    );

    @Query(value = "SELECT DISTINCT t FROM Tour t LEFT JOIN FETCH t.images "
            + " WHERE (:tour_name IS NULL OR :tour_name = '' OR t.tour_name LIKE CONCAT('%', :tour_name, '%'))"
            + " AND (:tour_type IS NULL OR t.tour_type = :tour_type)"
            + " AND (:price_from IS NULL OR t.price >= :price_from)"
            + " AND (:price_to IS NULL OR t.price <= :price_to)"
            + " AND (:departing_at IS NULL OR t.departing_at = :departing_at)",
            countQuery = "SELECT count(t) FROM Tour t " // Query riêng để đếm tổng số lượng cho phân trang
                    + " WHERE (:tour_name IS NULL OR :tour_name = '' OR t.tour_name LIKE CONCAT('%', :tour_name, '%'))"
                    + " AND (:tour_type IS NULL OR t.tour_type = :tour_type)"
                    + " AND (:price_from IS NULL OR t.price >= :price_from)"
                    + " AND (:price_to IS NULL OR t.price <= :price_to)"
                    + " AND (:departing_at IS NULL OR t.departing_at = :departing_at)")
    Page<Tour> findAllAdmin(
            @Param("tour_name") String tour_name,
            @Param("price_from") Long price_from,
            @Param("price_to") Long price_to,
            @Param("departing_at") Date departing_at,
            @Param("tour_type") Integer tour_type,
            Pageable pageable
    );

//    @Query(value = "SELECT new com.javaadvancedg9.JavaAdvancedG9.dto.TourDTO(t.id,t.tour_name,t.tour_introduction,t.tour_duration,t.tour_details,t.end_at,t.departing_at,t.destination,t.tour_type,t.tour_illustration,t.departure_point,t.status,t.price) FROM Tour t "
//            + " WHERE t.id = :id")
//    TourDTO findTourDTOById(Long id);

//    @Query(value ="SELECT new com.javaadvancedg9.JavaAdvancedG9.dto.TourDTO(t.id,t.tour_name,t.tour_introduction,t.tour_duration,t.tour_details,t.end_at,t.departing_at,t.destination,t.tour_type,t.tour_illustration,t.departure_point,t.status,t.price) FROM Tour t "
//            + " JOIN Booking b ON t.id = b.tour_id WHERE b.tour_id = :booking_id" )
//    TourDTO findTourByBookingId(@Param("booking_id") Long booking_id);

    public Optional<Tour> findTourById(Long id);

    @Query("SELECT COUNT(*) > 0 FROM Booking b WHERE b.tour_id = :tourId")
    boolean existsBookingByTourId(@Param("tourId") Long tourId);

    Tour findFirstByOrderByIdDesc();
}
