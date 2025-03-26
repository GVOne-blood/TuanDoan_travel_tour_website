package com.javaadvancedg9.JavaAdvancedG9.repository;

import java.util.List;

import com.javaadvancedg9.JavaAdvancedG9.dto.TourStartDTO;
import com.javaadvancedg9.JavaAdvancedG9.entity.TourStart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface TourStartRepository extends JpaRepository<TourStart, Long>{

    @Query("SELECT new com.javaadvancedg9.JavaAdvancedG9.dto.TourStartDTO(d.id,d.departing_at) FROM TourStart d WHERE d.tour_id = :tour_id")
    List<TourStartDTO> getDateStartByTourId(@Param("tour_id") Long tour_id);
}