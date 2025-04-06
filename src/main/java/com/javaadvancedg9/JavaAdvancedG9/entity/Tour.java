package com.javaadvancedg9.JavaAdvancedG9.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
@Entity
@Table(name = "tour")
public class Tour extends AbstractEntity{

    private String tour_name;

    private String tour_introduction;

    private Integer tour_duration;

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


}
