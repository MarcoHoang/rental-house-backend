package com.codegym.controller.api;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.response.CustomerDTO;
import com.codegym.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin("*")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // Lấy tất cả khách hàng
    @GetMapping
    public ResponseEntity<ApiResponse<List<CustomerDTO>>> getAllCustomers() {
        List<CustomerDTO> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách khách hàng thành công", customers));
    }

    // Lấy khách hàng theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerDTO>> getCustomerById(@PathVariable Long id) {
        CustomerDTO dto = customerService.getCustomerById(id);
        if (dto == null) {
            return ResponseEntity.ok(ApiResponse.error("404", "Không tìm thấy khách hàng với ID = " + id));
        }
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin khách hàng thành công", dto));
    }

    // Tạo mới khách hàng
    @PostMapping
    public ResponseEntity<ApiResponse<CustomerDTO>> createCustomer(@RequestBody CustomerDTO dto) {
        CustomerDTO created = customerService.createCustomer(dto);
        return ResponseEntity.ok(ApiResponse.success("Tạo khách hàng mới thành công", created));
    }

    // Cập nhật thông tin khách hàng
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerDTO>> updateCustomer(@PathVariable Long id, @RequestBody CustomerDTO dto) {
        CustomerDTO updated = customerService.updateCustomer(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật khách hàng thành công", updated));
    }

    // Xóa khách hàng
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa khách hàng thành công", null));
    }
}
