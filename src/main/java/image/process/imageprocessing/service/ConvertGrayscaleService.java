package image.process.imageprocessing.service;

import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class ConvertGrayscaleService {

    public byte[] convertToBlackAndWhite (byte[] imageData, String format) throws IOException {
        BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(imageData));
        BufferedImage convertedImage = Scalr.apply(originalImage,Scalr.OP_GRAYSCALE);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(convertedImage, format, baos);
        return baos.toByteArray();
    }
}
