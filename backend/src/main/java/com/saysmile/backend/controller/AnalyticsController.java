package com.saysmile.backend.controller;

import com.saysmile.backend.service.AppointmentService;
import com.saysmile.backend.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "*") // Allow Angular to connect
@RequiredArgsConstructor
public class AnalyticsController {

    private final AppointmentService appointmentService;
    private final InvoiceService invoiceService;

    @GetMapping("/dashboard")
    public Map<String, Object> getDashboardData() {
        Map<String, Object> data = new HashMap<>();

        // Calculate for the current month roughly
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0);
        LocalDateTime endOfMonth = LocalDateTime.now();

        // 1. Revenue
        Double revenue = invoiceService.getTotalRevenue(startOfMonth, endOfMonth);
        data.put("monthlyRevenue", revenue != null ? revenue : 0.0);

        // 2. Procedure Trends (Disease tracking)
        data.put("procedureTrends", appointmentService.getProcedureTrends());

        // We can add Patient Flow, Employee Attendance, etc. here
        return data;
    }
}
