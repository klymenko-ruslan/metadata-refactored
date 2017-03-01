package com.turbointernational.metadata.service;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.persistence.Transient;

import org.apache.commons.io.FileUtils;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.turbointernational.metadata.dao.ProductImageDao;
import com.turbointernational.metadata.entity.part.ProductImage;

/**
 * @author Edward
 */
@Service
public class ImageService {

    private final static Logger log = LoggerFactory.getLogger(ImageService.class);

    public final static int[] SIZES = { 50, 135, 1000 };

    public final static int PART_TYPE_LEGEND_WIDTH = 800;
    public final static int PART_TYPE_LEGEND_HEIGHT = 600;

    public final static int PART_CRIT_DIM_LEGEND_WIDTH = 640;
    public final static int PART_CRIT_DIM_LEGEND_HEIGHT = 480;

    @Autowired
    private ProductImageDao productImageDao;

    @Value("${images.originals}")
    private File originalsDir;

    @Value("${images.resized}")
    private File resizedDir;

    public void generateResizedImage(String filenameOrigin, String filenameResized, int size)
            throws IOException, InterruptedException, IM4JavaException {
        generateResizedImage(filenameOrigin, filenameResized, size, size, false);
    }

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

    public ResponseEntity<byte[]> getOriginalImage(String filename) throws IOException {
        return getImage(originalsDir, filename);
    }

    public ResponseEntity<byte[]> getResizedImage(String filename) throws IOException {
        return getImage(resizedDir, filename);
    }

    public void delOriginalImage(String filename) {
        delImage(originalsDir, filename);
    }

    public void delResizedImage(String filename) {
        delImage(resizedDir, filename);
    }

    private void delImage(File dir, String filename) {
        File original = new File(dir, filename);
        FileUtils.deleteQuietly(original);
    }

    @Transient
    public void publish(Long imageId, Boolean publish) {
        ProductImage pi = productImageDao.findOne(imageId);
        pi.setPublish(publish);
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