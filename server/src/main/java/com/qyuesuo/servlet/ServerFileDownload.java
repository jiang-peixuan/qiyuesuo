package com.qyuesuo.servlet;

import com.qyuesuo.service.FileService;
import com.qyuesuo.service.Impl.FileServiceImpl;
import com.qyuesuo.utils.JDBCUtil;
import org.apache.log4j.Logger;
import org.junit.Test;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.Part;
import java.io.*;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//http://localhost:9090/server/serverDownLoad?filename=1.txt
@WebServlet("/serverDownLoadById")

public class ServerFileDownload extends HttpServlet {
    private static final Logger logger = Logger.getLogger(ServerFileDownload.class);
    FileService fileService = new FileServiceImpl();

    @Override
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) {
        //1. 获取要下载的文件名
        String uuid = request.getParameter("filename");
            //设置响应头类型 content-type
            String mimeType = this.getServletContext().getMimeType(uuid);
            response.setContentType(mimeType);
            response.setCharacterEncoding("UTF-8");
            //设置响应头打开方式content-disposition
        InputStream in = null;
        ServletOutputStream out = null;
        try {
            in = fileService.fileDownLoad(uuid);
            out = response.getOutputStream();
            int len=0;
            byte [] bytes = new byte[1024*8];
            while ((len=in.read(bytes))!=-1){
                    out.write(bytes,0,len);
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.debug(e.getMessage());
            response.setStatus(410);
        } finally {
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(out!=null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        logger.info("【文件下载】uuid = " + uuid);


    }

    @Override
    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        this.doPost(request, response);
    }
}
