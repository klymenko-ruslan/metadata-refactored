package com.turbointernational.metadata.web;

import com.turbointernational.metadata.domain.part.Part;
import com.turbointernational.metadata.domain.part.PartDao;
import com.turbointernational.metadata.domain.part.ProductImage;
import com.turbointernational.metadata.domain.part.ProductImageDao;
import com.turbointernational.metadata.services.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RequestMapping("/metadata/image")
@RestController
public class ImageController {

    private static final Logger log = LoggerFactory.getLogger(ImageController.class);

    @Autowired
    private ImageService imageService;
    
    @Autowired
    private PartDao partDao;
    
    @Autowired
    private ProductImageDao productImageDao;
    @RequestMapping(value = "/{id}.jpg", method = GET)
    @Secured("ROLE_READ")
    public ResponseEntity<byte[]> getPartImage(@PathVariable Long id) throws Exception {
        ProductImage productImage = productImageDao.findOne(id);
        String filename = productImage.getFilename();
        return imageService.getOriginalImage(filename);
    }

    @RequestMapping(value = "/{size}/{id}.jpg", method = GET)
    @Secured("ROLE_READ")
    public ResponseEntity<byte[]> getResizedPartImage(@PathVariable int size, @PathVariable Long id) throws Exception {
        ProductImage productImage = productImageDao.findOne(id);
        String filename = productImage.getFilename(size);
        return imageService.getResizedImage(filename);
    }

    @RequestMapping(value = "/{partId}/cdlegend/{name}", method = GET)
    @Secured("ROLE_READ")
    public ResponseEntity<byte[]> getPartCritDimsLegendImage(@PathVariable("partId") Long partId) throws Exception {
        Part part = partDao.findOne(partId);
        String filename = part.getLegendImgFilename();
        return imageService.getResizedImage(filename);
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
        imageService.delOriginalImage(image.getFilename());
        for (int size : ImageService.SIZES) {
            imageService.delResizedImage(image.getFilename(size));
        }
        return new ResponseEntity<>((Void) null, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value="/{id}/cdlegend.jpg", method = DELETE)
    @Secured("ROLE_PART_IMAGES")
    public ResponseEntity<Void> removePartCritDimsLegendImage(@PathVariable Long id) throws Exception {
        Part part = partDao.findOne(id);
        imageService.delResizedImage(part.getLegendImgFilename());
        part.setLegendImgFilename(null);
        return new ResponseEntity<>((Void) null, HttpStatus.OK);
    }

}
