package com.qualitrack.atelier.controller;

import com.qualitrack.atelier.dto.RawMaterialDto;
import com.qualitrack.atelier.entite.RawMaterial;
import com.qualitrack.atelier.mapper.RawMaterialMapper;
import com.qualitrack.atelier.service.RawMaterialService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/matieres-premieres")
public class RawMaterialController {
    private final RawMaterialService service;
    private final RawMaterialMapper mapper;

    public RawMaterialController(RawMaterialService service, RawMaterialMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<RawMaterialDto>> getAll() {
        return ResponseEntity.ok(service.getAll().stream().map(mapper::toDto).toList());
    }
    @GetMapping("/{id}")
    public ResponseEntity<RawMaterialDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDto(service.getById(id)));
    }
    @PostMapping
    public ResponseEntity<RawMaterialDto> create(@Valid @RequestBody RawMaterialDto dto) {
        RawMaterial material = mapper.toEntity(dto);
        material.setId(null);
        RawMaterialDto created = mapper.toDto(service.save(material));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }
    @PutMapping("/{id}")
    public ResponseEntity<RawMaterialDto> update(@PathVariable Long id, @Valid @RequestBody RawMaterialDto dto) {
        return ResponseEntity.ok(mapper.toDto(service.modifier(id, mapper.toEntity(dto))));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}
