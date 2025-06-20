package com.app.claims.service;

import com.app.claims.dto.PropertyDTO;
import com.app.claims.entity.Property;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PropertyService {
    Property create(PropertyDTO request);
    List<Property> getAll();
    Property uploadImages(Long propertyId, List<MultipartFile> files) throws IOException;
}
