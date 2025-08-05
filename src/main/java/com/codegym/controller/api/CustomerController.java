package com.codegym.controller.api;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.request.ChangePasswordRequest;
import com.codegym.dto.response.CustomerDTO;
import com.codegym.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin("*")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PutMapping("/{id}/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(@PathVariable Long id, @RequestBody @Valid ChangePasswordRequest request) {
        customerService.changePassword(id, request.getNewPassword());
        return ResponseEntity.ok(new ApiResponse<>(200, "Đổi mật khẩu thành công", null));
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<ApiResponse<CustomerDTO>> updateProfile(@PathVariable Long id, @RequestBody @Valid CustomerDTO dto) {
        CustomerDTO updated = customerService.updateProfile(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(200, "Cập nhật profile thành công", updated));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CustomerDTO>>> getAllCustomers() {
        List<CustomerDTO> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(new ApiResponse<>(200, "Lấy danh sách khách hàng thành công", customers));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerDTO>> getCustomerById(@PathVariable Long id) {
        CustomerDTO dto = customerService.getCustomerById(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Lấy thông tin khách hàng thành công", dto));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CustomerDTO>> createCustomer(@RequestBody @Valid CustomerDTO dto) {
        CustomerDTO created = customerService.createCustomer(dto);
        return new ResponseEntity<>(new ApiResponse<>(201, "Tạo khách hàng mới thành công", created), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerDTO>> updateCustomer(@PathVariable Long id, @RequestBody @Valid CustomerDTO dto) {
        CustomerDTO updated = customerService.updateCustomer(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(200, "Cập nhật khách hàng thành công", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Xóa khách hàng thành công", null));
    }
}