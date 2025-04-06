package com.javaadvancedg9.JavaAdvancedG9.controller.api;

import com.javaadvancedg9.JavaAdvancedG9.dto.response.ResponseData;
import com.javaadvancedg9.JavaAdvancedG9.dto.TourDTO;
import com.javaadvancedg9.JavaAdvancedG9.dto.TourStartAddDTO;
import com.javaadvancedg9.JavaAdvancedG9.entity.Tour;
import com.javaadvancedg9.JavaAdvancedG9.entity.TourStart;
import com.javaadvancedg9.JavaAdvancedG9.repository.TourStartRepository;
import com.javaadvancedg9.JavaAdvancedG9.service.ImageService;
import com.javaadvancedg9.JavaAdvancedG9.service.TourService;
import com.javaadvancedg9.JavaAdvancedG9.service.UserService;
import com.javaadvancedg9.JavaAdvancedG9.utilities.DateUtils;
import com.javaadvancedg9.JavaAdvancedG9.utilities.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tour")
public class TourController {

    private final TourService tourService;

    private final TourStartRepository tourStartRepository;

    private final ImageService imageService;

    private final UserService userService;

    @GetMapping("/getAllTour")
    public ResponseData<?> getAllTour(@RequestParam(value="tour_name",required = false) String tour_name,
                                   @RequestParam(value="price_from",required = false) Long price_from,
                                   @RequestParam(value="price_to",required = false) Long price_to,
                                   @RequestParam(value="departing_at",required = false) Date departing_at,
                                   @RequestParam(value="tour_type",required = false) Integer tour_type,
                                   @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize,
                                   @RequestParam(value = "pageIndex") Integer pageIndex
    ) {
        if(!this.userService.checkAdminLogin()) {
            return new ResponseData<>("Không có quyền truy cập",null);
        }

        Page<TourDTO> page = this.tourService.findAllTourAdmin(tour_name,price_from,price_to,departing_at,tour_type, PageRequest.of(pageIndex,pageSize));

        return new ResponseData<>("Thành công",page.getContent());
    }

    @GetMapping("/{id}")
    public ResponseData<?> getOneTour(@PathVariable("id") Long id) {

        if(!this.userService.checkAdminLogin()) {
            return new ResponseData<>("Không có quyền truy cập",null);
        }

        TourDTO tour = this.tourService.findTourById(id);

        if(tour!=null) {
            return new ResponseData<>("Thành công",tour);
        }
        return new ResponseData<>("Thất bại" ,null);
    }

    @PostMapping("/test-up-anh")
    public ResponseData<?> testUpAnh(@RequestParam("image") MultipartFile image) {

        if(!this.userService.checkAdminLogin()) {
            return new ResponseData<>("Không có quyền truy cập",null);
        }

        String uploadDir = "HoangMinhWeb/src/main/resources/static/public/img";

        try {
            // Lưu ảnh vào thư mục "upload"
            String fileName = UUID.randomUUID().toString()+ image.getOriginalFilename();
            FileUploadUtil.saveFile(uploadDir, fileName, image);

            return new ResponseData<>("Thành công",fileName);
        } catch (IOException e) {
            // Xử lý exception
            log.info("Lỗi upload file: {}",e.getMessage());
        }
        return new ResponseData<>("Thêm thất bại",null);
    }

    @PostMapping("/add/image")
    public ResponseData<?> createTourImage(@RequestParam("image")MultipartFile image) {

        if(!this.userService.checkAdminLogin()) {
            return new ResponseData<>("Không có quyền truy cập",null);
        }

        String uploadDir = "HoangMinhWeb/src/main/resources/static/public/img";

        Tour tour = tourService.findFirstByOrderByIdDesc();

        try {
            // Lưu ảnh vào thư mục "upload"
            String fileName = UUID.randomUUID().toString()+ image.getOriginalFilename();
            FileUploadUtil.saveFile(uploadDir, fileName, image);

            // Lưu thông tin của tour vào cơ sở dữ liệu
            tour.setTour_illustration(fileName);
            return new ResponseData<>("Thành công",this.tourService.saveTour(tour));
        } catch (IOException  e) {
            // Xử lý exception
            log.info("Lỗi upload file: {}",e.getMessage());
        }
        return new ResponseData<>("Thêm thất bại",null);
    }

    @PostMapping("/add")
    public ResponseData<?> createTour(@RequestBody TourDTO tourDTO) {

        if(!this.userService.checkAdminLogin()) {
            return new ResponseData("Không có quyền truy cập",null);
        }

        String[] dataGet = tourDTO.getDestination().split("/");
        tourDTO.setDestination(dataGet[0]);
        tourDTO.setDeparting_at(DateUtils.convertStringToDate(dataGet[1]));
        tourDTO.setEnd_at(DateUtils.convertStringToDate(dataGet[2]));

        Tour tour = this.tourService.addTour(tourDTO);
        if(tour!=null) {
            return new ResponseData<>("Thành công",tour);
        }
        return new ResponseData<>("Thêm thất bại",null);

    }


    @PutMapping("/update/image/{id}")
    public ResponseData<?> updateTourImage(@PathVariable("id") Long id, @RequestParam("image") MultipartFile image) {

        if(!this.userService.checkAdminLogin()) {
            return new ResponseData<>("Không có quyền truy cập",null);
        }

        String uploadDir = "HoangMinhWeb/src/main/resources/static/public/img";

        TourDTO tourDTO = this.tourService.findTourById(id);
        try {
            // Lưu ảnh vào thư mục "upload"
            String fileName = UUID.randomUUID().toString()+ image.getOriginalFilename();
            FileUploadUtil.saveFile(uploadDir, fileName, image);

            // Lưu thông tin của tour vào cơ sở dữ liệu
            tourDTO.setTour_illustration(fileName);
            Tour updateTour = this.tourService.updateTour(tourDTO,id);
            if(updateTour!=null) {
                return new ResponseData<>("Thành công",updateTour);
            }

        } catch (IOException  e) {
            // Xử lý exception
            log.info("Lỗi upload file: {}",e.getMessage());
        }
        return new ResponseData<>("Update thất bại",null);
    }

    @PutMapping("/update/{id}")
    public ResponseData<?> updateTour(@PathVariable("id") Long id, @RequestBody TourDTO tourDTO) {

        if(!this.userService.checkAdminLogin()) {
            return new ResponseData<>("Không có quyền truy cập",null);
        }

        String[] dataGet = tourDTO.getDestination().split("/");
        tourDTO.setDestination(dataGet[0]);
        tourDTO.setDeparting_at(DateUtils.convertStringToDate(dataGet[1]));
        tourDTO.setEnd_at(DateUtils.convertStringToDate(dataGet[2]));
        Tour updateTour = this.tourService.updateTour(tourDTO,id);
        if(updateTour!=null) {
            return new ResponseData<>("Thành công",updateTour);
        }

        return new ResponseData<>("Update thất bại",null);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseData<?> deleteTour(@PathVariable("id") Long id) {

        if(!this.userService.checkAdminLogin()) {
            return new ResponseData<>("Không có quyền truy cập",null);
        }

        if(this.tourService.findTourById(id)!=null) {
            if(this.tourService.deleteTour(id)) {
                return new ResponseData<>("Xóa thành công",null);
            }

        }
        return new ResponseData<>("Xóa thất bại",null);
    }

    @GetMapping("/getAllImageOfTour/{id}")
    public ResponseData<?> getAllImageOfTour(@PathVariable("id") Long id) {

        if(!this.userService.checkAdminLogin()) {
            return new ResponseData("Không có quyền truy cập",null);
        }

        return new ResponseData<>("Thành công",this.imageService.findByTourId(id));
    }

    @PostMapping("/add-image/{id}")
    public ResponseData<?> addImage(@PathVariable("id") Long id, @RequestParam("image") MultipartFile image) {

        if(!this.userService.checkAdminLogin()) {
            return new ResponseData<>("Không có quyền truy cập",null);
        }

        String uploadDir = "HoangMinhWeb/src/main/resources/static/public/img";

        try {
            // Lưu ảnh vào thư mục "upload"
            String fileName = UUID.randomUUID()+image.getOriginalFilename();
            FileUploadUtil.saveFile(uploadDir, fileName, image);

            if(this.tourService.findTourById(id)!=null) {

                return new ResponseData<>("Thêm thành công",this.imageService.addToTour(id,fileName));
            }
        } catch (IOException  e) {
            // Xử lý exception
            log.info("Lỗi upload file: {}",e.getMessage());
        }

        return new ResponseData<>("Lỗi khi thêm",null);

    }

    @GetMapping("/StartDate/{id}")
    public ResponseData<?> getAllStartDate(@PathVariable("id") Long id) {

        if(!this.userService.checkAdminLogin()) {
            return new ResponseData<>("Không có quyền truy cập",null);
        }

        return new ResponseData<>("Thành công",this.tourStartRepository.getDateStartByTourId(id));
    }


    @DeleteMapping("/StartDate/delete/{id}")
    public ResponseData<?> deleteStartDate(@PathVariable("id") Long id) {

        if(!this.userService.checkAdminLogin()) {
            return new ResponseData<>("Không có quyền truy cập",null);
        }

        this.tourStartRepository.deleteById(id);
        return new ResponseData<>("Xóa thành công",null);
    }

    @DeleteMapping("/TourImage/delete/{id}")
    public ResponseData<?> deleteTourImage(@PathVariable("id") Long id) {

        if(!this.userService.checkAdminLogin()) {
            return new ResponseData<>("Không có quyền truy cập",null);
        }

        this.imageService.deleteById(id);

        return new ResponseData<>("Xóa ảnh thành công",null);
    }

    @PostMapping("/add-date/{id}")
    public ResponseData<?> addStartDate(@PathVariable("id") Long id , @RequestBody TourStartAddDTO toutStartAddDTO) {

        if(!this.userService.checkAdminLogin()) {
            return new ResponseData<>("Không có quyền truy cập",null);
        }

        Date departing_at = DateUtils.convertStringToDate(toutStartAddDTO.getDeparting_at());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(departing_at);
        calendar.add(Calendar.DAY_OF_MONTH, 1);


        if(this.tourService.findTourById(id)!=null) {

            TourStart tourStart = new TourStart();

            tourStart.setTour_id(id);
            tourStart.setDeparting_at(calendar.getTime());

            return new ResponseData<>("Thêm thành công",this.tourStartRepository.save(tourStart));
        }

        return new ResponseData<>("Tour không tồn tại khi thêm",null);
    }

}

