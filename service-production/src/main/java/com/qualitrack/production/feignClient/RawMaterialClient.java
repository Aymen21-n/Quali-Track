package com.qualitrack.production.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "service-atelier")
public interface RawMaterialClient {
    @GetMapping("/matieres-premieres/{id}")
    RawMaterialDto getRawMaterialById(@PathVariable("id") Long id);

    @PatchMapping("/matieres-premieres/{id}/stock")
    RawMaterialDto decrementStock(@PathVariable("id") Long id, @RequestBody RawMaterialStockUpdateRequest request);
}

