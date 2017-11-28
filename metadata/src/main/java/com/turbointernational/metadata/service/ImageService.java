package com.turbointernational.metadata.service;

import java.io.IOException;

import org.im4java.core.IM4JavaException;
import org.springframework.http.ResponseEntity;

/**
 * @author Edward
 */
public interface ImageService {

    public final static int[] SIZES = { 50, 135, 1000 };

    public final static int PART_TYPE_LEGEND_WIDTH = 800;
    public final static int PART_TYPE_LEGEND_HEIGHT = 600;

    public final static int PART_CRIT_DIM_LEGEND_WIDTH = 640;
    public final static int PART_CRIT_DIM_LEGEND_HEIGHT = 480;

    public void generateResizedImage(String filenameOrigin, String filenameResized, int size)
            throws IOException, InterruptedException, IM4JavaException;

    public void generateResizedImage(String source, String destination, int width, int height, boolean removeSource)
            throws IOException, InterruptedException, IM4JavaException;

    public ResponseEntity<byte[]> getOriginalImage(String filename) throws IOException;

    public ResponseEntity<byte[]> getResizedImage(String filename) throws IOException;
    
    public void delProductImage(Long id);

    public void delOriginalImage(String filename);

    public void delResizedImage(String filename);

    public void publish(Long imageId, Boolean publish);

    public void setPrimary(Long imageId, Boolean primary);

}