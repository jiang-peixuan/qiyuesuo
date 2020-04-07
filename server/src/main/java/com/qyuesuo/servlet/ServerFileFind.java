package com.qyuesuo.servlet;

import com.alibaba.fastjson.support.odps.CodecCheck;
import com.qyuesuo.pojo.MyFile;
import com.qyuesuo.utils.JDBCUtil;
import jdk.nashorn.internal.runtime.JSONListAdapter;
import org.apache.log4j.Logger;
import org.eclipse.jetty.util.ajax.JSON;
import top.jfunc.json.impl.JSONArray;
import top.jfunc.json.impl.JSONObject;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.io.*;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


@WebServlet("/serverFileFind")
public class ServerFileFind extends HttpServlet {
    private static final Logger logger = Logger.getLogger(ServerFileFind.class);
    @Override
    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        JSONArray jsonArray  =new JSONArray();
        ResultSet resultSet = null;
       //从数据库读取元数据
        try {
            Statement statement = JDBCUtil.getStatement();
             resultSet = statement.executeQuery("select * from t_file ORDER BY uploadTime DESC");
            for (int i = 0; i < 10&&resultSet.next(); i++) {
                //oldName,newName,ext,path,size
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("oldNname",resultSet.getString("oldName"));
                jsonObject.put("newName",resultSet.getString("newName"));
                jsonObject.put("ext",resultSet.getString("ext"));
                jsonObject.put("path",resultSet.getString("path"));
                jsonObject.put("size",resultSet.getString("size"));
                jsonArray.put(jsonObject);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.info("insert into t_file(oldName,newName,ext,path,size)VALUES(oldName,newName,ext,realPath,fileSize)");
        } finally {
            JDBCUtil.close();
        }
        out.write(jsonArray.toString());
    }

    @Override
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }
}
