package com.effourt.calenkit.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class ImageUpload {
    public String uploadImage(MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        String originalFilename = file.getOriginalFilename();
        //고유 ID값을 부여해서 이미지 이름 중복되지 않게 처리.
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID().toString() + "_" + originalFilename + extension;
        //이미지가 저장될 경로
        String savePath = "C:/Users/이진규/IdeaProjects/calenkit6/src/main/resources/static/img/";

        Path path = Paths.get(savePath + filename);
        //파일 저장
        Files.write(path, bytes);
        return filename;
    }

    public String getOriginalName(String filename) {
        return filename.substring(filename.indexOf("_") + 1);
    }
}