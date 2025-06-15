package com.javaadvancedg9.JavaAdvancedG9.repository;

import com.javaadvancedg9.JavaAdvancedG9.entity.TinTuc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TinTucRepository extends JpaRepository<TinTuc,Long> {

 // THÊM PHƯƠNG THỨC MỚI NÀY
 @Query(value = "SELECT t FROM TinTuc t LEFT JOIN FETCH t.user",
         countQuery = "SELECT count(t) FROM TinTuc t")
 Page<TinTuc> findAllWithUser(Pageable pageable);

 // Phương thức findAll mặc định của JpaRepository đã đủ, không cần khai báo lại
 // public Page<TinTuc> findAll(Pageable pageable);

 @Query("SELECT t FROM TinTuc t LEFT JOIN FETCH t.user WHERE t.id = :id")
 public TinTuc findOnePage(@Param("id") Long id);

 @Query(value = "SELECT COUNT(*)>0 FROM TinTuc t WHERE t.tieu_de=:tieu_de")
 public boolean checkExistTieuDe(@Param("tieu_de") String tieu_de);
}