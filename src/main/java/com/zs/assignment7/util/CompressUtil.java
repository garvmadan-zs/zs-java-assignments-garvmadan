package com.zs.assignment7.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompressUtil {
    private static final Logger logger = LoggerFactory.getLogger(CompressUtil.class);

    public static void compressFile(String inputFile, String outputFile) {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inputFile));
             GZIPOutputStream gzip = new GZIPOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile)))) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                gzip.write(buffer, 0, bytesRead);
            }
            gzip.finish();
            logger.info("CSV records compressed successfully.");
        } catch (Exception e) {
            logger.error("The file cannot be found ", e);
        }

    }

}
