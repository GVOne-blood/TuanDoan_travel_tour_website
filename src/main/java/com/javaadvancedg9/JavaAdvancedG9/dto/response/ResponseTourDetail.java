package com.javaadvancedg9.JavaAdvancedG9.dto.response;

import com.javaadvancedg9.JavaAdvancedG9.dto.TourDTO;
import com.javaadvancedg9.JavaAdvancedG9.dto.TourStartAtdDTO;
import com.javaadvancedg9.JavaAdvancedG9.dto.TourStartDTO;
import com.javaadvancedg9.JavaAdvancedG9.entity.Image;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ResponseTourDetail {
    private TourDTO tour;
    private List<Image> images;
    private List<TourStartDTO> tourStart;
}
