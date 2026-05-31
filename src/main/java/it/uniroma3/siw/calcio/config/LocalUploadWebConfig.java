package it.uniroma3.siw.calcio.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Profile("!prod")
public class LocalUploadWebConfig implements WebMvcConfigurer {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String resourceLocation = uploadRootLocation();
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(resourceLocation);
    }

    private String uploadRootLocation() {
        Path uploadRoot = Paths.get(uploadDir).toAbsolutePath().normalize();
        String location = uploadRoot.toUri().toString();
        return location.endsWith("/") ? location : location + "/";
    }
}
