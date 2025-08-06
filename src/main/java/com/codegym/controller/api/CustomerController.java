package com.codegym.controller.api;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.request.ChangePasswordRequest;
import com.codegym.dto.response.CustomerDTO;
import com.codegym.service.CustomerService;
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
@RequestMapping("/api/customers")
@CrossOrigin("*")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final MessageSource messageSource;

    @PutMapping("/{id}/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(@PathVariable Long id, @RequestBody @Valid ChangePasswordRequest request, Locale locale) {
        customerService.changePassword(id, request.getNewPassword());
        return ResponseEntity.ok(ApiResponse.success(StatusCode.PASSWORD_CHANGED, messageSource, locale));
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<ApiResponse<CustomerDTO>> updateProfile(@PathVariable Long id, @RequestBody @Valid CustomerDTO dto, Locale locale) {
        CustomerDTO updated = customerService.updateProfile(id, dto);
        return ResponseEntity.ok(ApiResponse.success(updated, StatusCode.PROFILE_UPDATED, messageSource, locale));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CustomerDTO>>> getAllCustomers(Locale locale) {
        List<CustomerDTO> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(ApiResponse.success(customers, StatusCode.GET_LIST_SUCCESS, messageSource, locale));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerDTO>> getCustomerById(@PathVariable Long id, Locale locale) {
        CustomerDTO dto = customerService.getCustomerById(id);
        return ResponseEntity.ok(ApiResponse.success(dto, StatusCode.SUCCESS, messageSource, locale));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CustomerDTO>> createCustomer(@RequestBody @Valid CustomerDTO dto, Locale locale) {
        CustomerDTO created = customerService.createCustomer(dto);
        return new ResponseEntity<>(ApiResponse.success(created, StatusCode.CREATED_SUCCESS, messageSource, locale), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerDTO>> updateCustomer(@PathVariable Long id, @RequestBody @Valid CustomerDTO dto, Locale locale) {
        CustomerDTO updated = customerService.updateCustomer(id, dto);
        return ResponseEntity.ok(ApiResponse.success(updated, StatusCode.UPDATED_SUCCESS, messageSource, locale));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(@PathVariable Long id, Locale locale) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok(ApiResponse.success(StatusCode.DELETED_SUCCESS, messageSource, locale));
    }
}