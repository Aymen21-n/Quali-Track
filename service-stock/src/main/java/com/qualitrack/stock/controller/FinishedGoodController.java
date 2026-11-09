package com.qualitrack.stock.controller;

import com.qualitrack.stock.dto.FinishedGoodDetailDto;
import com.qualitrack.stock.dto.FinishedGoodDto;
import com.qualitrack.stock.entity.FinishedGood;
import com.qualitrack.stock.mapper.FinishedGoodMapper;
import com.qualitrack.stock.service.FinishedGoodService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/finished-goods")
public class FinishedGoodController {
    private final FinishedGoodService service;
    private final FinishedGoodMapper mapper;

    public FinishedGoodController(FinishedGoodService service, FinishedGoodMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<FinishedGoodDto>> getAll() {
        return ResponseEntity.ok(service.getAll().stream().map(mapper::toDto).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FinishedGoodDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDto(service.getById(id)));
    }

    @GetMapping("/{id}/detail")
    public ResponseEntity<FinishedGoodDetailDto> getDetail(@PathVariable Long id) {
        return ResponseEntity.ok(service.getDetail(id));
    }

    @PostMapping
    public ResponseEntity<FinishedGoodDto> create(@Valid @RequestBody FinishedGoodDto dto) {
        FinishedGood entity = mapper.toEntity(dto);
        entity.setId(null);
        FinishedGoodDto created = mapper.toDto(service.save(entity));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FinishedGoodDto> update(@PathVariable Long id, @Valid @RequestBody FinishedGoodDto dto) {
        return ResponseEntity.ok(mapper.toDto(service.modifier(id, mapper.toEntity(dto))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}
