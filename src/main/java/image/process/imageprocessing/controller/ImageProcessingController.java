package image.process.imageprocessing.controller;

import image.process.imageprocessing.service.ConvertGrayscaleService;
import image.process.imageprocessing.service.ResizeImageService;
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

    private final ResizeImageService resizeImageService;

    public ImageProcessingController(@Value("${image.directory}") String imageDirectory, ConvertGrayscaleService grayscaleService, ResizeImageService resizeImageService) {
        this.imageDirectory = imageDirectory;
        this.grayscaleService = grayscaleService;
        this.resizeImageService = resizeImageService;
    }

    @PostMapping("/convert-grayscale")
    public ResponseEntity<byte[]> postBlackAndWhite(@RequestBody MultipartFile file, @RequestParam(required = false) String format, @RequestParam(required = false) String name) {
        try {
            byte[] imageData = file.getBytes();
            String formatName = format != null ? format : getFileExtension(file.getOriginalFilename());
            byte[] result = grayscaleService.convertToBlackAndWhite(imageData, formatName);
            return getResponseEntity(file, name, formatName, result);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/resize")
    public ResponseEntity<byte[]> postNewSizeOfImage(@RequestBody MultipartFile file,
                                                     @RequestParam int width,
                                                     @RequestParam int height,
                                                     @RequestParam(required = false) String format,
                                                     @RequestParam(required = false) String name) throws IOException {
        try{
            byte[] imageData = file.getBytes();
            String formatName = format != null ? format : getFileExtension(file.getOriginalFilename());
            byte[] result = resizeImageService.resize(imageData,width,height,formatName);
            return getResponseEntity(file, name, formatName, result);
        }catch (IOException e){
            throw new IOException(e);
        }
    }

    private ResponseEntity<byte[]> getResponseEntity(@RequestBody MultipartFile file, @RequestParam(required = false) String name, String formatName, byte[] result) throws IOException {
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
