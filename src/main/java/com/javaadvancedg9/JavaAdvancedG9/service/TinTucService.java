package com.javaadvancedg9.JavaAdvancedG9.service;

import com.javaadvancedg9.JavaAdvancedG9.entity.TinTuc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TinTucService {
    public Page<TinTuc> getAllPage(Pageable pageable);

    public TinTuc findOnePage(Long id);

    public  void deleteOnePage(Long id);

    public TinTuc createOnePage(TinTuc newTinTuc);

    public TinTuc updateTinTuc(TinTuc updateTinTuc,Long id);
}
