/**
 *
 */
package com.turbointernational.metadata.service;

import java.io.IOException;

import org.im4java.core.IM4JavaException;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
@Service
@Profile("integration")
public class ImageServiceMockImpl implements ImageService {

    @Override
    public void generateResizedImage(String filenameOrigin, String filenameResized, int size)
            throws IOException, InterruptedException, IM4JavaException {
        // stub
    }

    @Override
    public void generateResizedImage(String source, String destination, int width, int height, boolean removeSource)
            throws IOException, InterruptedException, IM4JavaException {
        // stub
    }

    @Override
    public ResponseEntity<byte[]> getOriginalImage(String filename) throws IOException {
        // stub
        return null;
    }

    @Override
    public ResponseEntity<byte[]> getResizedImage(String filename) throws IOException {
        // stub
        return null;
    }

    @Override
    public void delOriginalImage(String filename) {
        // stub
    }

    @Override
    public void delResizedImage(String filename) {
        // stub
    }

    @Override
    public void publish(Long imageId, Boolean publish) {
        // stub
    }

    @Override
    public void setPrimary(Long imageId, Boolean primary) {
        // stub
    }

}
