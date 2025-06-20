package com.app.claims.service.impl;

import com.app.claims.dto.PropertyDTO;
import com.app.claims.entity.Property;
import com.app.claims.repository.PropertyRepository;
import com.app.claims.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PropertyServiceImpl implements PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    @Value("${file.upload-dir}")
    public String uploadDir;

    @Override
    public Property create(PropertyDTO request) {
        Property property = Property.builder()
                .owner(request.getOwner())
                .description(request.getDescription())
                .imageUrls(new ArrayList<>())
                .build();
        return propertyRepository.save(property);
    }

    @Override
    public List<Property> getAll() {
        return propertyRepository.findAll();
    }

    @Override
    public Property uploadImages(Long propertyId, List<MultipartFile> files) throws IOException {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        if (files.size() > 200) throw new RuntimeException("Max 200 images allowed");

        List<String> urls = property.getImageUrls() != null ? property.getImageUrls() : new ArrayList<>();

        for (MultipartFile file : files) {
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String path = uploadDir + File.separator + filename;

            File dest = new File(path);
            dest.getParentFile().mkdirs(); // ensure directory exists
            file.transferTo(dest);

            urls.add("/uploads/" + filename);
        }

        property.setImageUrls(urls);
        return propertyRepository.save(property);
    }
}
