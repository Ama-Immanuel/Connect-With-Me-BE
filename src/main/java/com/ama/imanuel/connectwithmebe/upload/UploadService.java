package com.ama.imanuel.connectwithmebe.upload;

import com.ama.imanuel.connectwithmebe.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadService {

    @Value("${application.upload.path}")
    private String uploadPath;

    public ResponseUpload uploadFile(MultipartFile file, User user, boolean isPublic) throws IOException {
        String newFilePath = uploadPath;
        if (!isPublic) newFilePath += "/" + normalizationEmail(user.getEmail());
        Path path = Paths.get(newFilePath);
        Files.createDirectories(path);
        String fileName = file.getOriginalFilename();
        assert fileName != null;
        String extension = getFileExtension(fileName);
        String objectFileName = UUID.randomUUID().toString()
                .replace("-", "") + "." + extension;
        Files.copy(file.getInputStream(), path.resolve(objectFileName));
        return ResponseUpload
                .builder()
                .objectName(objectFileName)
                .url("/" + (!isPublic ? "private" : "public") + "/" + objectFileName)
                .build();
    }

    private String normalizationEmail(String email) {
        return email.replace(".", "-");
    }

    private String getFileExtension(String fileName) {
        var split = fileName.split("\\.");
        return split[split.length - 1];
    }

    public Resource getFile(String filename) throws MalformedURLException {
        Path path = Paths.get(uploadPath);
        return new UrlResource(path.resolve(filename).toUri());
    }

    public Resource getFilePrivate(String filename, User user) throws MalformedURLException {
        Path path = Paths.get(uploadPath + "/" + normalizationEmail(user.getEmail()));
        return new UrlResource(path.resolve(filename).toUri());
    }
}
