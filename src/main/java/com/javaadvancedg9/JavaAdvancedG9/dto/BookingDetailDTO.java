package com.javaadvancedg9.JavaAdvancedG9.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDetailDTO {

    private Long id;

    private Long user_id;

    private String fullname;

    private String phone;

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