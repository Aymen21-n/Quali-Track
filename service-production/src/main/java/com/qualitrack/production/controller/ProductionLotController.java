package com.qualitrack.production.controller;

import com.qualitrack.production.dto.ProductionLotCreateRequest;
import com.qualitrack.production.dto.ProductionLotDto;
import com.qualitrack.production.dto.ProductionLotDtoFull;
import com.qualitrack.production.dto.ProductionMaterialDto;
import com.qualitrack.production.dto.StatutLotUpdateDto;
import com.qualitrack.production.entite.ProductionLot;
import com.qualitrack.production.entite.ProductionMaterial;
import com.qualitrack.production.mapper.ProductionLotMapper;
import com.qualitrack.production.service.ProductionLotService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/lots")
@Validated
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

    @GetMapping("/{id}/detail")
    public ResponseEntity<ProductionLotDtoFull> getDetail(@PathVariable Long id) {
        return ResponseEntity.ok(service.getDetail(id));
    }

    @PostMapping("/new")
    public ResponseEntity<ProductionLotDto> createNew(
            @RequestHeader("idempotencyKey") @NotBlank @Size(max = 255) String idempotencyKey,
            @Valid @RequestBody ProductionLotCreateRequest request) {
        ProductionLot entity = new ProductionLot();
        entity.setMachineId(request.getMachineId());
        entity.setControleurId(request.getControleurId());

        List<ProductionMaterial> lignes = new ArrayList<>();
        if (request.getLignes() != null) {
            for (var ligneRequest : request.getLignes()) {
                ProductionMaterial ligne = new ProductionMaterial();
                ligne.setRawMaterialId(ligneRequest.getRawMaterialId());
                ligne.setQuantite(ligneRequest.getQuantite());
                lignes.add(ligne);
            }
        }
        entity.setLignes(lignes);

        ProductionLot created = service.createLot(entity, idempotencyKey);
        ProductionLotDto dto = mapper.toDto(created);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(dto);
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