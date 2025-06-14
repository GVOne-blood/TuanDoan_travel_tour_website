package com.javaadvancedg9.JavaAdvancedG9.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@NoArgsConstructor
@Getter
public class SearchRequest {
    @NotNull(message = "Tour name cannot be null")
    private String tourName;

    @Min(value = 0, message = "Price from must be at least 0")
    private Long priceFrom;

    private Long priceTo;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date departingAt;



}
