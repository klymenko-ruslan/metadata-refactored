package com.turbointernational.metadata.services;

import org.apache.commons.io.IOUtils;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by trunikov on 04.08.16.
 */
@Service
public class ResourceService implements ResourceLoaderAware {

    private final static String encoding = "UTF-8";

    private ResourceLoader resourceLoader;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public String load(String location) throws IOException {
        Resource resourceSettings = resourceLoader.getResource(location);
        InputStream inputStream = resourceSettings.getInputStream();
        try {
            return IOUtils.toString(inputStream, encoding);
        } finally {
            inputStream.close();
        }
    }

    public String loadFromMeta(String filename) throws IOException {
        String location = ResourceUtils.CLASSPATH_URL_PREFIX + "META-INF" + File.separator + filename;
        return load(location);
    }

}
