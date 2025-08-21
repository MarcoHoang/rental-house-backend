package com.codegym.service.impl;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.response.RentalHistoryDTO;
import com.codegym.dto.response.UserDTO;
import com.codegym.dto.response.UserDetailAdminDTO;
import com.codegym.dto.response.HostDTO;
import com.codegym.entity.*;
import com.codegym.exception.AppException;
import com.codegym.exception.ResourceNotFoundException;
import com.codegym.repository.PasswordResetOtpRepository;
import com.codegym.repository.RentalRepository;
import com.codegym.repository.UserRepository;
import com.codegym.service.EmailService;
import com.codegym.service.UserService;
import com.codegym.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetOtpRepository passwordResetOtpRepository;
    private final EmailService emailService;
    private final RentalRepository rentalRepository;


    private UserDTO toDTO(User user) {
        if (user == null) return null;

        return UserDTO.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .address(user.getAddress())
                .phone(user.getPhone())
                .avatarUrl(user.getAvatarUrl())
                .email(user.getEmail())
                .birthDate(user.getBirthDate())
                .active(user.isActive())
                .roleName(user.getRole() != null ? user.getRole().getName() : null)
                .build();
    }

    private User toEntityForCreate(UserDTO dto) {
        if (dto == null) return null;

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setFullName(dto.getFullName());
        user.setAddress(dto.getAddress());
        user.setPhone(dto.getPhone());
        user.setBirthDate(dto.getBirthDate());
        user.setAvatarUrl(dto.getAvatarUrl() != null ? dto.getAvatarUrl() : "/images/default-avatar.png");
        user.setActive(dto.isActive());

        if (dto.getRoleName() != null) {
            Role role = new Role();
            role.setName(dto.getRoleName());
            user.setRole(role);
        }

        return user;
    }

    private void updateEntityFromDTO(User user, UserDTO dto) {
        user.setFullName(dto.getFullName());
        user.setAddress(dto.getAddress());

        if (dto.getPhone() != null && !user.getPhone().equals(dto.getPhone())) {
            if (userRepository.existsByPhone(dto.getPhone())) {
                throw new AppException(StatusCode.PHONE_ALREADY_EXISTS);
            }
            user.setPhone(dto.getPhone());
        }

        if (dto.getBirthDate() != null) {
            user.setBirthDate(dto.getBirthDate());
        }

        if(dto.getAvatarUrl() != null) {
            user.setAvatarUrl(dto.getAvatarUrl());
        }
    }

    private User findUserByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.USER_NOT_FOUND, id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        List<RoleName> rolesToExclude = Arrays.asList(RoleName.ADMIN, RoleName.HOST);

        Page<User> userPage = userRepository.findByRole_NameNotIn(rolesToExclude, pageable);

        return userPage.map(this::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        User user = findUserByIdOrThrow(id);
        return toDTO(user);
    }

    @Override
    @Transactional
    public UserDTO createUser(UserDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new AppException(StatusCode.EMAIL_ALREADY_EXISTS);
        }
        if (userRepository.existsByPhone(dto.getPhone())) {
            throw new AppException(StatusCode.PHONE_ALREADY_EXISTS);
        }

        User newUser = toEntityForCreate(dto);
        User savedUser = userRepository.save(newUser);
        return toDTO(savedUser);
    }

    @Override
    @Transactional
    public UserDTO updateUser(Long id, UserDTO dto) {
        User user = findUserByIdOrThrow(id);
        updateEntityFromDTO(user, dto);
        User updatedUser = userRepository.save(user);
        return toDTO(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = findUserByIdOrThrow(id);
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public void changePassword(Long id, String oldPassword, String newPassword, String confirmPassword) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        User currentUser = userRepository.findByEmail(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.USER_NOT_FOUND, currentUsername));

        if (!currentUser.getId().equals(id)) {
            throw new AppException(StatusCode.FORBIDDEN_ACTION);
        }

        if (!passwordEncoder.matches(oldPassword, currentUser.getPassword())) {
            throw new AppException(StatusCode.INVALID_PASSWORD);
        }

        if (!newPassword.equals(confirmPassword)) {
            throw new AppException(StatusCode.PASSWORD_CONFIRMATION_MISMATCH);
        }

        if (passwordEncoder.matches(newPassword, currentUser.getPassword())) {
            throw new AppException(StatusCode.DUPLICATE_OLD_PASSWORD);
        }

        currentUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(currentUser);
    }

    @Override
    @Transactional
    public UserDTO updateProfile(Long id, UserDTO dto) {
        User user = findUserByIdOrThrow(id);
        updateEntityFromDTO(user, dto);
        User updatedUser = userRepository.save(user);
        return toDTO(updatedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getCurrentUserProfile() {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        User currentUser = userRepository.findByEmail(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.USER_NOT_FOUND, currentUsername));

        return toDTO(currentUser);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isCurrentUserHost() {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        User currentUser = userRepository.findByEmail(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.USER_NOT_FOUND, currentUsername));

        return currentUser.getRole().getName().equals(RoleName.HOST);
    }

    @Override
    @Transactional(readOnly = true)
    public HostDTO getCurrentUserHostInfo() {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        User currentUser = userRepository.findByEmail(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.USER_NOT_FOUND, currentUsername));

        if (!currentUser.getRole().getName().equals(RoleName.HOST)) {
            throw new AppException(StatusCode.UNAUTHORIZED, "Người dùng không phải là chủ nhà");
        }

        if (currentUser.getHost() == null) {
            throw new AppException(StatusCode.RESOURCE_NOT_FOUND, "thông tin chủ nhà");
        }

        Host host = currentUser.getHost();
        return HostDTO.builder()
                .id(host.getId())
                .fullName(currentUser.getFullName())
                .username(currentUser.getUsername())
                .email(currentUser.getEmail())
                .phone(currentUser.getPhone())
                .avatar(currentUser.getAvatarUrl())
                .avatarUrl(currentUser.getAvatarUrl())
                .nationalId(host.getNationalId())
                .proofOfOwnershipUrl(host.getProofOfOwnershipUrl())
                .address(host.getAddress())
                .approvedDate(host.getApprovedDate())
                .approved(true) // Nếu đã có host record thì đã được approved
                .build();
    }


    @Override
    @Transactional
    public void requestPasswordReset(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return;
        }

        User user = userOpt.get();

        // Xóa OTP cũ nếu có
        passwordResetOtpRepository.deleteByUser(user);

        // Tạo OTP 6 số
        String otp = String.format("%06d", new Random().nextInt(1000000));
        PasswordResetOtp resetOtp = new PasswordResetOtp();
        resetOtp.setOtp(otp);
        resetOtp.setUser(user);
        resetOtp.setExpiryDate(LocalDateTime.now().plusMinutes(5)); // OTP hết hạn sau 5 phút
        passwordResetOtpRepository.save(resetOtp);

        emailService.sendResetPasswordEmail(email, otp);
    }


    @Override
    @Transactional
    public void resetPassword(String otp, String newPassword) {
        PasswordResetOtp resetOtp = passwordResetOtpRepository.findByOtp(otp)
                .orElseThrow(() -> new AppException(StatusCode.TOKEN_INVALID));

        if (resetOtp.getExpiryDate().isBefore(LocalDateTime.now())) {
            passwordResetOtpRepository.delete(resetOtp);
            throw new AppException(StatusCode.TOKEN_INVALID);
        }

        User user = resetOtp.getUser();

        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new AppException(StatusCode.DUPLICATE_OLD_PASSWORD);
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        passwordResetOtpRepository.delete(resetOtp);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean verifyOtp(String otp) {
        Optional<PasswordResetOtp> otpOpt = passwordResetOtpRepository.findByOtp(otp);
        
        if (otpOpt.isEmpty()) {
            return false;
        }

        PasswordResetOtp resetOtp = otpOpt.get();
        
        if (resetOtp.getExpiryDate().isBefore(LocalDateTime.now())) {
            passwordResetOtpRepository.delete(resetOtp);
            return false;
        }

        return true;
    }

    @Override
    @Transactional
    public void updateUserStatus(Long userId, boolean active) {
        User userToUpdate = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.USER_NOT_FOUND, userId));

        String currentAdminEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentAdmin = userRepository.findByEmail(currentAdminEmail)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.USER_NOT_FOUND, currentAdminEmail));

        if (userToUpdate.getId().equals(currentAdmin.getId())) {
            throw new AppException(StatusCode.FORBIDDEN_ACTION, "Admin cannot lock their own account.");
        }

        if (userToUpdate.getRole().getName().equals(RoleName.ADMIN)) {
            throw new AppException(StatusCode.FORBIDDEN_ACTION, "Cannot change status of another admin account.");
        }

        userToUpdate.setActive(active);
        userRepository.save(userToUpdate);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetailAdminDTO findUserDetailById(Long userId) {
        User user = findUserByIdOrThrow(userId);

        List<Rental> rentalRecords = rentalRepository.findByRenterIdOrderByStartDateDesc(user.getId());

        Double totalSpent = rentalRepository.sumTotalPriceByRenterId(user.getId());
        if (totalSpent == null) {
            totalSpent = 0.0;
        }

        List<RentalHistoryDTO> rentalHistory;
        if (rentalRecords != null && !rentalRecords.isEmpty()) {
            rentalHistory = rentalRecords.stream()
                    .map(rental -> RentalHistoryDTO.builder()
                            .houseId(rental.getHouse().getId())
                            .houseName(rental.getHouse().getTitle())
                            .checkinDate(rental.getStartDate().toLocalDate())
                            .checkoutDate(rental.getEndDate().toLocalDate())
                            .price(rental.getTotalPrice())
                            .build())
                    .collect(Collectors.toList());
        } else {
            rentalHistory = Collections.emptyList();
        }

        return UserDetailAdminDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .avatarUrl(user.getAvatarUrl())
                .active(user.isActive())
                .birthDate(user.getBirthDate())
                .address(user.getAddress())
                .totalSpent(totalSpent)
                .rentalHistory(rentalHistory)
                .build();
    }

    // Dashboard statistics methods
    @Override
    @Transactional(readOnly = true)
    public long countAllUsers() {
        return userRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long countHosts() {
        return userRepository.countByRoleName(RoleName.HOST);
    }

    @Override
    @Transactional(readOnly = true)
    public long countAdmins() {
        return userRepository.countByRoleName(RoleName.ADMIN);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> searchUsers(String keyword, String role, Boolean active) {
        List<User> users;
        
        // Nếu có keyword, tìm kiếm theo keyword
        if (keyword != null && !keyword.trim().isEmpty()) {
            String searchTerm = keyword.trim().toLowerCase();
            users = userRepository.findByKeywordAndFilters(searchTerm, role, active);
        } else {
            // Nếu không có keyword, chỉ filter theo role và active
            users = userRepository.findByFilters(role, active);
        }
        
        return users.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

}