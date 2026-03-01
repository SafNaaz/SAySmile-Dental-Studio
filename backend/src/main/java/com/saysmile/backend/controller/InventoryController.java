package com.saysmile.backend.controller;

import com.saysmile.backend.entity.InventoryItem;
import com.saysmile.backend.repository.InventoryItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryItemRepository inventoryRepository;

    @GetMapping
    public List<InventoryItem> getAllInventory() {
        return inventoryRepository.findAll();
    }
}
