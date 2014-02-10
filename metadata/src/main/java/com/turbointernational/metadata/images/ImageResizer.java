package com.turbointernational.metadata.images;

import com.turbointernational.metadata.domain.part.ProductImage;
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
    
    @Value("${images.originals}")
    private String originalsPath;
    
    @Value("${images.resized}")
    private String resizedPath;
    
    public void generateResizedImage(ProductImage image, int size) throws IOException, InterruptedException, IM4JavaException {
        ConvertCmd cmd = new ConvertCmd();
        IMOperation op = new IMOperation();
        op.addImage(originalsPath + image.getFilename());
        op.resize(size, size);
        op.addImage(resizedPath + ProductImage.getResizedFilename(image.getPart().getId(), image.getId(), size));
        cmd.run(op);
    }
    
}