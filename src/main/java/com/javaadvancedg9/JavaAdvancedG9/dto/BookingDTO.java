package com.javaadvancedg9.JavaAdvancedG9.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
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

    public BookingDTO(Long id, Long user_id, Long tour_id, String tour_name, Integer number_of_people, Date departing_at, Long total_fee, Integer status, Integer payment_method, String notes, Date booking_at) {
        this.id = id;
        this.user_id = user_id;
        this.tour_id = tour_id;
        this.tour_name = tour_name;
        this.number_of_people = number_of_people;
        this.departing_at = departing_at;
        this.total_fee = total_fee;
        this.status = status;
        this.payment_method = payment_method;
        this.notes = notes;
        this.booking_at = booking_at;
    }
}
