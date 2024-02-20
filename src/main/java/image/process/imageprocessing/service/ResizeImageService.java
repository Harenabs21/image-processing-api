package image.process.imageprocessing.service;

import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class ResizeImageService {
    public byte[] resize(byte[] image, int width, int height, String format) throws IOException {
        BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(image));
        BufferedImage resizedImage = Scalr.resize(originalImage, Scalr.Method.SPEED, Scalr.Mode.FIT_EXACT, width, height);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, format, baos);
        return baos.toByteArray();
    }
}
