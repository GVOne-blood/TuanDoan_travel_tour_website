package com.javaadvancedg9.JavaAdvancedG9.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import io.micrometer.common.lang.Nullable;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    // Các trường không có trong entity, sẽ được tính toán ở Service
    private Double rating;
    private Integer reviews;
    private String details_link;

    // THAY THẾ CÁC TRƯỜNG ẢNH CŨ BẰNG MỘT LIST
    private List<String> imageUrls;

}