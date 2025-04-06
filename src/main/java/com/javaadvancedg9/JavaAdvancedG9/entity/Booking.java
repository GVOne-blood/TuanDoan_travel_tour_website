package com.javaadvancedg9.JavaAdvancedG9.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
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


    private Long user_id;

    private Long tour_id;

    private Integer number_of_people; //number_of_people

    private Date departing_at;

    private Long total_fee;

    private Integer status; //trang_thai

    private Date booking_at;

    private Integer payment_method; //payment_method

    private String notes;



    @PrePersist
    public void onCreate() {
        this.booking_at = new Date();
    }

}
