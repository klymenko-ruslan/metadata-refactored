package com.turbointernational.metadata.web.controller;

import static com.turbointernational.metadata.service.ImageService.SIZES;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.turbointernational.metadata.dao.PartDao;
import com.turbointernational.metadata.dao.PartTypeDao;
import com.turbointernational.metadata.dao.ProductImageDao;
import com.turbointernational.metadata.entity.PartType;
import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.entity.part.ProductImage;
import com.turbointernational.metadata.service.ImageService;

@RequestMapping("/metadata/image")
@RestController
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private PartDao partDao;

    @Autowired
    private PartTypeDao partTypeDao;

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

    @RequestMapping(value = "/{partTypeId}/ptlegend/{name}", method = GET)
    @Secured("ROLE_READ")
    public ResponseEntity<byte[]> getPartTypeLegendImage(@PathVariable("partTypeId") Long partTypeId) throws Exception {
        PartType partType = partTypeDao.findOne(partTypeId);
        String filename = partType.getLegendImgFilename();
        return imageService.getResizedImage(filename);
    }

    @Transactional
    @RequestMapping(value = "/{id}", method = DELETE)
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
        for (int size : SIZES) {
            imageService.delResizedImage(image.getFilename(size));
        }
        return new ResponseEntity<>((Void) null, OK);
    }

    @Transactional
    @RequestMapping(value = "/{id}", method = PUT)
    @Secured("ROLE_PART_IMAGES")
    public ResponseEntity<Void> publishPartImage(@PathVariable(name = "id") Long imageId,
            @RequestParam(name = "publish") Boolean publish) throws Exception {
        imageService.publish(imageId, publish);
        return new ResponseEntity<>((Void) null, OK);
    }

    @Transactional
    @RequestMapping(value = "/{id}/cdlegend.jpg", method = DELETE)
    @Secured("ROLE_PART_IMAGES")
    public ResponseEntity<Void> removePartCritDimsLegendImage(@PathVariable Long id) throws Exception {
        Part part = partDao.findOne(id);
        imageService.delResizedImage(part.getLegendImgFilename());
        part.setLegendImgFilename(null);
        return new ResponseEntity<>((Void) null, OK);
    }

    @Transactional
    @RequestMapping(value = "/{id}/ptlegend.jpg", method = DELETE)
    @Secured("ROLE_PART_IMAGES")
    public ResponseEntity<Void> removePartTypeLegendImage(@PathVariable Long id) throws Exception {
        PartType partType = partTypeDao.findOne(id);
        imageService.delResizedImage(partType.getLegendImgFilename());
        partType.setLegendImgFilename(null);
        return new ResponseEntity<>((Void) null, OK);
    }

}
