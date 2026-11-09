package com.qualitrack.production.controller;

import com.qualitrack.production.dto.ProductionLotDto;
import com.qualitrack.production.dto.StatutLotUpdateDto;
import com.qualitrack.production.entite.ProductionLot;
import com.qualitrack.production.mapper.ProductionLotMapper;
import com.qualitrack.production.service.ProductionLotService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lots")
public class ProductionLotController {
    private final ProductionLotService service;
    private final ProductionLotMapper mapper;

    public ProductionLotController(ProductionLotService service, ProductionLotMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<ProductionLotDto>> getAll() {
        return ResponseEntity.ok(service.getAll().stream().map(mapper::toDto).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductionLotDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDto(service.getById(id)));
    }

    @PostMapping
    public ResponseEntity<ProductionLotDto> create(@Valid @RequestBody ProductionLotDto dto) {
        ProductionLot entity = mapper.toEntity(dto);
        entity.setId(null);
        ProductionLotDto created = mapper.toDto(service.save(entity));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductionLotDto> update(@PathVariable Long id, @Valid @RequestBody ProductionLotDto dto) {
        return ResponseEntity.ok(mapper.toDto(service.modifier(id, mapper.toEntity(dto))));
    }

    @PatchMapping("/{id}/statut")
    public ResponseEntity<ProductionLotDto> updateStatus(@PathVariable Long id,
                                                       @Valid @RequestBody StatutLotUpdateDto dto) {
        return ResponseEntity.ok(mapper.toDto(service.changerStatut(id, dto.getStatut())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}

