package com.saysmile.backend.controller;

import com.saysmile.backend.entity.Invoice;
import com.saysmile.backend.entity.User;
import com.saysmile.backend.entity.Role;
import com.saysmile.backend.repository.UserRepository;
import com.saysmile.backend.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final UserRepository userRepository;

    @GetMapping
    public List<Invoice> getAllInvoices(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow();

        if (user.getRole() == Role.PATIENT) {
            return invoiceService.getInvoicesByPatient(username);
        }

        return invoiceService.getAllInvoices();
    }

    @PostMapping
    public Invoice createInvoice(@RequestBody Invoice invoice) {
        return invoiceService.createInvoice(invoice);
    }
}
