package com.saysmile.backend.controller;

import com.saysmile.backend.entity.Appointment;
import com.saysmile.backend.entity.User;
import com.saysmile.backend.entity.Role;
import com.saysmile.backend.entity.Patient;
import com.saysmile.backend.repository.UserRepository;
import com.saysmile.backend.repository.PatientRepository;
import com.saysmile.backend.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;

    @GetMapping
    public List<Appointment> getAllAppointments(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow();

        if (user.getRole() == Role.PATIENT) {
            return appointmentService.getAppointmentsByPatient(username);
        }

        return appointmentService.getAllAppointments();
    }

    @PostMapping
    public ResponseEntity<Appointment> bookAppointment(@RequestBody Appointment appointment,
            Authentication authentication) {
        String username = authentication.getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 1. Set Patient
        if (currentUser.getRole() == Role.PATIENT) {
            // Self-booking: override to current logged-in patient
            Patient patient = patientRepository.findByUser_Username(username)
                    .orElseThrow(() -> new RuntimeException("Patient profile not found"));
            appointment.setPatient(patient);
        } else {
            // Staff booking: validate if provided patient is valid
            if (appointment.getPatient() != null && appointment.getPatient().getId() != null) {
                // Fetch the actual patient from DB to ensure it's valid
                Patient patient = patientRepository.findById(appointment.getPatient().getId())
                        .orElseThrow(() -> new RuntimeException("Provided patient not found"));
                appointment.setPatient(patient);
            } else {
                return ResponseEntity.badRequest().build();
            }
        }

        // 2. Set Doctor (Default to ID 1 or current user if they are a doctor)
        if (appointment.getDoctor() == null || appointment.getDoctor().getId() == null) {
            if (currentUser.getRole() == Role.DOCTOR) {
                appointment.setDoctor(currentUser);
            } else {
                User defaultDoctor = userRepository.findById(1L)
                        .orElseThrow(() -> new RuntimeException("Default doctor with ID 1 not found"));
                appointment.setDoctor(defaultDoctor);
            }
        } else {
            // Validate provided doctor
            User doctor = userRepository.findById(appointment.getDoctor().getId())
                    .orElseThrow(() -> new RuntimeException("Selected doctor not found"));
            appointment.setDoctor(doctor);
        }

        // 3. Set Default Status
        if (appointment.getStatus() == null) {
            appointment.setStatus("SCHEDULED");
        }

        Appointment createdAppointment = appointmentService.createAppointment(appointment);
        return ResponseEntity.ok(createdAppointment);
    }

    @PutMapping("/{id}")
    public Appointment updateAppointment(@PathVariable Long id, @RequestBody Appointment appointment) {
        return appointmentService.updateAppointment(id, appointment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long id, Authentication authentication) {
        // Patients can only delete their own appointments (maybe restrict to
        // cancellation instead?)
        // For now, let's just allow it or check ownership
        appointmentService.deleteAppointment(id);
        return ResponseEntity.ok().build();
    }
}
