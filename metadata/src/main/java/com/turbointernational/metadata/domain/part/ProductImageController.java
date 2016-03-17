package com.turbointernational.metadata.domain.part;

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

@RequestMapping("/metadata/image")
@RestController
public class ProductImageController {

    private static final Logger log = LoggerFactory.getLogger(ProductImageController.class);
    
    @Value("${images.originals}")
    private File originalImagesDir;
    
    @Value("${images.resized}")
    private File resizedImagesDir;
    
    @Autowired(required=true)
    private ImageResizerService resizer;
    
    @Autowired
    private PartDao partDao;
    
    @Autowired
    private ProductImageDao productImageDao;
    
    @RequestMapping(value = "/{id}.jpg", method = RequestMethod.GET)
    @Secured("ROLE_READ")
    public ResponseEntity<byte[]> get(@PathVariable Long id) throws Exception {
        
        // Get the image
        ProductImage productImage = productImageDao.findOne(id);
        
        // Load it from disk
        File imageFile = new File(originalImagesDir, productImage.getFilename());
        
        try {
            
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "image/jpg");

            byte[] bytes = FileUtils.readFileToByteArray(imageFile);

            return new ResponseEntity(bytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            log.info("Couldn't load image file: " + imageFile.getAbsolutePath(), e);
            throw e;
        }
    }
    
    @RequestMapping(value = "/{size}/{id}.jpg", method = RequestMethod.GET)
    @Secured("ROLE_READ")
    public ResponseEntity<byte[]> getResized(@PathVariable int size, @PathVariable Long id) throws Exception {
        
        // Get the image
        ProductImage productImage = productImageDao.findOne(id);
        
        // Load it from disk
        File imageFile = new File(resizedImagesDir, productImage.getFilename(size));
        
        try {
            
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "image/jpg");

            byte[] bytes = FileUtils.readFileToByteArray(imageFile);

            return new ResponseEntity(bytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            log.info("Couldn't load image file: " + imageFile.getAbsolutePath(), e);
            throw e;
        }
    }
    
    @Transactional
    @RequestMapping(value="/{id}", method = RequestMethod.DELETE)
    @Secured("ROLE_PART_IMAGES")
    public ResponseEntity<Void> remove(@PathVariable Long id) throws Exception {
        
        // Look up the image
        ProductImage image = productImageDao.findOne(id);
        Part part = image.getPart();
        
        // Remove the image from the part
        part.getProductImages().remove(image);
        partDao.merge(part);
        
        // Remove the image
        productImageDao.remove(image);
        
        // Delete the files
        File original = new File(originalImagesDir, image.getFilename());
        FileUtils.deleteQuietly(original);
        
        for (int size : ImageResizerService.SIZES) {
            File resized = new File(resizedImagesDir, image.getFilename(size));
            FileUtils.deleteQuietly(resized);
        }
        
        return new ResponseEntity<>((Void) null, HttpStatus.OK);
    }
    
    
    

}
