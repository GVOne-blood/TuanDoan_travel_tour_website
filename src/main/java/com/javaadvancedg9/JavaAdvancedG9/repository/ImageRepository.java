package com.javaadvancedg9.JavaAdvancedG9.repository;

import java.util.List;

import com.javaadvancedg9.JavaAdvancedG9.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ImageRepository  extends JpaRepository<Image, Long>{

    @Query(value="select i from Image i where i.tour_id = :id")
    List<Image> findByTourId(@Param("id") Long id);

}
