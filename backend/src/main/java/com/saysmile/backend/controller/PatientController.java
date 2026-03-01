package com.saysmile.backend.controller;

import com.saysmile.backend.entity.Patient;
import com.saysmile.backend.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PatientController {

    private final PatientRepository patientRepository;
    private final com.saysmile.backend.repository.UserRepository userRepository;

    @GetMapping
    public List<Patient> getAllPatients(org.springframework.security.core.Authentication authentication) {
        String username = authentication.getName();
        com.saysmile.backend.entity.User user = userRepository.findByUsername(username).orElseThrow();

        if (user.getRole() == com.saysmile.backend.entity.Role.PATIENT) {
            // Patients shouldn't see a list of other patients
            return List.of(patientRepository.findByUser_Username(username).orElseThrow());
        }

        return patientRepository.findAll();
    }

    @PostMapping
    public Patient createPatient(@RequestBody Patient patient) {
        return patientRepository.save(patient);
    }

    @PutMapping("/{id}")
    public Patient updatePatient(@PathVariable Long id, @RequestBody Patient patientDetails) {
        Patient patient = patientRepository.findById(id).orElseThrow();
        patient.setDateOfBirth(patientDetails.getDateOfBirth());
        patient.setGender(patientDetails.getGender());
        patient.setPrimaryDentalIssues(patientDetails.getPrimaryDentalIssues());
        patient.setMedicalHistory(patientDetails.getMedicalHistory());
        return patientRepository.save(patient);
    }

    @DeleteMapping("/{id}")
    public void deletePatient(@PathVariable Long id) {
        patientRepository.deleteById(id);
    }
}
