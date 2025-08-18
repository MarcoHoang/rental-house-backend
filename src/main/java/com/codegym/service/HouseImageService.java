package com.codegym.service;

import com.codegym.entity.HouseImage;
import java.util.List;
import com.codegym.entity.House;

public interface HouseImageService {
    
    /**
     * Lấy danh sách ảnh của một nhà theo thứ tự sắp xếp
     */
    List<HouseImage> getHouseImagesByHouseId(Long houseId);
    
    /**
     * Thêm ảnh mới cho nhà
     */
    HouseImage addHouseImage(Long houseId, String imageUrl);
    
    /**
     * Xóa ảnh theo ID
     */
    void deleteHouseImage(Long imageId, Long houseId);
    
    /**
     * Cập nhật thứ tự sắp xếp của các ảnh
     */
    void updateImageOrder(Long houseId, List<Long> imageIds);
    
    /**
     * Xóa tất cả ảnh của một nhà
     */
    void deleteAllHouseImages(Long houseId);
    
    /**
     * Lấy thông tin nhà theo ID (cho debug)
     */
    House getHouseById(Long houseId);
}
