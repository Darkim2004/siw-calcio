package it.uniroma3.siw.calcio.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStorageService {

    String store(MultipartFile file, String folder);

    void delete(String imageUrl);
}
