package com.codegym.service.impl;

import com.codegym.dto.response.NotificationDTO;
import com.codegym.entity.Notification;
import com.codegym.entity.User;
import com.codegym.repository.NotificationRepository;
import com.codegym.repository.UserRepository;
import com.codegym.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // Thay thế @Autowired bằng constructor injection qua Lombok
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    // ---- SERVICE IMPLEMENTATIONS ----

    @Override
    @Transactional(readOnly = true) // Thêm transactional cho các phương thức chỉ đọc
    public List<NotificationDTO> findByReceiverId(Long userId) {
        // Thêm bước kiểm tra người dùng có tồn tại không
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("Không tìm thấy người dùng với ID: " + userId);
        }
        return notificationRepository.findByReceiverIdOrderByCreatedAtDesc(userId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public NotificationDTO create(NotificationDTO dto) {
        // 1. Tìm người dùng nhận, với thông báo lỗi rõ ràng hơn
        User user = userRepository.findById(dto.getReceiverId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng nhận thông báo với ID: " + dto.getReceiverId()));

        // 2. Tạo entity mới (sử dụng builder là rất tốt!)
        Notification notification = Notification.builder()
                .receiver(user)
                .type(dto.getType()) // Giữ lại các trường bạn đã có
                .content(dto.getContent())
                .isRead(false) // Mặc định khi tạo là chưa đọc
                // @CreationTimestamp sẽ tự động xử lý createdAt, không cần set thủ công
                .build();

        // 3. Lưu và trả về DTO
        return toDTO(notificationRepository.save(notification));
    }

    @Override
    @Transactional
    public NotificationDTO markAsRead(Long id) {
        // Lấy thông báo với thông báo lỗi rõ ràng
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy thông báo để đánh dấu đã đọc với ID: " + id));

        // Tối ưu: Nếu đã đọc rồi thì không cần ghi vào DB nữa
        if (Boolean.TRUE.equals(notification.getIsRead())) {
            return toDTO(notification);
        }

        notification.setIsRead(true);
        return toDTO(notificationRepository.save(notification));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        // Thêm bước kiểm tra sự tồn tại trước khi xóa
        if (!notificationRepository.existsById(id)) {
            throw new EntityNotFoundException("Không thể xóa. Thông báo với ID: " + id + " không tồn tại.");
        }
        notificationRepository.deleteById(id);
    }

    // ---- MAPPER ----
    // Đặt private mapper ở cuối cho gọn gàng
    private NotificationDTO toDTO(Notification n) {
        return NotificationDTO.builder()
                .id(n.getId())
                .receiverId(n.getReceiver().getId())
                .type(n.getType())
                .content(n.getContent())
                .isRead(n.getIsRead())
                .createdAt(n.getCreatedAt())
                .build();
    }
}