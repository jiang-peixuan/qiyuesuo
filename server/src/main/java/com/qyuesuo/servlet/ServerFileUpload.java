package com.qyuesuo.servlet;
import com.qyuesuo.service.FileService;
import com.qyuesuo.service.Impl.FileServiceImpl;
import org.apache.log4j.Logger;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.io.*;

/**
 * 文件服务器servlet，负责接接收上传的文件
 */

@MultipartConfig
@WebServlet("/ServerFileUpload")
public class ServerFileUpload extends HttpServlet {
    private static final Logger logger = Logger.getLogger(ServerFileUpload.class);
    private FileService fileService = new FileServiceImpl();

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        String uuid = fileService.fileUpload(request);
        out.write(uuid);
        logger.info("uuid ======》"+uuid);
    }



}
