package com.qiyuesuo.controller;

import com.qiyuesuo.utils.ClientUploadTest;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/*
  接收前端上传的文件，接收到的文件需要调用 Server 端
的文件上传接口，将文件上传至文件服务器，然后调用服务端的的元数据接
口获取文件详细信息返回给前端，响应前端的下载接口，从 Server 端下载
文件，解密后返回给前端*/

@Controller
public class ClientFileUpload {
    /**
     * 前端上传文件，后端接收后构造post请求，调用外部文件上传接口
     */
    private static final Logger logger = Logger.getLogger(ClientFileUpload.class);
    @PostMapping(value = "ClientUpload")
    public String ClientUpload(Model model, @RequestParam(value = "file") MultipartFile file) throws Exception {
        String url = "http://localhost:9090/server/ServerFileUpload";
        String uuid = ClientUploadTest.uploadFileTest(file, url);
        System.out.println(uuid);
        logger.info("....................");
        model.addAttribute("uuid",uuid);
        return "showAll";
    }
}


