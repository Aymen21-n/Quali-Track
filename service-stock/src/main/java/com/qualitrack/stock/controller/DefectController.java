package com.qualitrack.stock.controller;

import com.qualitrack.stock.dto.DefectDto;
import com.qualitrack.stock.entity.Defect;
import com.qualitrack.stock.mapper.DefectMapper;
import com.qualitrack.stock.service.DefectService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/defects")
public class DefectController {
    private final DefectService service;
    private final DefectMapper mapper;

    public DefectController(DefectService service, DefectMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<DefectDto>> getAll() {
        return ResponseEntity.ok(service.getAll().stream().map(mapper::toDto).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DefectDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDto(service.getById(id)));
    }

    @PostMapping
    public ResponseEntity<DefectDto> create(@Valid @RequestBody DefectDto dto) {
        Defect entity = mapper.toEntity(dto);
        entity.setId(null);
        DefectDto created = mapper.toDto(service.save(entity));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DefectDto> update(@PathVariable Long id, @Valid @RequestBody DefectDto dto) {
        return ResponseEntity.ok(mapper.toDto(service.modifier(id, mapper.toEntity(dto))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}

