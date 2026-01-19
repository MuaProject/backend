package Mua.Mua_backend.global.s3.controller;

import Mua.Mua_backend.global.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;
import java.util.Map;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;

    @GetMapping("/presigned")
    public Map<String, String> getPreSignedUploadUrl(
            @RequestParam String type,
            @RequestParam String contentType
    ) {
        String key = s3Service.createKey(type);
        URL url = s3Service.generatePreSignedUploadUrl(key, contentType);

        return Map.of(
                "uploadUrl", url.toString(),
                "fileKey", key
        );
    }
}