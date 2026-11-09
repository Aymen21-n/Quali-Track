package com.qualitrack.atelier.controller;

import com.qualitrack.atelier.dto.MachineDto;
import com.qualitrack.atelier.entite.Machine;
import com.qualitrack.atelier.mapper.MachineMapper;
import com.qualitrack.atelier.service.MachineService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/machines")
public class MachineController {
    private final MachineService service;
    private final MachineMapper mapper;

    public MachineController(MachineService service, MachineMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<MachineDto>> getAll() {
        return ResponseEntity.ok(service.getAll().stream().map(mapper::toDto).toList());
    }
    @GetMapping("/{id}")
    public ResponseEntity<MachineDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDto(service.getById(id)));
    }
    @PostMapping
    public ResponseEntity<MachineDto> create(@Valid @RequestBody MachineDto dto) {
        Machine machine = mapper.toEntity(dto);
        machine.setId(null);
        MachineDto created = mapper.toDto(service.save(machine));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }
    @PatchMapping("/{id}/statut")
    public ResponseEntity<MachineDto> reserve(@PathVariable Long id, @RequestBody MachineDto dto) {
        return ResponseEntity.ok(mapper.toDto(service.reserveMachine(id, dto.getStatut() != null ? dto.getStatut().name() : null)));
    }
    @PutMapping("/{id}")
    public ResponseEntity<MachineDto> update(@PathVariable Long id, @Valid @RequestBody MachineDto dto) {
        return ResponseEntity.ok(mapper.toDto(service.modifier(id, mapper.toEntity(dto))));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}