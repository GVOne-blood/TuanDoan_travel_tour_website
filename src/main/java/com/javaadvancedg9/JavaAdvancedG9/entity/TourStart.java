package com.javaadvancedg9.JavaAdvancedG9.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TourStart extends AbstractEntity {

    private Date departing_at;

    @Column(name = "tour_id", insertable = false, updatable = false)
    private Long tour_id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "tour_id")
    private Tour tour;
}
