package com.codegym.controller.api;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.request.ChangePasswordRequest;
import com.codegym.dto.response.UserDTO;
import com.codegym.service.UserService;
import com.codegym.utils.StatusCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final MessageSource messageSource;

    @PutMapping("/{id}/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(@PathVariable Long id, @RequestBody @Valid ChangePasswordRequest request, Locale locale) {
        userService.changePassword(id, request.getNewPassword());
        return ResponseEntity.ok(ApiResponse.success(StatusCode.PASSWORD_CHANGED, messageSource, locale));
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<ApiResponse<UserDTO>> updateProfile(@PathVariable Long id, @RequestBody @Valid UserDTO dto, Locale locale) {
        UserDTO updated = userService.updateProfile(id, dto);
        return ResponseEntity.ok(ApiResponse.success(updated, StatusCode.PROFILE_UPDATED, messageSource, locale));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllCustomers(Locale locale) {
        List<UserDTO> customers = userService.getAllCustomers();
        return ResponseEntity.ok(ApiResponse.success(customers, StatusCode.GET_LIST_SUCCESS, messageSource, locale));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getCustomerById(@PathVariable Long id, Locale locale) {
        UserDTO dto = userService.getCustomerById(id);
        return ResponseEntity.ok(ApiResponse.success(dto, StatusCode.SUCCESS, messageSource, locale));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserDTO>> createCustomer(@RequestBody @Valid UserDTO dto, Locale locale) {
        UserDTO created = userService.createCustomer(dto);
        return new ResponseEntity<>(ApiResponse.success(created, StatusCode.CREATED_SUCCESS, messageSource, locale), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> updateCustomer(@PathVariable Long id, @RequestBody @Valid UserDTO dto, Locale locale) {
        UserDTO updated = userService.updateCustomer(id, dto);
        return ResponseEntity.ok(ApiResponse.success(updated, StatusCode.UPDATED_SUCCESS, messageSource, locale));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(@PathVariable Long id, Locale locale) {
        userService.deleteCustomer(id);
        return ResponseEntity.ok(ApiResponse.success(StatusCode.DELETED_SUCCESS, messageSource, locale));
    }
}