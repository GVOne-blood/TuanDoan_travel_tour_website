package com.javaadvancedg9.JavaAdvancedG9.dto;

import lombok.Data;
import java.util.Date;

@Data
public class TinTucDTO {
    private Long id;
    private String tieuDe;
    private String tomTat;
    private String hinhAnh;
    private Date ngayDang;
    private String tenNguoiDang; // Chỉ lấy tên, không lấy cả object User
}