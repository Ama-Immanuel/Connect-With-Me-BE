package com.ama.imanuel.connectwithmebe.upload;


import com.ama.imanuel.connectwithmebe.config.IgnoreResponseBinding;
import com.ama.imanuel.connectwithmebe.shared.CodeError;
import com.ama.imanuel.connectwithmebe.shared.ErrorAppException;
import com.ama.imanuel.connectwithmebe.shared.SuccessResponse;
import com.ama.imanuel.connectwithmebe.utils.PrincipalUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/upload")
@RequiredArgsConstructor
@Tag(name = "Upload")
public class UploadController {
    private final PrincipalUtils principalUtils;
    private final UploadService service;

    @PostMapping(value = "", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public SuccessResponse<ResponseUpload> uploadFile(
            @RequestParam(value = "file") MultipartFile file,
            @RequestParam(value = "is_public", required = false, defaultValue = "false") String isPublic,
            Principal principal
    ) throws IOException {
        var user = principalUtils.getUser(principal);
        var boolIsPublic = isPublic.equalsIgnoreCase("true");
        if (file.isEmpty()) throw new ErrorAppException(CodeError.BAD_REQUEST.getCodeError(), "file is required");
        return new SuccessResponse<ResponseUpload>(service.uploadFile(file, user, boolIsPublic));
    }

    @GetMapping("/public/{filename:.+}")
    @IgnoreResponseBinding
    @ResponseBody
    public ResponseEntity<Resource> getPublicImage(@PathVariable String filename, HttpServletRequest request) throws MalformedURLException {
        Resource file = service.getFile(filename);
        String contentType = getContentType(request, file);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    private String getContentType(HttpServletRequest request, Resource file) {
        try {
            return request.getServletContext().getMimeType(file.getFile().getAbsolutePath());
        } catch (IOException ex) {
            return "application/octet-stream";
        }
    }

    @GetMapping("/private/{filename:.+}")
    @IgnoreResponseBinding
    @ResponseBody
    public ResponseEntity<Resource> getPrivateImage(@PathVariable String filename, Principal principal, HttpServletRequest request) throws MalformedURLException {
        Resource file = service.getFilePrivate(filename, principalUtils.getUser(principal));
        String contentType = getContentType(request, file);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}
