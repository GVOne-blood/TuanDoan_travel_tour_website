package com.javaadvancedg9.JavaAdvancedG9.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class ResponseDTO {
    public int status;
   public String message;
   public Object data;
}
