package com.qyuesuo.servlet;

import com.google.gson.Gson;
import com.qyuesuo.pojo.MyFile;
import com.qyuesuo.service.FileService;
import com.qyuesuo.service.Impl.FileServiceImpl;
import org.apache.log4j.Logger;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet("/serverFileFindById")
public class ServerFileFindById extends HttpServlet {
    private static final Logger logger = Logger.getLogger(ServerFileFindById.class);
    FileService fileService = new FileServiceImpl();
    @Override
    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        String uuid = request.getParameter("filename");
        Gson gson = new Gson();
        PrintWriter out = response.getWriter();
       //从数据库读取元数据
        MyFile myFile = fileService.fileFind(uuid);
        String myFile_json = gson.toJson(myFile);
        out.write(myFile_json);
    }

    @Override
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }
}
