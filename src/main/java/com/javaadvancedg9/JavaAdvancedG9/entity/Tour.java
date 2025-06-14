package com.javaadvancedg9.JavaAdvancedG9.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import io.micrometer.common.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
@Entity
@Table(name = "tour")
public class Tour extends AbstractEntity{

    private String tour_name;

//    @Lob
    @Column(name = "tour_introduction", columnDefinition = "TEXT")
    private String tour_introduction;

    private Integer tour_duration;

    //@Lob
    @Column(name = "tour_details", columnDefinition = "TEXT")
    private String tour_details;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date departing_at;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date end_at;

    private String destination;

    private Integer tour_type;

    private String tour_illustration;

    private String departure_point; //diem khoi hanh

    private Integer status;

    private Long price;

    @OneToMany(mappedBy = "tour", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TourStart> tourStarts;

    @OneToMany(mappedBy = "tour", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Image> images;
}
