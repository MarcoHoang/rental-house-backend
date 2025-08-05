package com.codegym.service;

import com.codegym.components.JwtTokenUtil;
import com.codegym.dto.request.LoginRequest;
import com.codegym.dto.request.RegisterRequest;
import com.codegym.entity.Role;
import com.codegym.entity.RoleName;
import com.codegym.entity.User;
import com.codegym.exception.DuplicateEmailException;
import com.codegym.exception.DuplicatePhoneException;
import com.codegym.mapper.UserMapper;
import com.codegym.repository.RoleRepository;
import com.codegym.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public String login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Email hoặc mật khẩu không chính xác."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Email hoặc mật khẩu không chính xác.");
        }

        try {
            return jwtTokenUtil.generateToken(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Không thể tạo token xác thực. Vui lòng thử lại sau.");
        }
    }

    @Transactional
    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("Email " + request.getEmail() + " đã được đăng ký.");
        }
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new DuplicatePhoneException("Số điện thoại " + request.getPhone() + " đã được đăng ký.");
        }

        Role role = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy vai trò mặc định (USER)."));

        User user = userMapper.toEntity(request, role);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

    }
}