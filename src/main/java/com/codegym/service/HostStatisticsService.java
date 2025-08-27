package com.codegym.service;

import com.codegym.dto.response.HostStatisticsDTO;

public interface HostStatisticsService {
    
    /**
     * Lấy thống kê chi tiết cho host theo kỳ thời gian
     * @param hostId ID của host
     * @param period Kỳ thời gian (current_month, last_month, last_3_months, last_6_months, current_year)
     * @return HostStatisticsDTO chứa thông tin thống kê
     */
    HostStatisticsDTO getHostStatistics(Long hostId, String period);
    
    /**
     * Lấy thống kê cho host hiện tại đang đăng nhập
     * @param period Kỳ thời gian
     * @return HostStatisticsDTO chứa thông tin thống kê
     */
    HostStatisticsDTO getCurrentHostStatistics(String period);
}
