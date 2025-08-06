package com.codegym.controller.api;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.request.ChangePasswordRequest;
import com.codegym.dto.response.CustomerDTO;
import com.codegym.service.CustomerService;
import com.codegym.utils.MessageUtil;
import com.codegym.utils.StatusCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin("*")
@RequiredArgsConstructor // Sử dụng Lombok để tự động tạo constructor và inject dependency
public class CustomerController {

    private final CustomerService customerService;
    private final MessageUtil messageUtil; // Inject MessageUtil

    @PutMapping("/{id}/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(@PathVariable Long id, @RequestBody @Valid ChangePasswordRequest request) {
        customerService.changePassword(id, request.getNewPassword());

        // Sử dụng StatusCode cụ thể và MessageUtil
        StatusCode status = StatusCode.PASSWORD_CHANGED;
        String message = messageUtil.getMessage(status.getMessageKey());
        ApiResponse<Void> response = new ApiResponse<>(status.getCode(), message);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<ApiResponse<CustomerDTO>> updateProfile(@PathVariable Long id, @RequestBody @Valid CustomerDTO dto) {
        CustomerDTO updated = customerService.updateProfile(id, dto);

        StatusCode status = StatusCode.PROFILE_UPDATED;
        String message = messageUtil.getMessage(status.getMessageKey());
        ApiResponse<CustomerDTO> response = new ApiResponse<>(status.getCode(), message, updated);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CustomerDTO>>> getAllCustomers() {
        List<CustomerDTO> customers = customerService.getAllCustomers();

        StatusCode status = StatusCode.GET_LIST_SUCCESS; // Tái sử dụng StatusCode chung
        String message = messageUtil.getMessage("customer.list.found"); // Hoặc lấy key trực tiếp
        ApiResponse<List<CustomerDTO>> response = new ApiResponse<>(status.getCode(), message, customers);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerDTO>> getCustomerById(@PathVariable Long id) {
        CustomerDTO dto = customerService.getCustomerById(id);

        String message = messageUtil.getMessage("customer.get.found");
        ApiResponse<CustomerDTO> response = new ApiResponse<>(StatusCode.SUCCESS.getCode(), message, dto);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CustomerDTO>> createCustomer(@RequestBody @Valid CustomerDTO dto) {
        CustomerDTO created = customerService.createCustomer(dto);

        StatusCode status = StatusCode.CREATED_SUCCESS; // Tái sử dụng
        String message = messageUtil.getMessage("customer.created");
        ApiResponse<CustomerDTO> response = new ApiResponse<>(status.getCode(), message, created);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerDTO>> updateCustomer(@PathVariable Long id, @RequestBody @Valid CustomerDTO dto) {
        CustomerDTO updated = customerService.updateCustomer(id, dto);

        StatusCode status = StatusCode.UPDATED_SUCCESS; // Tái sử dụng
        String message = messageUtil.getMessage("customer.updated");
        ApiResponse<CustomerDTO> response = new ApiResponse<>(status.getCode(), message, updated);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);

        StatusCode status = StatusCode.DELETED_SUCCESS; // Tái sử dụng
        String message = messageUtil.getMessage("customer.deleted");
        ApiResponse<Void> response = new ApiResponse<>(status.getCode(), message);

        return ResponseEntity.ok(response);
    }
}