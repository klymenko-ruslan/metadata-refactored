package com.turbointernational.metadata.util;

import static java.net.URLConnection.guessContentTypeFromName;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static org.apache.commons.io.FileUtils.copyFile;
import static org.apache.commons.io.FileUtils.sizeOf;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dmytro.trunykov@zorallabs.com
 */
public class DownloadUtils {

    private final static Logger log = LoggerFactory.getLogger(DownloadUtils.class);

    /**
     * Returns a local file to a browser.
     *
     * @param file
     *            a local file to download
     * @param downloadName
     *            name of the file it will be displayed by browser. If this option is null,
     *            then hardcoded value 'attachment.bin' will be used
     * @param response
     *            HTTP response
     * @throws IOException
     */
    public static void download(File file, String downloadName, HttpServletResponse response)
            throws IOException {
        if (!file.exists()) {
            log.warn("Local file to download not found: {}", file);
            response.setStatus(SC_NOT_FOUND);
            return;
        }
        ServletOutputStream out = response.getOutputStream();
        if (StringUtils.isBlank(downloadName)) {
            downloadName = "attachment.bin";
        }
        String mimeType = guessContentTypeFromName(downloadName);
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }
        response.setContentType(mimeType);
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", downloadName));
        response.setContentLength((int) sizeOf(file));
        copyFile(file, out);
    }

}
