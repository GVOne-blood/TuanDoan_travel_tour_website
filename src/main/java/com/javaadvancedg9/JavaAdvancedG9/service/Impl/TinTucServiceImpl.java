package com.javaadvancedg9.JavaAdvancedG9.service.Impl;

import com.javaadvancedg9.JavaAdvancedG9.entity.TinTuc;
import com.javaadvancedg9.JavaAdvancedG9.repository.TinTucRepository;
import com.javaadvancedg9.JavaAdvancedG9.service.TinTucService;
import com.javaadvancedg9.JavaAdvancedG9.dto.TinTucDetailDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import com.javaadvancedg9.JavaAdvancedG9.dto.TinTucDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TinTucServiceImpl implements TinTucService {


    private final TinTucRepository tinTucRepository;    

    @Override
    public Page<TinTucDTO> getAllPage(Pageable pageable) {
        // 1. Gọi phương thức mới từ repository để lấy dữ liệu trong 1 query
        Page<TinTuc> tinTucPage = this.tinTucRepository.findAllWithUser(pageable);

        // 2. Dùng hàm map để chuyển đổi Page<TinTuc> thành Page<TinTucDto>
        return tinTucPage.map(this::convertToDto);
    }

    // Hàm helper để chuyển đổi một Entity TinTuc sang TinTucDto
    private TinTucDTO convertToDto(TinTuc tinTuc) {
        TinTucDTO dto = new TinTucDTO();
        dto.setId(tinTuc.getId());
        dto.setTieuDe(tinTuc.getTieu_de());
        dto.setTomTat(tinTuc.getTom_tat());
        dto.setHinhAnh(tinTuc.getHinh_anh());
        dto.setNgayDang(tinTuc.getNgay_dang());

        // Kiểm tra null để tránh lỗi và lấy tên người đăng
        if (tinTuc.getUser() != null) {
            dto.setTenNguoiDang(tinTuc.getUser().getFullname());
        } else {
            dto.setTenNguoiDang("Chưa xác định"); // Hoặc một giá trị mặc định nào đó
        }
        return dto;
    }


    @Override
    public TinTucDetailDTO findOnePage(Long id) {
        // Vẫn gọi phương thức đã có JOIN FETCH để tối ưu query
        TinTuc tinTuc = this.tinTucRepository.findOnePage(id);
        if (tinTuc == null) {
            return null;
        }
        // Chuyển đổi Entity sang Detail DTO
        return convertToDetailDTO(tinTuc);
    }
    private TinTucDetailDTO convertToDetailDTO(TinTuc tinTuc) {
        TinTucDetailDTO detailDto = new TinTucDetailDTO();
        detailDto.setId(tinTuc.getId());
        detailDto.setTieuDe(tinTuc.getTieu_de());
        detailDto.setTomTat(tinTuc.getTom_tat());
        detailDto.setNoiDung(tinTuc.getNoi_dung()); // Thêm nội dung
        detailDto.setHinhAnh(tinTuc.getHinh_anh());
        detailDto.setNgayDang(tinTuc.getNgay_dang());
        if (tinTuc.getUser() != null) {
            detailDto.setTenNguoiDang(tinTuc.getUser().getFullname());
            // detailDto.setAvatarNguoiDang(tinTuc.getUser().getAvatarUrl()); // Ví dụ nếu có
        } else {
            detailDto.setTenNguoiDang("Chưa xác định");
        }
        return detailDto;
    }

    @Override
    public void deleteOnePage(Long id) {
        this.tinTucRepository.deleteById(id);
    }

    @Override
    public TinTuc createOnePage(TinTuc newTinTuc) {
        if(this.tinTucRepository.checkExistTieuDe(newTinTuc.getTieu_de())) {
            return null;
        }
        return this.tinTucRepository.save(newTinTuc);
    }

    @Override
    public TinTuc updateTinTuc(TinTuc updateTinTuc, Long id) {

        TinTuc tintuc = this.tinTucRepository.findOnePage(id);

        if(tintuc==null) {
            return null;
        }

        tintuc.setHinh_anh(updateTinTuc.getHinh_anh());
        tintuc.setNgay_dang(updateTinTuc.getNgay_dang());
        tintuc.setTom_tat(updateTinTuc.getTom_tat());
        tintuc.setTieu_de(updateTinTuc.getTieu_de());
        tintuc.setNoi_dung(updateTinTuc.getNoi_dung());


        return this.tinTucRepository.save(tintuc);
    }
}
