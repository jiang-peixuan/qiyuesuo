package com.qiyuesuo.controller;

import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Text;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

@RunWith(SpringRunner.class)
@RequestMapping("/clientFileDownload")

public class ClientFileDownload {
    String url = "http://localhost:9090/server/serverDownLoad?filename=acb2d0ed-3dd9-4c52-8af2-a0b6b0731271.txt";
    //http://localhost:9090/server/serverDownLoad?filename=1.txt
    @Test
    public void clientFileDownload() throws ClientProtocolException, IOException {
        String uuid ="acb2d0ed-3dd9-4c52-8af2-a0b6b0731271.txt";
        RestTemplate restTemplate = new RestTemplate();
      String  forObject = restTemplate.getForObject(url, String.class);
        System.out.println(forObject);
//
//        FileSystemResource file = new FileSystemResource(filePath);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
//        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getFilename()))
//        headers.add("Pragma", "no-cache");
//        headers.add("Expires", "0");
//
//        return ResponseEntity
//                .ok()
//                .headers(headers)
//                .contentLength(file.contentLength())
//                .contentType(MediaType.parseMediaType("application/octet-stream"))
//                .body(new InputStreamResource(file.getInputStream()));
    }
}
