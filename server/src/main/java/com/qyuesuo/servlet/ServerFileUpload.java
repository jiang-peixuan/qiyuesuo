package com.qyuesuo.servlet;
import com.qyuesuo.utils.JDBCUtil;
import org.apache.log4j.Logger;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.Part;
import java.io.*;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * 文件服务器servlet，负责接接收上传的文件
 */

@MultipartConfig
@WebServlet("/ServerFileUpload")
public class ServerFileUpload extends HttpServlet {
    private static final Logger logger = Logger.getLogger(ServerFileUpload.class);
    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String uuid = null;
       logger.info("收到client消息：");
       logger.info(request);
        logger.info("收到client消息2：");
        String header = request.getHeader(" Content-Type");
        System.out.println(header);
        Part file = request.getPart("file");
        uuid= FileProcess(file);
        out.write(uuid);
        logger.info("uuid ======》"+uuid);

    }
    private String  FileProcess(Part part) throws IOException {
        System.out.println("part.getName(): " + part.getName());
        if (part.getName().equals("file")) {
            String cd = part.getHeader("Content-Disposition");
            String[] cds = cd.split(";");
            //获取文件的相关属性
            String oldName = cds[2].substring(cds[2].indexOf("=") + 1).substring(cds[2].lastIndexOf("//") + 1).replace("\"", "");
            String ext = oldName.substring(oldName.lastIndexOf(".") + 1);
            long fileSize = part.getSize();
            String uuid = UUID.randomUUID().toString();
            String newName = uuid+"."+ext;
            //设置存放的绝对路径
            String path = this.getServletContext().getRealPath("/");
            String storyFile = new SimpleDateFormat("yyyyMMdd").format(new Date());//目录
            String realPath = path + "\\" + storyFile;
            logger.info("存放的路径为："+realPath);
            //判断该目标路径(目录)是否存在,不存在就创建
            File file = new File(realPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            //将原文件名，新文件名，文件类型，存储路径保存进数据库
            try {
                Statement statement = JDBCUtil.getStatement();
                statement.executeUpdate("insert into t_file(oldName,newName,ext,path,size)VALUES('"+oldName+"','"+newName+"','"+ext+"','"+realPath+"','"+fileSize+"')");
            } catch (SQLException e) {
                e.printStackTrace();
                logger.info("insert into t_file(oldName,newName,ext,path,size)VALUES(oldName,newName,ext,realPath,fileSize)");
            } finally {
              JDBCUtil.close();
            }
            logger.info("文件存入数据库。。");
            logger.info("path："+path);
            //将文件写入硬盘
            InputStream is = part.getInputStream();
            commonFileProcess(newName, is,realPath);
            return newName;
        }
        return null;
    }

    private void commonFileProcess(String filename, InputStream is,String realPath) {
        BufferedOutputStream fos = null;
        try {
//                String s1 = getClass().getResource("/").getPath() + filename;
            logger.info(realPath+"\\"+filename);
            fos =new BufferedOutputStream( new FileOutputStream(new File(realPath+"\\"+filename)));
            int len = 0;
            byte []bytes = new byte[102400];
            while ((len = is.read(bytes)) != -1) {
//                fos.write(b);
                String s = new String(bytes, 0, len,"utf-8");
                fos.write(bytes,0,len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
