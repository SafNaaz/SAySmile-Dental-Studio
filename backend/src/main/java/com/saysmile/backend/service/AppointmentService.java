package com.saysmile.backend.service;

import com.saysmile.backend.entity.Appointment;
import com.saysmile.backend.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
