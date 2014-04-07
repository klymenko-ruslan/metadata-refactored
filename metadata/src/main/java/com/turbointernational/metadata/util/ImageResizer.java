package com.turbointernational.metadata.util;

import com.turbointernational.metadata.domain.part.ProductImage;
import java.io.File;
import java.io.IOException;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Edward
 */
@Service
public class ImageResizer {
    
    public static final int[] SIZES = {50, 135, 1000};
    
    @Value("${images.originals}")
    private File originalsDir;
    
    @Value("${images.resized}")
    private File resizedDir;
    
    public void generateResizedImage(ProductImage image, int size) throws IOException, InterruptedException, IM4JavaException {
        ConvertCmd cmd = new ConvertCmd();
        IMOperation op = new IMOperation();
        
        File original = new File(originalsDir, image.getFilename());
        File resized = new File(resizedDir, image.getFilename(size));
        
        op.addImage(original.getAbsolutePath());
        op.resize(size, size);
        op.addImage(resized.getAbsolutePath());
        cmd.run(op);
    }
    
}