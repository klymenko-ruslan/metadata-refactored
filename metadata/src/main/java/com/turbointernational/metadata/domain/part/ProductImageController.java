package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.images.ImageResizer;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/metadata/image")
@Controller
public class ProductImageController {

    private static final Logger log = Logger.getLogger(ProductImageController.class.toString());
    
    @Value("${images.originals}")
    private File originalImagesDir;
    
    @Value("${images.resized}")
    private File resizedImagesDir;
    
    private static final int[] sizes = {50, 135, 1000};
    
    @RequestMapping(value = "/{id}.jpg", method = RequestMethod.GET)
    @ResponseBody
    @Secured("ROLE_READ")
    public ResponseEntity<byte[]> get(@PathVariable Long id) throws Exception {
        
        // Get the image
        ProductImage productImage = ProductImage.find(id);
        
        // Load it from disk
        File imageFile = new File(originalImagesDir, productImage.getFilename());
        
        try {
            
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "image/jpg");

            byte[] bytes = FileUtils.readFileToByteArray(imageFile);

            return new ResponseEntity(bytes, headers, HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            log.log(Level.INFO, "Couldn't load image file: " + imageFile.getAbsolutePath(), e);
            throw e;
        }
    }
    
    @RequestMapping(value = "/{size}/{id}.jpg", method = RequestMethod.GET)
    @ResponseBody
    @Secured("ROLE_READ")
    public ResponseEntity<byte[]> getResized(@PathVariable int size, @PathVariable Long id) throws Exception {
        
        // Get the image
        ProductImage productImage = ProductImage.find(id);
        
        // Load it from disk
        File imageFile = new File(resizedImagesDir, productImage.getFilename(size));
        
        try {
            
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "image/jpg");

            byte[] bytes = FileUtils.readFileToByteArray(imageFile);

            return new ResponseEntity(bytes, headers, HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            log.log(Level.INFO, "Couldn't load image file: " + imageFile.getAbsolutePath(), e);
            throw e;
        }
    }
    
    @Transactional
    @RequestMapping(value="/{id}/addProductImage", method = RequestMethod.POST)
    @ResponseBody
    @Secured("ROLE_ADD_IMAGE")
    public ResponseEntity<Void> addProductImage(@PathVariable Long id,
                                                @RequestParam("file") MultipartFile upload) throws Exception {
        
        // Look up the part
        Part part = Part.findPart(id);
        
        // Save the file into the originals directory
        File originalFile = new File(originalImagesDir, upload.getOriginalFilename());
        
        if (originalFile.exists()) {
            throw new IOException("Image already exists: " + upload.getOriginalFilename());
        }
        
        FileUtils.writeByteArrayToFile(originalFile, upload.getBytes());
        
        // Create the product image
        ProductImage productImage = new ProductImage();
        productImage.setFilename(upload.getOriginalFilename());
        productImage.setPart(part);
        
        part.getProductImages().add(productImage);

        // Generate the resized images
        ImageResizer resizer = new ImageResizer();
        for (int size : sizes) {
            resizer.generateResizedImage(productImage, size);
        }
        
        // Save it
        productImage.persist();
        part.merge();
        
        return new ResponseEntity<Void>((Void) null, HttpStatus.OK);
    }
    
    
    

}
