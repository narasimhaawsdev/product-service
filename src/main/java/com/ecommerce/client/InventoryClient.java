package com.ecommerce.client;

import com.ecommerce.entity.Product;
import com.ecommerce.model.Inventory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@FeignClient(name = "inventory-service", url = "${inventory.service.url}")
public interface InventoryClient {
    @PostMapping("/update-stock")
    void createOrUpdateInventory(@RequestParam("productId") Long productId, @RequestParam("delta") Integer delta);
}

