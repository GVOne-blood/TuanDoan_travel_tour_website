package com.javaadvancedg9.JavaAdvancedG9.controller.api;

import com.javaadvancedg9.JavaAdvancedG9.dto.response.ResponseData;
import com.javaadvancedg9.JavaAdvancedG9.entity.TinTuc;
import com.javaadvancedg9.JavaAdvancedG9.service.TinTucService;
import com.javaadvancedg9.JavaAdvancedG9.utilities.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tintuc")
public class TinTucController {
    private final TinTucService tinTucService;

    @GetMapping("/getAllPage")
    public ResponseData<?> getAllPage(@RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize,
                                   @RequestParam(value = "pageIndex") Integer pageIndex) {
        Page<TinTuc> page = this.tinTucService.getAllPage(PageRequest.of(pageSize,pageIndex));

        return new ResponseData<>("Thành Công",page.getContent());
    }

    @GetMapping("/{id}")
    public ResponseData<?> getOnePage(@PathVariable("id") Long id) {

        return new ResponseData<>("Thành công",this.tinTucService.findOnePage(id));

    }

    @PostMapping("/add")
    public ResponseData<?> addNewTintuc(@RequestBody TinTuc tinTuc, @RequestParam("image") MultipartFile image) {

        String uploadDir = "/upload";
        try {
            // Lưu ảnh vào thư mục "upload"
            String fileName = image.getOriginalFilename();
            FileUploadUtil.saveFile(uploadDir, fileName, image);

            // Lưu thông tin của tour vào cơ sở dữ liệu
            tinTuc.setHinh_anh(fileName);

            return new ResponseData<>("Thành công",this.tinTucService.createOnePage(tinTuc));

        } catch (IOException e) {
            // Xử lý exception
            log.info("Lỗi upload file: {}",e.getMessage());
        }

        return new ResponseData<>("Thất bại",null);

    }

    @PutMapping(value = "/update/{id}")
    public ResponseData<?> updateOneTinTuc(@PathVariable("id") Long id,
                                        @RequestBody TinTuc tinTuc,
                                        @RequestParam("image") MultipartFile image) {

        String uploadDir = "/upload";
        try {
            // Lưu ảnh vào thư mục "upload"
            String fileName = image.getOriginalFilename();
            FileUploadUtil.saveFile(uploadDir, fileName, image);

            // Lưu thông tin của tour vào cơ sở dữ liệu
            tinTuc.setHinh_anh(fileName);

            return new ResponseData<>("Thành công",this.tinTucService.updateTinTuc(tinTuc,id));

        } catch (IOException e) {
            // Xử lý exception
            log.info("Lỗi upload file: {}",e.getMessage());
        }

        return new ResponseData<>("Cập nhật thất bại",null);

    }


}