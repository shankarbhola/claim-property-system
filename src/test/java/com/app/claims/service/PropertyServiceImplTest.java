package com.app.claims.service;

import com.app.claims.dto.PropertyDTO;
import com.app.claims.entity.Property;
import com.app.claims.repository.PropertyRepository;
import com.app.claims.service.impl.PropertyServiceImpl;

import org.junit.*;
import org.junit.rules.TemporaryFolder;
import org.mockito.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PropertyServiceImplTest {

    @Mock
    private PropertyRepository propertyRepository;

    @InjectMocks
    private PropertyServiceImpl propertyService;

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private Property property;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        property = Property.builder()
                .id(1L)
                .owner("Rajesh Sharma")
                .description("Nice 3BHK")
                .imageUrls(new ArrayList<>())
                .build();

        propertyService.uploadDir = tempFolder.getRoot().getAbsolutePath();
    }

    @Test
    public void testCreateProperty() {
        PropertyDTO dto = new PropertyDTO("Rajesh Sharma", "Nice 3BHK");

        when(propertyRepository.save(any(Property.class))).thenReturn(property);

        Property saved = propertyService.create(dto);

        assertEquals("Rajesh Sharma", saved.getOwner());
    }

    @Test
    public void testGetAllProperties() {
        when(propertyRepository.findAll()).thenReturn(Collections.singletonList(property));

        List<Property> list = propertyService.getAll();

        assertEquals(1, list.size());
    }

    @Test
    public void testUploadImages_Success() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("image1.jpg");
        doAnswer(invocation -> {
            File dest = invocation.getArgument(0);
            dest.createNewFile();
            return null;
        }).when(file).transferTo(any(File.class));

        List<MultipartFile> files = Collections.singletonList(file);

        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));
        when(propertyRepository.save(any(Property.class))).thenReturn(property);

        Property updated = propertyService.uploadImages(1L, files);

        assertEquals(1, updated.getImageUrls().size());
        assertTrue(updated.getImageUrls().get(0).startsWith("/uploads/"));
    }

    @Test(expected = RuntimeException.class)
    public void testUploadImages_PropertyNotFound() throws IOException {
        when(propertyRepository.findById(99L)).thenReturn(Optional.empty());

        propertyService.uploadImages(99L, new ArrayList<>());
    }

    @Test(expected = RuntimeException.class)
    public void testUploadImages_ExceedsLimit() throws IOException {
        List<MultipartFile> files = new ArrayList<>();
        for (int i = 0; i < 201; i++) {
            MultipartFile file = mock(MultipartFile.class);
            when(file.getOriginalFilename()).thenReturn("image" + i + ".jpg");
            files.add(file);
        }

        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));

        propertyService.uploadImages(1L, files);
    }
}
