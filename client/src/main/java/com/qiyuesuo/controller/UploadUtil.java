package com.qiyuesuo.controller;

import org.apache.http.client.methods.HttpPost;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;


public class UploadUtil {
    public static File multipartFileToFile(MultipartFile file, String fileName) {
        if (file != null) {
            File conventFile = new File(fileName);
            try {
                file.transferTo(conventFile);
                return conventFile;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String uploadFileTest(MultipartFile file, String url_str) {
        String originalFilename = file.getOriginalFilename();
        ResponseEntity<String> stringResponseEntity =null;
        File tempFile = multipartFileToFile(file,  "F:\\com.qiyuesuo.qiyuesuo\\client\\src\\test\\"+originalFilename);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setConnection("Keep-Alive");
        headers.setCacheControl("no-cache");
        FileSystemResource resource = new FileSystemResource(tempFile);
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        formData.add("file",resource);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(formData, headers);
        try {
            stringResponseEntity = restTemplate.postForEntity(url_str, request, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tempFile.delete();
        return stringResponseEntity.getBody();
    }
}
