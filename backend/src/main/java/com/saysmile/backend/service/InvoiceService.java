package com.saysmile.backend.service;

import com.saysmile.backend.entity.Invoice;
import com.saysmile.backend.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public Double getTotalRevenue(LocalDateTime start, LocalDateTime end) {
        return invoiceRepository.sumTotalRevenueBetween(start, end);
    }
}
