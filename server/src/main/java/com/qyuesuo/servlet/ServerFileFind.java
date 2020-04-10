package com.qyuesuo.servlet;

import com.google.gson.Gson;
import com.qyuesuo.pojo.MyFile;
import com.qyuesuo.service.FileService;
import com.qyuesuo.service.Impl.FileServiceImpl;
import org.apache.log4j.Logger;
import top.jfunc.json.impl.JSONArray;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.io.*;
import java.util.List;


@WebServlet("/serverFileFind")
public class ServerFileFind extends HttpServlet {
    private static final Logger logger = Logger.getLogger(ServerFileFind.class);
    FileService fileService = new FileServiceImpl();
    @Override
    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        try {
            request.setCharacterEncoding("utf-8");
            response.setContentType("text/html; charset=UTF-8");
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            //list转换为json
            Gson gson = new Gson();
            List<MyFile> list = fileService.queryTen();
            String myFile_list = gson.toJson(list);
            out.write(myFile_list);
        } catch (IOException e) {
            e.printStackTrace();
            logger.debug(e.getMessage());
        }
    }

    @Override
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }
}
