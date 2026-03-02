package com.saysmile.backend.controller;

import com.saysmile.backend.service.AppointmentService;
import com.saysmile.backend.service.InvoiceService;
import com.saysmile.backend.repository.UserRepository;
import com.saysmile.backend.repository.PatientRepository;
import com.saysmile.backend.repository.InventoryItemRepository;
import com.saysmile.backend.entity.User;
import com.saysmile.backend.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AppointmentService appointmentService;
    private final InvoiceService invoiceService;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final InventoryItemRepository inventoryItemRepository;

    @GetMapping("/dashboard")
    public Map<String, Object> getDashboardData(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow();

        Map<String, Object> data = new HashMap<>();

        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0);
        LocalDateTime endOfMonth = LocalDateTime.now();

        if (user.getRole() == Role.PATIENT) {
            data.put("monthlyRevenue", 0.0);
            data.put("procedureTrends", Collections.emptyList());
            data.put("upcomingAppointments", appointmentService.getUpcomingAppointmentsByPatient(username,
                    LocalDateTime.now(), LocalDateTime.now().plusMonths(1)));
            return data;
        }

        // Staff/Admin see everything
        data.put("upcomingAppointments",
                appointmentService.getUpcomingAppointments(LocalDateTime.now(), LocalDateTime.now().plusMonths(1)));

        Double revenue = invoiceService.getTotalRevenue(startOfMonth, endOfMonth);
        data.put("monthlyRevenue", revenue != null ? revenue : 0.0);
        data.put("procedureTrends", appointmentService.getProcedureTrends());

        // Add additional stats
        data.put("totalPatients", patientRepository.count());
        data.put("lowStockCount", inventoryItemRepository.findLowStockItems().size());

        return data;
    }
}
