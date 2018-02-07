package com.turbointernational.metadata.service;

import static com.turbointernational.metadata.entity.Changelog.ServiceEnum.IMAGE;
import static com.turbointernational.metadata.util.FormatUtils.formatPart;
import static com.turbointernational.metadata.util.FormatUtils.formatProductImage;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.turbointernational.metadata.dao.PartDao;
import com.turbointernational.metadata.dao.ProductImageDao;
import com.turbointernational.metadata.entity.ChangelogPart;
import com.turbointernational.metadata.entity.part.Part;
import com.turbointernational.metadata.entity.part.ProductImage;
import com.turbointernational.metadata.service.ChangelogService.RelatedPart;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
@Service
@Profile("!integration")
public class ImageServiceImpl implements ImageService {

    private final static Logger log = LoggerFactory.getLogger(ImageServiceImpl.class);

    @Autowired
    private PartDao partDao;

    @Autowired
    private ProductImageDao productImageDao;

    @Autowired
    private ChangelogService changelogService;

    @Value("${images.originals}")
    private File originalsDir;

    @Value("${images.resized}")
    private File resizedDir;

    @Override
    public void generateResizedImage(String filenameOrigin, String filenameResized, int size)
            throws IOException, InterruptedException, IM4JavaException {
        generateResizedImage(filenameOrigin, filenameResized, size, size, false);
    }

    @Override
    public void generateResizedImage(String source, String destination, int width, int height, boolean removeSource)
            throws IOException, InterruptedException, IM4JavaException {
        ConvertCmd cmd = new ConvertCmd();
        IMOperation op = new IMOperation();

        File original = new File(originalsDir, source);
        File resized = new File(resizedDir, destination);

        op.addImage(original.getAbsolutePath());
        op.resize(width, height);
        op.addImage(resized.getAbsolutePath());
        cmd.run(op);

        if (removeSource) {
            FileUtils.deleteQuietly(new File(originalsDir, source));
        }
    }

    @Override
    public ResponseEntity<byte[]> getOriginalImage(String filename) throws IOException {
        return getImage(originalsDir, filename);
    }

    @Override
    public ResponseEntity<byte[]> getResizedImage(String filename) throws IOException {
        return getImage(resizedDir, filename);
    }

    @Override
    public void delProductImage(Long id) {
        // Look up the image
        ProductImage image = productImageDao.findOne(id);
        Part part = image.getPart();
        // Remove the image from the part
        part.getProductImages().remove(image);
        partDao.merge(part);
        // Remove the image
        productImageDao.remove(image);
        // Delete the files
        delOriginalImage(image.getFilename());
        for (int size : SIZES) {
            delResizedImage(image.getFilename(size));
        }
        Collection<RelatedPart> relatedParts = new ArrayList<>(1);
        relatedParts.add(new RelatedPart(part.getId(), ChangelogPart.Role.PART0));
        changelogService.log(IMAGE, "A product image " + formatProductImage(image) + " of the part " + formatPart(part)
                + " has been removed.", relatedParts);
    }

    @Override
    public void delOriginalImage(String filename) {
        delImage(originalsDir, filename);
    }

    @Override
    public void delResizedImage(String filename) {
        delImage(resizedDir, filename);
    }

    private void delImage(File dir, String filename) {
        File original = new File(dir, filename);
        FileUtils.deleteQuietly(original);
    }

    @Override
    public void publish(Long imageId, Boolean publish) {
        ProductImage pi = productImageDao.findOne(imageId);
        pi.setPublish(publish);
        Part part = pi.getPart();
        Collection<RelatedPart> relatedParts = new ArrayList<>(1);
        relatedParts.add(new RelatedPart(part.getId(), ChangelogPart.Role.PART0));
        String newState = publish ? "published" : "unpublished";
        changelogService.log(IMAGE, "A product image " + formatProductImage(pi) + " of the part " + formatPart(part)
                + " has been " + newState + ".", relatedParts);
    }

    @Override
    @Transactional
    public void setPrimary(Long imageId, Boolean primary) {
        ProductImage pi = productImageDao.findOne(imageId);
        Long partId = pi.getPart().getId();
        List<ProductImage> images = partDao.findProductImages(Arrays.asList(partId));
        images.forEach(img -> img.setMain(img.getId().equals(imageId)));
        Part part = pi.getPart();
        Collection<RelatedPart> relatedParts = new ArrayList<>(1);
        relatedParts.add(new RelatedPart(part.getId(), ChangelogPart.Role.PART0));
        changelogService.log(IMAGE, "A product image " + formatProductImage(pi) + " of the part " + formatPart(part)
                + " has been set as primary.", relatedParts);
    }

    private ResponseEntity<byte[]> getImage(File dir, String filename) throws IOException {
        File imageFile = new File(dir, filename);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "image/jpg");
        try {
            byte[] bytes = FileUtils.readFileToByteArray(imageFile);
            return new ResponseEntity<>(bytes, headers, OK);
        } catch (FileNotFoundException e) {
            log.warn("Part's image loading failed: {}", e.getMessage());
            return new ResponseEntity<>(null, headers, NOT_FOUND);
        }
    }

}
