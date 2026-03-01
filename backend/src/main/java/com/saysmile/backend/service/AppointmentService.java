package com.saysmile.backend.service;

import com.saysmile.backend.entity.Appointment;
import com.saysmile.backend.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public List<Appointment> getAppointmentsByPatient(String username) {
        return appointmentRepository.findByPatient_User_Username(username);
    }

    public Appointment createAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    public List<Object[]> getProcedureTrends() {
        return appointmentRepository.countAppointmentsByProcedureType();
    }

    public Appointment updateAppointment(Long id, Appointment appointmentDetails) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appointment.setAppointmentTime(appointmentDetails.getAppointmentTime());
        appointment.setProcedureType(appointmentDetails.getProcedureType());
        appointment.setStatus(appointmentDetails.getStatus());
        appointment.setNotes(appointmentDetails.getNotes());

        return appointmentRepository.save(appointment);
    }

    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }

    public List<Appointment> getUpcomingAppointments(LocalDateTime start, LocalDateTime end) {
        return appointmentRepository.findByAppointmentTimeBetween(start, end);
    }

    public List<Appointment> getUpcomingAppointmentsByPatient(String username, LocalDateTime start, LocalDateTime end) {
        return appointmentRepository.findByPatient_User_UsernameAndAppointmentTimeBetween(username, start, end);
    }
}
