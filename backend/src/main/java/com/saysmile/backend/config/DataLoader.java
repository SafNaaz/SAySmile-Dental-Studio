package com.saysmile.backend.config;

import com.saysmile.backend.entity.*;
import com.saysmile.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final InvoiceRepository invoiceRepository;
    private final InventoryItemRepository inventoryItemRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            System.out.println("No data found - Seeding Dummy Data...");

            // Create Doctor (Dr. Aysha)
            User doctor = userRepository.save(User.builder().username("aysha@saysmile.com").password("pass")
                    .role(Role.DOCTOR).fullName("Dr. Aysha").email("aysha@saysmile.com").build());

            // Create Patients
            User pUser1 = userRepository.save(User.builder().username("9876543210").password("pass").role(Role.PATIENT)
                    .fullName("John Doe").phone("9876543210").build());
            User pUser2 = userRepository.save(User.builder().username("9123456789").password("pass").role(Role.PATIENT)
                    .fullName("Jane Smith").phone("9123456789").build());
            User pUser3 = userRepository.save(User.builder().username("9988776655").password("pass").role(Role.PATIENT)
                    .fullName("Jim Halpert").phone("9988776655").build());

            Patient patient1 = patientRepository
                    .save(Patient.builder().user(pUser1).gender("Male").primaryDentalIssues("Root Canal").build());
            Patient patient2 = patientRepository.save(
                    Patient.builder().user(pUser2).gender("Female").primaryDentalIssues("Scaling & Polishing").build());
            Patient patient3 = patientRepository
                    .save(Patient.builder().user(pUser3).gender("Male").primaryDentalIssues("Root Canal").build());

            // Create Appointments
            Appointment appt1 = appointmentRepository.save(Appointment.builder().patient(patient1).doctor(doctor)
                    .appointmentTime(LocalDateTime.now().minusDays(2)).status("COMPLETED").procedureType("Root Canal")
                    .build());
            Appointment appt2 = appointmentRepository.save(Appointment.builder().patient(patient2).doctor(doctor)
                    .appointmentTime(LocalDateTime.now().minusDays(1)).status("COMPLETED")
                    .procedureType("Scaling & Polishing").build());
            Appointment appt3 = appointmentRepository.save(Appointment.builder().patient(patient3).doctor(doctor)
                    .appointmentTime(LocalDateTime.now().plusHours(2)).status("SCHEDULED").procedureType("Root Canal")
                    .build());

            // Create Invoices (Revenue)
            invoiceRepository.save(Invoice.builder().patient(patient1).appointment(appt1).subTotal(4500.0).cgst(405.0)
                    .sgst(405.0).totalAmount(5310.0).paymentStatus("PAID").build());
            invoiceRepository.save(Invoice.builder().patient(patient2).appointment(appt2).subTotal(1500.0).cgst(135.0)
                    .sgst(135.0).totalAmount(1770.0).paymentStatus("PAID").build());

            // Create Inventory items (Triggering alerts)
            inventoryItemRepository.save(InventoryItem.builder().name("Latex Gloves M").category("PPE").stockQuantity(5)
                    .minimumThreshold(20).costPerUnit(150.0).build());
            inventoryItemRepository.save(InventoryItem.builder().name("Composite Resin A2").category("Composites")
                    .stockQuantity(2).minimumThreshold(10).costPerUnit(800.0).build());
            inventoryItemRepository.save(InventoryItem.builder().name("Lidocaine 2%").category("Pharmaceuticals")
                    .stockQuantity(1).minimumThreshold(15).costPerUnit(300.0).build());
            inventoryItemRepository.save(InventoryItem.builder().name("Cotton Rolls").category("Consumables")
                    .stockQuantity(200).minimumThreshold(50).costPerUnit(10.0).build());

            System.out.println("Dummy Data Seeding Completed!");
        }
    }
}
