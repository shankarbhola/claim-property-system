package com.app.claims.controller;

import com.app.claims.dto.PropertyDTO;
import com.app.claims.entity.Property;
import com.app.claims.service.PropertyService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PropertyControllerTest {

    @Mock
    private PropertyService propertyService;

    @InjectMocks
    private PropertyController propertyController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateProperty() {
        PropertyDTO request = new PropertyDTO();
        request.setOwner("Shankar");
        request.setDescription("3BHK near city");

        when(propertyService.create(request)).thenReturn(new Property());

        ResponseEntity<?> response = propertyController.create(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(propertyService).create(request);
    }

    @Test
    public void testUploadImages() throws IOException {
        Long propertyId = 1L;
        MultipartFile file = new MockMultipartFile("file", "img.jpg", "image/jpeg", new byte[10]);
        List<MultipartFile> files = Collections.singletonList(file);

        when(propertyService.uploadImages(propertyId, files)).thenReturn(new Property());

        ResponseEntity<?> response = propertyController.uploadImages(propertyId, files);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(propertyService).uploadImages(propertyId, files);
    }

    @Test
    public void testGetAllProperties() {
        List<Property> mockList = Collections.singletonList(new Property());

        when(propertyService.getAll()).thenReturn(mockList);

        ResponseEntity<List<?>> response = propertyController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockList, response.getBody());
        verify(propertyService).getAll();
    }
}
