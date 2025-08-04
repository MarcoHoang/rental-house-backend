package com.codegym.service;

import com.codegym.components.JwtTokenUtil;
import com.codegym.dto.ApiResponse;
import com.codegym.dto.request.LoginRequest;
import com.codegym.dto.request.RegisterRequest;
import com.codegym.entity.Role;
import com.codegym.entity.User;
import com.codegym.mapper.UserMapper;
import com.codegym.repository.RoleRepository;
import com.codegym.repository.UserRepository;
import com.codegym.utils.MessageCode;
import com.codegym.utils.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired private UserMapper userMapper;
    @Autowired private JwtTokenUtil jwtTokenUtil;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private RoleRepository roleRepository;


    public ApiResponse<String> login(LoginRequest request) throws Exception {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(MessageCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ApiResponse.error(StatusCode.INVALID_PASSWORD, MessageCode.INVALID_PASSWORD);
        }
        String token = jwtTokenUtil.generateToken(user);
        return ApiResponse.success(StatusCode.SUCCESS, MessageCode.LOGIN_SUCCESS, token);
    }


    @Transactional
    public ApiResponse<String> register(RegisterRequest request) {
        boolean isEmailAlreadyRegistered = userRepository.existsByEmail(request.getEmail());
        if (isEmailAlreadyRegistered) {
            return ApiResponse.error(StatusCode.EMAIL_ALREADY_EXISTS, MessageCode.EMAIL_ALREADY_EXISTS);
        }
        boolean isPhoneAlreadyRegistered = userRepository.findByPhone(request.getPhone()).isPresent();
        if (isPhoneAlreadyRegistered) {
            return ApiResponse.error(StatusCode.PHONE_ALREADY_EXISTS, MessageCode.PHONE_ALREADY_EXISTS);
        }

        Role role = Optional.ofNullable(request.getRoleId())
                .flatMap(roleRepository::findById)
                .or(() -> roleRepository.findByName("USER"))
                .orElse(null);
        if (role == null) {
            return ApiResponse.error(StatusCode.ROLE_NOT_FOUND, MessageCode.ROLE_NOT_FOUND);
        }

        User user = userMapper.toEntity(request, role);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        return ApiResponse.success(StatusCode.SUCCESS, MessageCode.REGISTER_SUCCESS, null);
    }

}
