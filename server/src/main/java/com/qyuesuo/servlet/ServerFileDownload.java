package com.qyuesuo.servlet;

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

    @Override
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        //1. 获取要下载的文件名
        String uuid = request.getParameter("filename");
        //2.根据UUID获取该文件的存储位置
        Statement statement = JDBCUtil.getStatement();
        String realPath = null;
        BufferedInputStream bufferedInputStream = null;
        ServletOutputStream outputStream = null;
        FileInputStream fileInputStream = null;
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM t_file where newName = '" + uuid + "'");
            logger.info("SELECT * FROM t_file where newName = '" + uuid + "'");
            while (resultSet.next()) {
                realPath = resultSet.getString("path");
            }
            //设置响应头
            //设置响应头类型 content-type
            String mimeType = this.getServletContext().getMimeType(uuid);
            response.setContentType(mimeType);
            response.setCharacterEncoding("UTF-8");
            //设置响应头打开方式content-disposition
            response.setHeader("content-disposition", "attachment;filename=" + "uuid");
            try {
                fileInputStream = new FileInputStream(realPath + "\\" + uuid);
                outputStream = response.getOutputStream();
                byte[] bytes = new byte[1024000];
                int len = 0;
                while ((len = fileInputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        this.doPost(request, response);
    }
}
