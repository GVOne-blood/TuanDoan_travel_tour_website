package com.javaadvancedg9.JavaAdvancedG9.service;
import java.util.Date;

import com.javaadvancedg9.JavaAdvancedG9.dto.TourDTO;
import com.javaadvancedg9.JavaAdvancedG9.entity.Tour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface TourService {

    Page<TourDTO> findAllTour(String tour_name, Long price_from, Long price_to, Date departing_at, Integer tour_type, Pageable pageable);

    Page<TourDTO> findAllTourAdmin(String tour_name,Long price_from,Long price_to,Date departing_at,Integer tour_type,Pageable pageable);

    TourDTO findTourById(Long id);

    boolean saveTour(Tour tour);

    Tour findFirstByOrderByIdDesc();

    Tour addTour(TourDTO tourDTO);

    Tour updateTour(TourDTO newTour,Long id);

    boolean deleteTour(Long id);
}