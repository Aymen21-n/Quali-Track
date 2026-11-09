package com.qualitrack.production.controller;

import com.qualitrack.production.dto.QualityCheckDto;
import com.qualitrack.production.entite.ProductionLot;
import com.qualitrack.production.entite.QualityCheck;
import com.qualitrack.production.mapper.QualityCheckMapper;
import com.qualitrack.production.service.ProductionLotService;
import com.qualitrack.production.service.QualityCheckService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/quality-checks")
public class QualityCheckController {
    private final QualityCheckService service;
    private final QualityCheckMapper mapper;
    private final ProductionLotService productionLotService;

    public QualityCheckController(QualityCheckService service, QualityCheckMapper mapper, ProductionLotService productionLotService) {
        this.service = service;
        this.mapper = mapper;
        this.productionLotService = productionLotService;
    }

    @GetMapping
    public ResponseEntity<List<QualityCheckDto>> getAll() {
        return ResponseEntity.ok(service.getAll().stream().map(mapper::toDto).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<QualityCheckDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDto(service.getById(id)));
    }

    @PostMapping
    public ResponseEntity<QualityCheckDto> create(@Valid @RequestBody QualityCheckDto dto) {
        QualityCheck entity = mapper.toEntity(dto);
        entity.setId(null);
        ProductionLot lot = productionLotService.getById(dto.getLotId());
        entity.setLot(lot);

        QualityCheckDto created = mapper.toDto(service.save(entity));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QualityCheckDto> update(@PathVariable Long id, @Valid @RequestBody QualityCheckDto dto) {
        QualityCheck entity = mapper.toEntity(dto);
        ProductionLot lot = productionLotService.getById(dto.getLotId());
        entity.setLot(lot);
        return ResponseEntity.ok(mapper.toDto(service.modifier(id, entity)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}

