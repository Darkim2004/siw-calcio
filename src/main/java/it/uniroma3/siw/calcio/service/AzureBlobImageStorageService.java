package it.uniroma3.siw.calcio.service;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.models.BlobHttpHeaders;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@Profile("prod")
public class AzureBlobImageStorageService implements ImageStorageService {

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(".jpg", ".jpeg", ".png", ".webp");

    private final BlobContainerClient containerClient;

    public AzureBlobImageStorageService(BlobContainerClient containerClient) {
        this.containerClient = containerClient;
    }

    @Override
    public String store(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        validateImage(file);

        try {
            String extension = getExtension(file);
            String blobName = folder + "/" + UUID.randomUUID() + extension;
            BlobClient blobClient = containerClient.getBlobClient(blobName);

            blobClient.upload(file.getInputStream(), file.getSize(), true);
            blobClient.setHttpHeaders(new BlobHttpHeaders().setContentType(file.getContentType()));

            return blobClient.getBlobUrl();
        } catch (IOException e) {
            throw new IllegalStateException("Errore durante l'upload dell'immagine su Azure", e);
        }
    }

    @Override
    public void delete(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            return;
        }

        String containerUrl = containerClient.getBlobContainerUrl();
        if (!imageUrl.startsWith(containerUrl + "/")) {
            return;
        }

        String blobName = imageUrl.substring(containerUrl.length() + 1);
        try {
            containerClient.getBlobClient(blobName).deleteIfExists();
        } catch (RuntimeException e) {
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
