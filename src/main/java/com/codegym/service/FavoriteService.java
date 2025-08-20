package com.codegym.service;

import com.codegym.entity.Favorite;
import com.codegym.entity.House;
import com.codegym.entity.User;
import com.codegym.entity.RoleName;
import com.codegym.exception.AppException;
import com.codegym.repository.FavoriteRepository;
import com.codegym.repository.HouseRepository;
import com.codegym.repository.UserRepository;
import com.codegym.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final HouseRepository houseRepository;

    @Transactional
    public boolean toggleFavorite(Long userId, Long houseId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        House house = houseRepository.findById(houseId)
                .orElseThrow(() -> new RuntimeException("House not found"));

        // Kiểm tra xem người dùng có phải là chủ nhà của căn nhà này không
        if (user.getRole().getName().equals(RoleName.HOST) && 
            house.getHost().getId().equals(user.getId())) {
            throw new AppException(StatusCode.HOST_CANNOT_FAVORITE_OWN_HOUSE);
        }

        // Kiểm tra xem đã yêu thích chưa
        if (favoriteRepository.existsByUserIdAndHouseId(userId, houseId)) {
            // Nếu đã yêu thích thì xóa
            favoriteRepository.deleteByUserIdAndHouseId(userId, houseId);
            return false; // Đã bỏ yêu thích
        } else {
            // Nếu chưa yêu thích thì thêm mới
            Favorite favorite = Favorite.builder()
                    .user(user)
                    .house(house)
                    .uniqueConstraint(userId + "_" + houseId)
                    .build();
            favoriteRepository.save(favorite);
            return true; // Đã yêu thích
        }
    }

    public boolean isFavorite(Long userId, Long houseId) {
        return favoriteRepository.existsByUserIdAndHouseId(userId, houseId);
    }

    public List<Long> getFavoriteHouseIds(Long userId) {
        return favoriteRepository.findHouseIdsByUserId(userId);
    }

    public Long getFavoriteCount(Long houseId) {
        return favoriteRepository.countByHouseId(houseId);
    }
}
