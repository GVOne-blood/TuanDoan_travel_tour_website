package com.javaadvancedg9.JavaAdvancedG9.service.Impl;

import com.javaadvancedg9.JavaAdvancedG9.dto.TourDTO;
import com.javaadvancedg9.JavaAdvancedG9.entity.Image;
import com.javaadvancedg9.JavaAdvancedG9.entity.Tour;
import com.javaadvancedg9.JavaAdvancedG9.repository.TourRepository;
import com.javaadvancedg9.JavaAdvancedG9.service.TourService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TourServiceImpl implements TourService {

    private final TourRepository tourRepository;

    /**
     * Phương thức tìm kiếm chính, được sử dụng bởi API công khai.
     * Nó gọi repository để lấy Page<Tour> và sau đó map sang Page<TourDTO>.
     */
    @Override
    public Page<TourDTO> findAllTourAdmin(String tour_name, Long price_from, Long price_to, Date departing_at, Integer tour_type, Pageable pageable) {
        Page<Tour> tourPage = tourRepository.findAllAdmin(tour_name, price_from, price_to, departing_at, tour_type, pageable);

        // Sử dụng hàm map của Page để chuyển đổi mỗi Tour thành TourDTO
        return tourPage.map(this::convertToDto);
    }
    private TourDTO convertToDto(Tour tour) {
        TourDTO dto = new TourDTO();

        // Map các trường cơ bản
        dto.setId(tour.getId());
        dto.setTour_name(tour.getTour_name());
        dto.setTour_introduction(tour.getTour_introduction());
        dto.setPrice(tour.getPrice());
        dto.setDestination(tour.getDestination());
        dto.setTour_duration(tour.getTour_duration());
        dto.setStatus(tour.getStatus());
        // Thêm các trường khác nếu cần (rating, reviews,...)
        dto.setTour_details(tour.getTour_details());
        dto.setTour_illustration(tour.getTour_illustration());
        // Tạo link chi tiết
        dto.setDetails_link("tour-details.html?id=" + tour.getId());

        // Xử lý danh sách ảnh
        if (tour.getImages() != null && !tour.getImages().isEmpty()) {
            List<String> imageUrls = tour.getImages().stream()
                    .map(Image::getUrl) // Lấy URL từ mỗi đối tượng Image
                    .collect(Collectors.toList());
            dto.setImageUrls(imageUrls);
        } else {
            // Nếu không có ảnh, trả về một list rỗng để tránh lỗi ở frontend
            dto.setImageUrls(new ArrayList<>());
        }

        return dto;
    }

    @Override
    public TourDTO findTourById(Long id) {
        // Sử dụng findById của JpaRepository trả về Optional để an toàn hơn
        Optional<Tour> tourOptional = this.tourRepository.findById(id);
        // Nếu tìm thấy tour, chuyển nó sang DTO, nếu không thì trả về null
        return tourOptional.map(this::convertToDto).orElse(null);
    }

    // Các phương thức còn lại (add, update, delete) có thể giữ nguyên vì chúng
    // hoạt động trực tiếp với entity và không cần trả về danh sách ảnh phức tạp.

    @Override
    public boolean saveTour(Tour tour) {
        return this.tourRepository.save(tour) != null;
    }

    @Override
    public Tour findFirstByOrderByIdDesc() {
        return this.tourRepository.findFirstByOrderByIdDesc();
    }

    @Override
    public Tour addTour(TourDTO tourDTO) {
        Tour tour = new Tour();
        tour.setTour_name(tourDTO.getTour_name());
        tour.setTour_illustration(null); // Trường này không còn dùng trong DTO mới
        tour.setTour_type(tourDTO.getTour_type());
        tour.setPrice(tourDTO.getPrice());
        tour.setTour_introduction(tourDTO.getTour_introduction());
        tour.setDestination(tourDTO.getDestination());
        tour.setTour_details(tourDTO.getTour_details());
        tour.setDeparture_point(tourDTO.getDeparture_point());
        tour.setDeparting_at(tourDTO.getDeparting_at());
        tour.setTour_duration(tourDTO.getTour_duration());
        tour.setStatus(tourDTO.getStatus());
        tour.setEnd_at(tourDTO.getEnd_at());
        return this.tourRepository.save(tour);
    }

    @Override
    public Tour updateTour(TourDTO newTour, Long id) {
        Optional<Tour> tourOptional = this.tourRepository.findById(id);
        if (tourOptional.isPresent()) {
            Tour updatedTour = tourOptional.get();
            updatedTour.setTour_name(newTour.getTour_name());
            updatedTour.setTour_type(newTour.getTour_type());
            updatedTour.setPrice(newTour.getPrice());
            updatedTour.setTour_introduction(newTour.getTour_introduction());
            // Cập nhật các trường khác tương tự...
            return this.tourRepository.save(updatedTour);
        }
        return null;
    }

    @Override
    public boolean deleteTour(Long id) {
        Optional<Tour> tour = this.tourRepository.findById(id);
        if (tour.isPresent()) {
            if (!this.tourRepository.existsBookingByTourId(id)) {
                this.tourRepository.deleteById(id);
                return true;
            }
        }
        return false;
    }
}