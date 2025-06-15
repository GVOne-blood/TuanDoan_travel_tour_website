package com.javaadvancedg9.JavaAdvancedG9.dto;

import lombok.Data;
import java.util.Date;

@Data
public class TinTucDetailDTO {
    private Long id;
    private String tieuDe;
    private String tomTat;
    private String noiDung; // Thêm trường nội dung chi tiết
    private String hinhAnh;
    private Date ngayDang;
    private String tenNguoiDang;
    private String avatarNguoiDang; // Có thể thêm avatar nếu muốn
}