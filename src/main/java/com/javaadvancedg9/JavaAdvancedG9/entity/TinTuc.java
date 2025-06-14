package com.javaadvancedg9.JavaAdvancedG9.entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tin_tuc")
public class TinTuc extends AbstractEntity {

    @Column(name = "user_id", insertable = false, updatable = false)
    private Long user_id;

    private String tieu_de;

    //@Lob
    @Column(name = "tom_tat" , columnDefinition = "TEXT")
    private String tom_tat;

    @Lob
    @Column(name = "noi_dung" , columnDefinition = "TEXT")
    private String noi_dung;

    private String hinh_anh;

    private Date ngay_dang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


}
