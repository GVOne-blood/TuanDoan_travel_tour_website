package com.javaadvancedg9.JavaAdvancedG9.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import io.micrometer.common.lang.Nullable;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
@Data
@NoArgsConstructor
public class TourDTO {

    private Long id;

    private String tour_name;

    private String tour_introduction;

    private Integer tour_duration;

    private String tour_details;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date end_at;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date departing_at;

    private String destination;

    private Integer tour_type;

    private String tour_illustration;

    private String departure_point;

    private Integer status;

    private Long price;

    public TourDTO(Long id, String tour_name, String tour_introduction, Integer tour_duration, String tour_details, Date end_at, Date departing_at, String destination, Integer tour_type, String tour_illustration, String departure_point, Integer status, Long price) {
        this.id = id;
        this.tour_name = tour_name;
        this.tour_introduction = tour_introduction;
        this.tour_duration = tour_duration;
        this.tour_details = tour_details;
        this.end_at = end_at;
        this.departing_at = departing_at;
        this.destination = destination;
        this.tour_type = tour_type;
        this.tour_illustration = tour_illustration;
        this.departure_point = departure_point;
        this.status = status;
        this.price = price;
    }
}