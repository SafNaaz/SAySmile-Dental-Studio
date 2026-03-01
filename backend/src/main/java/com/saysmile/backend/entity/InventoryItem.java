package com.saysmile.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@Entity
@Table(name = "inventory_items")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String category; // e.g., Composites, Instruments, PPE, Pharmaceuticals

    private Integer stockQuantity;
    private Integer minimumThreshold; // Send alert if stock falls below this

    private Double costPerUnit;
}
