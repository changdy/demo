package com.example.demo.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputDirectory;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL;
import static org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants.EXIF_TAG_USER_COMMENT;
import static org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants.TIFF_TAG_ARTIST;
import static org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants.TIFF_TAG_COPYRIGHT;

/**
 * Created by Changdy on 2019/8/17.
 */
@Slf4j
public class ExifUtil {

    private static final String ALLOW_PIC_TYPE = "jpg";
    private static final SimpleDateFormat DATE_TIME_FORMATTER = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");

    private static final String COMPANY_NAME = "changdy company";
    private static final Double LONGITUDE = 20.0;
    private static final Double LATITUDE = 20.0;
    private static final Date UPLOAD_TIME = new Date();
    private static final String USER_NAME = "changdy";
    private static final String PHOTO_REMARKS = "备注";

    public static String setExifValue(String suffix, File picFile) throws IOException {
        if (!suffix.equals(ALLOW_PIC_TYPE)) {
            log.info("文件后缀:" + suffix);
            return null;
        }
        OutputStream os = null;
        try {
            TiffOutputSet outputSet = getTiffOutputSet(picFile);
            if (outputSet == null) {
                log.info("已经被app添加过信息:" + picFile.getName());
                return null;
            }
            outputSet.setGPSInDegrees(LONGITUDE, LATITUDE);
            String format = DATE_TIME_FORMATTER.format(UPLOAD_TIME);
            setExifInfo(outputSet.getOrCreateExifDirectory(), USER_NAME, PHOTO_REMARKS, format);
            setExifInfo(outputSet.getOrCreateRootDirectory(), USER_NAME, PHOTO_REMARKS, format);
            File tempFile = File.createTempFile("exif_", "." + suffix);
            tempFile.deleteOnExit();
            os = new BufferedOutputStream(new FileOutputStream(tempFile));
            new ExifRewriter().updateExifMetadataLossless(picFile, os, outputSet);
            return tempFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("添加exif中出错:", picFile.getName());
            return null;
        } finally {
            if (os != null) {
                os.close();
            }
        }
    }

    private static TiffOutputSet getTiffOutputSet(File picFile) throws IOException, ImageReadException, ImageWriteException {
        JpegImageMetadata jpegMetadata = (JpegImageMetadata) Imaging.getMetadata(picFile);
        if (null != jpegMetadata) {
            TiffField exifValue = jpegMetadata.findEXIFValue(TIFF_TAG_COPYRIGHT);
            if (exifValue != null && exifValue.getStringValue().equals(COMPANY_NAME)) {
                return null;
            }
            TiffImageMetadata exif = jpegMetadata.getExif();
            if (null != exif) {
                return exif.getOutputSet();
            }
        }
        return new TiffOutputSet();
    }

    private static void setExifInfo(TiffOutputDirectory exifDirectory, String userName, String address, String dateTime) throws ImageWriteException {
        exifDirectory.removeField(TIFF_TAG_COPYRIGHT);
        exifDirectory.removeField(TIFF_TAG_ARTIST);
        exifDirectory.removeField(EXIF_TAG_USER_COMMENT);
        exifDirectory.removeField(EXIF_TAG_DATE_TIME_ORIGINAL);
        exifDirectory.add(TIFF_TAG_COPYRIGHT, COMPANY_NAME);
        exifDirectory.add(TIFF_TAG_ARTIST, userName);
        exifDirectory.add(EXIF_TAG_USER_COMMENT, address);
        exifDirectory.add(EXIF_TAG_DATE_TIME_ORIGINAL, dateTime);
        exifDirectory.add(TiffTagConstants.TIFF_TAG_IMAGE_DESCRIPTION, address);
    }
}