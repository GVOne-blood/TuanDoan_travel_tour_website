package com.javaadvancedg9.JavaAdvancedG9.service.Impl;

import java.util.*;

import com.javaadvancedg9.JavaAdvancedG9.dto.TourDTO;
import com.javaadvancedg9.JavaAdvancedG9.entity.Tour;
import com.javaadvancedg9.JavaAdvancedG9.repository.TourRepository;
import com.javaadvancedg9.JavaAdvancedG9.service.TourService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TourServiceImpl implements TourService {

    @Autowired
    private TourRepository tourRepository;


    @Override
    public Page<TourDTO> findAllTour(String tour_name, Long price_from, Long price_to, Date departing_at, Integer tour_type, Pageable pageable) {
        Page<TourDTO> page = this.tourRepository.findAll(tour_name, price_from, price_to,departing_at, tour_type, pageable);
        return page;
    }

    @Override
    public Page<TourDTO> findAllTourAdmin(String tour_name,Long price_from,Long price_to,Date departing_at,Integer tour_type,Pageable pageable) {
        Page<TourDTO> page = this.tourRepository.findAllAdmin(tour_name, price_from, price_to,departing_at, tour_type, pageable);
        return page;
    }

    @Override
    public TourDTO findTourById(Long id) {
        TourDTO tourDTO = this.tourRepository.findTourById(id);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(tourDTO.getDeparting_at());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        tourDTO.setDeparting_at(calendar.getTime());


        calendar.setTime(tourDTO.getEnd_at());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        tourDTO.setEnd_at(calendar.getTime());

        if(tourDTO!=null) {
            return tourDTO;
        }
        return null;
    }

    @Override
    public boolean saveTour(Tour tour) {
        if(this.tourRepository.save(tour) != null) {
            return true;
        }
        return false;
    }

    @Override
    public Tour findFirstByOrderByIdDesc() {
        return this.tourRepository.findFirstByOrderByIdDesc();
    }

    @Override
    public Tour addTour(TourDTO tourDTO) {

        Tour tour = new Tour();
        tour.setTour_name(tourDTO.getTour_name());
        tour.setTour_illustration(tourDTO.getTour_illustration());
        tour.setTour_type(tourDTO.getTour_type());
        tour.setPrice(tourDTO.getPrice());
        tour.setTour_introduction(tourDTO.getTour_introduction());
        tour.setDestination(tourDTO.getDestination());
        tour.setTour_details(tourDTO.getTour_details());
        tour.setDeparture_point(tourDTO.getDeparture_point());
        tour.setDeparting_at(tourDTO.getDeparting_at());
        tour.setTour_duration(tourDTO.getTour_duration());
        tour.setStatus(tourDTO.getStatus());
        tour.setEnd_at(tourDTO.getEnd_at());

        return this.tourRepository.save(tour);
    }

    @Override
    public Tour updateTour(TourDTO newTour, Long id) {
        Optional<Tour> tour = this.tourRepository.findById(id);
        log.info("new tour lấy đươc : {}",newTour);
        if(tour.isPresent()) {
            Tour updatedTour = tour.get();

            updatedTour.setTour_name(newTour.getTour_name());
            updatedTour.setTour_type(newTour.getTour_type());
            updatedTour.setPrice(newTour.getPrice());
            updatedTour.setTour_introduction(newTour.getTour_introduction());
            if(newTour.getTour_illustration()!=null) {
                updatedTour.setTour_illustration(newTour.getTour_illustration());
            }
            updatedTour.setDestination(newTour.getDestination());
            updatedTour.setTour_details(newTour.getTour_details());
            updatedTour.setDeparture_point(newTour.getDeparture_point());
            updatedTour.setDeparting_at(newTour.getDeparting_at());
            updatedTour.setTour_duration(newTour.getTour_duration());
            updatedTour.setStatus(newTour.getStatus());
            updatedTour.setEnd_at(newTour.getEnd_at());

            return this.tourRepository.save(updatedTour);
        }
        return null;

    }

    @Override
    public boolean deleteTour(Long id) {
        Optional<Tour> tour = this.tourRepository.findById(id);
        if(tour.isPresent()) {
            if(this.tourRepository.existsBookingByTourId(id)==false) {
                this.tourRepository.deleteById(id);
                return true;
            }
        }
        return false;
    }
}

