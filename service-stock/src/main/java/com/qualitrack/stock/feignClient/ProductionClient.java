package com.qualitrack.stock.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "service-production")
public interface ProductionClient {
    @GetMapping("/lots/{id}")
    ProductionLotRequest getLotById(@PathVariable("id") Long id);
}

