package com.javaadvancedg9.JavaAdvancedG9.entity;

import java.util.Date;

import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "booking")
public class Booking extends AbstractEntity {

    @Column(name = "user_id", insertable = false, updatable = false)
    private Long user_id;

    @Column(name = "tour_id", insertable = false, updatable = false)
    private Long tour_id;

    private Integer number_of_people; //number_of_people

    private Date departing_at;

    private Long total_fee;

    private Integer status; //trang_thai

    private Date booking_at;

    private Integer payment_method; //payment_method

    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_id")
    private Tour tour;


    @PrePersist
    public void onCreate() {
        this.booking_at = new Date();
    }

}
