package com.turbointernational.metadata.domain.part;
import com.turbointernational.metadata.util.ImageResizer;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/metadata/image")
@Controller
public class ProductImageController {

    private static final Logger log = Logger.getLogger(ProductImageController.class.toString());
    
    @Value("${images.originals}")
    private File originalImagesDir;
    
    @Value("${images.resized}")
    private File resizedImagesDir;
    
    @Autowired(required=true)
    ImageResizer resizer;
    
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

            return new ResponseEntity(bytes, headers, HttpStatus.OK);
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

            return new ResponseEntity(bytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            log.log(Level.INFO, "Couldn't load image file: " + imageFile.getAbsolutePath(), e);
            throw e;
        }
    }
    
    @Transactional
    @RequestMapping(value="/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @Secured("ROLE_PART_IMAGES")
    public ResponseEntity<Void> remove(@PathVariable Long id) throws Exception {
        
        // Look up the image
        ProductImage image = ProductImage.find(id);
        Part part = image.getPart();
        
        // Remove the image from the part
        part.getProductImages().remove(image);
        part.merge();
        
        // Remove the image
        image.remove();
        
        // Delete the files
        File original = new File(originalImagesDir, image.getFilename());
        FileUtils.deleteQuietly(original);
        
        for (int size : ImageResizer.SIZES) {
            File resized = new File(resizedImagesDir, image.getFilename(size));
            FileUtils.deleteQuietly(resized);
        }
        
        return new ResponseEntity<Void>((Void) null, HttpStatus.OK);
    }
    
    
    

}
