package image.process.imageprocessing.controller;

import image.process.imageprocessing.service.ConvertGrayscaleService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
public class ImageProcessingController {

    private final String imageDirectory;

    private final ConvertGrayscaleService grayscaleService;

    public ImageProcessingController(@Value("${image.directory}") String imageDirectory, ConvertGrayscaleService grayscaleService) {
        this.imageDirectory = imageDirectory;
        this.grayscaleService = grayscaleService;
    }

    @PostMapping("/convert-grayscale")
    public ResponseEntity<byte[]> postBlackAndWhite(@RequestBody MultipartFile file, @RequestParam(required = false) String format, @RequestParam(required = false) String name) {
        try {
            byte[] imageData = file.getBytes();
            String formatName = format != null ? format : getFileExtension(file.getOriginalFilename());
            byte[] result = grayscaleService.convertToBlackAndWhite(imageData, formatName);
            String fileName = name != null ? name : file.getOriginalFilename();
            if (formatName != null) {
                assert fileName != null;
                if (!fileName.toLowerCase().endsWith("." + formatName.toLowerCase())) {
                    fileName += "." + formatName;
                }
            }
            saveImageToDisk(result, fileName);
            assert formatName != null;
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                    .contentType(getMediaType(formatName))
                    .body(result);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveImageToDisk(byte[] imageData, String filename) throws IOException {
        File file = new File(imageDirectory + File.separator + filename);
        FileUtils.writeByteArrayToFile(file, imageData);
    }

    private String getFileExtension(String filename) {
        if (filename == null) return null;
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1) return null;
        return filename.substring(lastDotIndex + 1);
    }

    private MediaType getMediaType(String formatName) {
        return switch (formatName.toLowerCase()) {
            case "jpg", "jpeg" -> MediaType.IMAGE_JPEG;
            case "png" -> MediaType.IMAGE_PNG;
            case "gif" -> MediaType.IMAGE_GIF;
            // Add support for other image formats as needed
            default -> MediaType.APPLICATION_OCTET_STREAM; // Default to binary data
        };
    }
}
