package com.qyuesuo.servlet;

import com.qyuesuo.utils.JDBCUtil;
import org.apache.log4j.Logger;

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


@WebServlet("/ServerDownLoad")
public class ServerFileDownload extends HttpServlet {
    @Override
    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
       //1. 获取要下载的文件名
        String uuid = request.getHeader("UUID");
        //根据UUID获取该文件的存储位置
        Statement statement = JDBCUtil.getStatement();
        String path=null;
        BufferedInputStream bufferedInputStream=null;
        ServletOutputStream outputStream=null;
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM t_file where UUID = 'uuid'");
            while (resultSet.next()){
                 path = resultSet.getString("path");
            }
            if(path!=null){
                File file = new File(path);
                /*获得文件名，并采用UTF-8编码方式进行编码，以解决中文问题*/
                String filename = URLEncoder.encode(file.getName(), "UTF-8");
                //设置文件的类型
                String mimeType = this.getServletContext().getMimeType(filename);
                response.setContentType(mimeType);
                //设置http头信息中 的内容
                response.addHeader("Content-Disposition","attachment:filename=\"" + filename + "\"" );
                //设置文件的长度
                int fileLength = (int)file.length();
                response.setContentLength(fileLength);
                if(fileLength>0){

                        //创建输入流
                        InputStream inStream = new FileInputStream(file);
                        byte[] buf = new byte[4096];
                        //创建输出流
                        ServletOutputStream servletOS = response.getOutputStream();
                        int readLength;
                        //读取文件内容并写到response的输出流当中
                        while(((readLength = inStream.read(buf))!=-1)){
                            servletOS.write(buf, 0, readLength);
                        }
                        //关闭输入流
                        inStream.close();
                        //刷新输出缓冲
                        servletOS.flush();
                        //关闭输出流
                        servletOS.close();
                }

                bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
               outputStream = response.getOutputStream();
                byte []bytes = new byte[102400];
                int len;
                while ((len=bufferedInputStream.read(bytes))!=-1){
                    outputStream.write(bytes,0,len);
                }
            }else {
                System.out.println("文件不存在~！");
                PrintWriter out = response.getWriter();
                out.println("文件 \"" + path + "\" 不存在");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            outputStream.close();
            bufferedInputStream.close();
        }
        String fileName = request.getParameter("fileName");
        //2.获取servlet上下文
        ServletContext servletContext = getServletContext();
        InputStream resourceAsStream = servletContext.getResourceAsStream(fileName);
    }

    @Override
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }
}
