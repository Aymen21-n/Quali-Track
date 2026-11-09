package com.qualitrack.production.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "service-utilisateurs")
public interface UtilisateursClient {
    @GetMapping("/utilisateurs/{id}")
    UserDto getUserById(@PathVariable("id") Long id);
}

