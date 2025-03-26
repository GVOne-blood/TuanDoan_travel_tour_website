package com.javaadvancedg9.JavaAdvancedG9.service;

import java.util.List;

import com.javaadvancedg9.JavaAdvancedG9.entity.Image;


public interface ImageService {

    List<Image> findByTourId(Long id);

    public Image addToTour(Long tourId,String url);

    void deleteById(Long id);
}

