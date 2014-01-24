package com.mycompany.ImageResizer;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.process.ProcessStarter;
import org.im4java.utils.FilenameLoader;

/**
 * @author Edward
 */
public class ImageResizer {
    // Create necessary variables
    static int[] sizes = {50,135,1000}; // File sizes and directory names
    // Source/base directory
    static String sourceDir = System.getProperty("user.dir") + "\\images\\base";
    static String destDirBase = System.getProperty("user.dir") + "\\images";
    // Mogrify source
    static String imageMagickPath = "C:\\Program Files\\ImageMagick-6.8.8-Q16";

    public static void main(String[] args) throws IOException, Exception {
        
        ProcessStarter.setGlobalSearchPath(imageMagickPath);
        ensureSizeDirExists();

        // Resize all base files to their appropriate directories
        for (int size : sizes) {
            resizeImages(size);
        }
    }

    private static void ensureSizeDirExists() {
        // Make a sub-directory for the sizes
        for (int size : sizes) {
            File sizeDir = new File(destDirBase, "" + size);
            sizeDir.mkdir();
        }
    }
    
    public static void resizeImage (Long sku, int sequence) throws IOException, InterruptedException, IM4JavaException {
        // Take a picture's location, and resize it to the size(s) needed
        File originalImage = new File(sourceDir, Long.toString(sku) + "-" + Integer.toString(sequence) + ".jpg");
        String srcImage = originalImage.toString();

        int lastSlash = srcImage.lastIndexOf('\\');
        
        for (int size : sizes) {
            String dstImage = destDirBase + "\\" + size + "\\" + srcImage.substring(lastSlash);
            
            // create command
            ConvertCmd cmd = new ConvertCmd();

            // create the operation, add images and operators/options
            IMOperation op = new IMOperation();

            // create a simple thumbnail operation
            op.addImage(srcImage);     // placeholder input filename
            op.resize(size,size);
            op.addImage(dstImage);     // placeholder output filename
            cmd.run(op);
        }
        
    }
    
    public static void resizeImages(int size) throws Exception {
        FilenameLoader loader =  new FilenameLoader();
        List<String> images = loader.loadFilenames(sourceDir);
        
        // create command
        ConvertCmd cmd = new ConvertCmd();
        
        // create the operation, add images and operators/options
        IMOperation op = new IMOperation();
        
        // create a simple thumbnail operation
        op.addImage();     // placeholder input filename
        op.resize(size,size);
        op.addImage();     // placeholder output filename
        
        for (String srcImage:images) {
            int lastSlash = srcImage.lastIndexOf('\\');
            String dstImage = destDirBase + "\\" + size + "\\" + srcImage.substring(lastSlash);
            cmd.run(op,srcImage,dstImage);
        }
    }
}