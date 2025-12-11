package utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ImageCompressor {
    private static final long MAX_FILE_SIZE = 300 * 1024; // 300KB
    private static final int MAX_WIDTH = 1200;
    private static final int MAX_HEIGHT = 1200;
    private static final float QUALITY_STEP = 0.1f;

    /**
     * Compress image to max 300KB
     */
    public static File compressImage(File inputFile, File outputFile, String format) throws IOException {
        if (!inputFile.exists()) {
            throw new IOException("Input file does not exist");
        }

        BufferedImage originalImage = ImageIO.read(inputFile);
        if (originalImage == null) {
            throw new IOException("Unable to read image file");
        }

        // Resize if too large
        BufferedImage resizedImage = resizeImage(originalImage);
        
        // Compress with quality adjustment
        float quality = 0.9f;
        File result = outputFile;
        
        while (quality > 0.1f) {
            try {
                result = writeImageWithQuality(resizedImage, outputFile, format, quality);
                if (result.length() <= MAX_FILE_SIZE) {
                    break;
                }
                quality -= QUALITY_STEP;
            } catch (IOException e) {
                Logger.error("Error compressing image with quality " + quality, e);
                quality -= QUALITY_STEP;
            }
        }

        if (result.length() > MAX_FILE_SIZE) {
            Logger.warning("Compressed image still exceeds 300KB: " + result.length() + " bytes");
        }

        return result;
    }

    /**
     * Compress image from InputStream
     */
    public static File compressImage(InputStream inputStream, File outputFile, String format) throws IOException {
        BufferedImage originalImage = ImageIO.read(inputStream);
        if (originalImage == null) {
            throw new IOException("Unable to read image from stream");
        }

        BufferedImage resizedImage = resizeImage(originalImage);
        
        float quality = 0.9f;
        File result = outputFile;
        
        while (quality > 0.1f) {
            try {
                result = writeImageWithQuality(resizedImage, outputFile, format, quality);
                if (result.length() <= MAX_FILE_SIZE) {
                    break;
                }
                quality -= QUALITY_STEP;
            } catch (IOException e) {
                Logger.error("Error compressing image with quality " + quality, e);
                quality -= QUALITY_STEP;
            }
        }

        return result;
    }

    private static BufferedImage resizeImage(BufferedImage original) {
        int width = original.getWidth();
        int height = original.getHeight();

        if (width <= MAX_WIDTH && height <= MAX_HEIGHT) {
            return original;
        }

        double widthRatio = (double) MAX_WIDTH / width;
        double heightRatio = (double) MAX_HEIGHT / height;
        double ratio = Math.min(widthRatio, heightRatio);

        int newWidth = (int) (width * ratio);
        int newHeight = (int) (height * ratio);

        BufferedImage resized = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resized.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(original, 0, 0, newWidth, newHeight, null);
        g.dispose();

        return resized;
    }

    private static File writeImageWithQuality(BufferedImage image, File outputFile, String format, float quality) throws IOException {
        if (!outputFile.getParentFile().exists()) {
            outputFile.getParentFile().mkdirs();
        }

        ImageIO.write(image, format, outputFile);
        return outputFile;
    }
}

