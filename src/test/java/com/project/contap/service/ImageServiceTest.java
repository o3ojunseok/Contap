package com.project.contap.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.project.contap.common.util.ImageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {
    @InjectMocks
    private ImageService imageService;

    @Mock
    private AmazonS3Client amazonS3Client;

    @Test
    @DisplayName("성공")
    void getMyInfo_success() throws IOException {
        String userPath = System.getProperty("user.dir");
        MultipartFile multipartFile = new MockMultipartFile("test.jpg","abv",".jpg", FileCopyUtils.copyToByteArray(new FileInputStream(new File(userPath+ "/src/test/resources/static/test.jpg"))));
        String st = "static";
        URL url = new URL("http://java.sun.com");
        when(amazonS3Client.getUrl(isNull(),any(String.class))).thenReturn(url);
        String test = imageService.upload(multipartFile,st,null);
    }



}
