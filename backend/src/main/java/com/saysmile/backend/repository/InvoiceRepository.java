package com.saysmile.backend.repository;

import com.saysmile.backend.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByPatientId(Long patientId);

    List<Invoice> findByPaymentStatus(String paymentStatus);

    // For revenue analytics
    List<Invoice> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT SUM(i.totalAmount) FROM Invoice i WHERE i.paymentStatus = 'PAID' AND i.createdAt BETWEEN :start AND :end")
    Double sumTotalRevenueBetween(LocalDateTime start, LocalDateTime end);
}
