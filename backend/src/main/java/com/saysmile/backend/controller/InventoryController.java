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

    @PostMapping
    public InventoryItem createItem(@RequestBody InventoryItem item) {
        return inventoryRepository.save(item);
    }

    @PutMapping("/{id}")
    public InventoryItem updateItem(@PathVariable Long id, @RequestBody InventoryItem itemDetails) {
        InventoryItem item = inventoryRepository.findById(id).orElseThrow();
        item.setName(itemDetails.getName());
        item.setCategory(itemDetails.getCategory());
        item.setStockQuantity(itemDetails.getStockQuantity());
        item.setMinimumThreshold(itemDetails.getMinimumThreshold());
        item.setCostPerUnit(itemDetails.getCostPerUnit());
        return inventoryRepository.save(item);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id) {
        inventoryRepository.deleteById(id);
    }
}
