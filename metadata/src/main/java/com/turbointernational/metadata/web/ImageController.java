package com.turbointernational.metadata.web;

import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.part.PartDao;
import com.turbointernational.metadata.domain.part.ProductImage;
import com.turbointernational.metadata.domain.part.ProductImageDao;
import com.turbointernational.metadata.services.ImageResizerService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RequestMapping("/metadata/image")
@RestController
public class ImageController {

    private static final Logger log = LoggerFactory.getLogger(ImageController.class);
    
    @Value("${images.originals}")
    private File originalImagesDir;
    
    @Value("${images.resized}")
    private File resizedImagesDir;
    
    @Autowired
    private ImageResizerService resizer;
    
    @Autowired
    private PartDao partDao;
    
    @Autowired
    private ProductImageDao productImageDao;
    @RequestMapping(value = "/{id}.jpg", method = GET)
    @Secured("ROLE_READ")
    public ResponseEntity<byte[]> getPartImage(@PathVariable Long id) throws Exception {
        ProductImage productImage = productImageDao.findOne(id);
        String filename = productImage.getFilename();
        return getImage(originalImagesDir, filename);
    }

    @RequestMapping(value = "/{size}/{id}.jpg", method = GET)
    @Secured("ROLE_READ")
    public ResponseEntity<byte[]> getResizedPartImage(@PathVariable int size, @PathVariable Long id) throws Exception {
        ProductImage productImage = productImageDao.findOne(id);
        String filename = productImage.getFilename(size);
        return getImage(resizedImagesDir, filename);
    }

    @RequestMapping(value = "/{id}/cdlegend.jpg", method = GET)
    @Secured("ROLE_READ")
    public ResponseEntity<byte[]> getPartCritDimsLegendImage(@PathVariable Long partId) throws Exception {
        Part part = partDao.findOne(partId);
        String filename = part.getLegendImgFilename();
        return getImage(originalImagesDir, filename);
    }

    @Transactional
    @RequestMapping(value="/{id}", method = DELETE)
    @Secured("ROLE_PART_IMAGES")
    public ResponseEntity<Void> removePartImage(@PathVariable Long id) throws Exception {
        // Look up the image
        ProductImage image = productImageDao.findOne(id);
        Part part = image.getPart();
        // Remove the image from the part
        part.getProductImages().remove(image);
        partDao.merge(part);
        // Remove the image
        productImageDao.remove(image);
        // Delete the files
        delImage(originalImagesDir, image.getFilename());
        for (int size : ImageResizerService.SIZES) {
            delImage(resizedImagesDir, image.getFilename(size));
        }
        return new ResponseEntity<>((Void) null, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value="/{id}/cdlegend.jpg", method = DELETE)
    @Secured("ROLE_PART_IMAGES")
    public ResponseEntity<Void> removePartCritDimsLegendImage(@PathVariable Long id) throws Exception {
        Part part = partDao.findOne(id);
        delImage(resizedImagesDir, part.getLegendImgFilename());
        return new ResponseEntity<>((Void) null, HttpStatus.OK);
    }

    private ResponseEntity<byte[]> getImage(File dir, String filename) throws IOException {
        File imageFile = new File(dir, filename);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "image/jpg");
        byte[] bytes = FileUtils.readFileToByteArray(imageFile);
        return new ResponseEntity(bytes, headers, HttpStatus.OK);
    }

    private void delImage(File dir, String filename) throws IOException {
        File original = new File(dir, filename);
        FileUtils.deleteQuietly(original);
    }

}
