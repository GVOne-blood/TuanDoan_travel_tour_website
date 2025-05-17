package com.javaadvancedg9.JavaAdvancedG9.dto;

import java.util.Date;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {

    private Long id;

    private Long user_id;

    private Long tour_id;

    private String tour_name;

    private Integer number_of_people;

    private Date departing_at;

    private Long total_fee;

    private Integer status;

    private Integer payment_method;

    private String notes;

    private Date booking_at;

}
