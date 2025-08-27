package com.codegym.controller.admin;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.request.LoginRequest;
import com.codegym.dto.response.LoginResponse;
import com.codegym.service.AuthService;
import com.codegym.service.HouseService;
import com.codegym.service.RentalService;
import com.codegym.service.UserService;
import com.codegym.utils.StatusCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/admin")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AdminController {

    private final AuthService authService;
    private final UserService userService;
    private final HouseService houseService;
    private final RentalService rentalService;
    private final MessageSource messageSource;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> adminLogin(
            @Valid @RequestBody LoginRequest request,
            Locale locale
    ) {
        LoginResponse loginResponse = authService.adminLogin(request);

        return ResponseEntity.ok(
                ApiResponse.success(loginResponse, StatusCode.LOGIN_SUCCESS, messageSource, locale)
        );
    }

    @GetMapping("/dashboard/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStats(Locale locale) {
        Map<String, Object> stats = new HashMap<>();
        
        // Thống kê người dùng
        long totalUsers = userService.countAllUsers();
        long totalHosts = userService.countHosts();
        long totalAdmins = userService.countAdmins();
        
        // Thống kê nhà
        long totalHouses = houseService.countAllHouses();
        long availableHouses = houseService.countHousesByStatus("AVAILABLE");
        long rentedHouses = houseService.countHousesByStatus("RENTED");
        long inactiveHouses = houseService.countHousesByStatus("INACTIVE");
        
        // Thống kê thuê nhà
        long totalRentals = rentalService.countAllRentals();
        long pendingRentals = rentalService.countRentalsByStatus("PENDING");
        long activeRentals = rentalService.countRentalsByStatus("SCHEDULED");
        long completedRentals = rentalService.countRentalsByStatus("CHECKED_OUT");
        
        // Doanh thu
        double totalRevenue = rentalService.calculateTotalRevenue();
        double monthlyRevenue = rentalService.calculateMonthlyRevenue();
        
        stats.put("users", Map.of(
            "total", totalUsers,
            "hosts", totalHosts,
            "admins", totalAdmins
        ));
        
        stats.put("houses", Map.of(
            "total", totalHouses,
            "available", availableHouses,
            "rented", rentedHouses,
            "inactive", inactiveHouses
        ));
        
        stats.put("rentals", Map.of(
            "total", totalRentals,
            "pending", pendingRentals,
            "active", activeRentals,
            "completed", completedRentals
        ));
        
        stats.put("revenue", Map.of(
            "total", totalRevenue,
            "monthly", monthlyRevenue
        ));
        
        return ResponseEntity.ok(ApiResponse.success(stats, StatusCode.SUCCESS, messageSource, locale));
    }

    @GetMapping("/dashboard/recent-houses")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getRecentHouses(Locale locale) {
        List<Map<String, Object>> recentHouses = houseService.getRecentHousesForDashboard(5);
        return ResponseEntity.ok(ApiResponse.success(recentHouses, StatusCode.SUCCESS, messageSource, locale));
    }

    @GetMapping("/dashboard/recent-rentals")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getRecentRentals(Locale locale) {
        List<Map<String, Object>> recentRentals = rentalService.getRecentRentalsForDashboard(5);
        return ResponseEntity.ok(ApiResponse.success(recentRentals, StatusCode.SUCCESS, messageSource, locale));
    }
}
