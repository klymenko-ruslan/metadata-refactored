package com.turbointernational.metadata.services;

import com.turbointernational.metadata.domain.part.ProductImage;
import org.apache.commons.io.FileUtils;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

/**
 * @author Edward
 */
@Service
public class ImageResizerService {
    
    public final static int[] SIZES = {50, 135, 1000};

    public final static int PART_TYPE_CRIT_DIM_LEGEND_WIDTH = 640;
    public final static int PART_TYPE_CRIT_DIM_LEGEND_HEIGHT = 480;
    
    @Value("${images.originals}")
    private File originalsDir;
    
    @Value("${images.resized}")
    private File resizedDir;
    
    public void generateResizedImage(ProductImage image, int size) throws IOException, InterruptedException, IM4JavaException {
        generateResizedImage(image.getFilename(), image.getFilename(size), size, size, false);
    }

    public void generateResizedImage(String source, String destination, int width, int height, boolean removeSource) throws IOException, InterruptedException, IM4JavaException {
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

}