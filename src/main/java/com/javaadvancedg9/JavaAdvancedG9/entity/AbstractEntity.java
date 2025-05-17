package com.javaadvancedg9.JavaAdvancedG9.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

@Getter
@Setter
@MappedSuperclass
public abstract class AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "created_by")
    @CreatedBy //cập nhật người tạo
    private String createdBy;

    @Column(name = "updated_by")
    @LastModifiedBy
    private String updatedBy;

    @Column(name = "created_at")
    @CreationTimestamp //cập nhật thời gian khi tạo
    @Temporal(TemporalType.TIMESTAMP)
    private String createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp //cập nhật thời gian khi update
    @Temporal(TemporalType.TIMESTAMP)
    private String updatedAt;
}
