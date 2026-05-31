package it.uniroma3.siw.calcio.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@Profile("!prod")
public class LocalImageStorageService implements ImageStorageService {

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(".jpg", ".jpeg", ".png", ".webp");

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Override
    public String store(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        validateImage(file);

        try {
            String extension = getExtension(file);
            String filename = UUID.randomUUID() + extension;
            Path folderPath = Paths.get(uploadDir, folder).toAbsolutePath().normalize();
            Files.createDirectories(folderPath);

            Path destination = folderPath.resolve(filename).normalize();
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/" + folder + "/" + filename;
        } catch (IOException e) {
            throw new IllegalStateException("Errore durante il salvataggio dell'immagine", e);
        }
    }

    @Override
    public void delete(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank() || !imageUrl.startsWith("/uploads/")) {
            return;
        }

        try {
            Path uploadRoot = Paths.get(uploadDir).toAbsolutePath().normalize();
            Path imagePath = uploadRoot.resolve(imageUrl.substring("/uploads/".length())).normalize();
            if (imagePath.startsWith(uploadRoot)) {
                Files.deleteIfExists(imagePath);
            }
        } catch (IOException e) {
            // File cleanup is best effort: the database operation should not fail because of it.
        }
    }

    private void validateImage(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Il file deve essere un'immagine");
        }

        String extension = getExtension(file);
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("Sono consentite solo immagini JPG, PNG o WEBP");
        }
    }

    private String getExtension(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename() == null ? "" : file.getOriginalFilename());
        int extensionStart = filename.lastIndexOf('.');
        if (extensionStart == -1) {
            throw new IllegalArgumentException("Il file deve avere estensione .jpg, .jpeg, .png o .webp");
        }
        return filename.substring(extensionStart).toLowerCase();
    }
}
