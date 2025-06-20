package com.app.claims.controller;

import com.app.claims.dto.PropertyDTO;
import com.app.claims.service.PropertyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor
public class PropertyController {

    @Autowired
    private PropertyService propertyService;


    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid PropertyDTO request) {
        return ResponseEntity.ok(propertyService.create(request));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    @PostMapping("/{id}/upload")
    public ResponseEntity<?> uploadImages(@PathVariable Long id,
                                                 @RequestParam("files") List<MultipartFile> files) throws IOException {
        return ResponseEntity.ok(propertyService.uploadImages(id, files));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT', 'CUSTOMER')")
    @GetMapping
    public ResponseEntity<List<?>> getAll() {
        return ResponseEntity.ok(propertyService.getAll());
    }
}