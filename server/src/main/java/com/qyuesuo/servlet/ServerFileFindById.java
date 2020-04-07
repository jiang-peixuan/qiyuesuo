package com.qyuesuo.servlet;

import com.qyuesuo.utils.JDBCUtil;
import org.apache.log4j.Logger;
import top.jfunc.json.impl.JSONArray;
import top.jfunc.json.impl.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


@WebServlet("/serverFileFindById")
public class ServerFileFindById extends HttpServlet {
    private static final Logger logger = Logger.getLogger(ServerFileFindById.class);
    @Override
    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=UTF-8");
        response.setContentType("text/html");
        String uuid = request.getParameter("filename");
        PrintWriter out = response.getWriter();
        JSONArray jsonArray  =new JSONArray();
        ResultSet resultSet = null;
       //从数据库读取元数据
        try {
            Statement statement = JDBCUtil.getStatement();
             resultSet = statement.executeQuery("select * from t_file where newName='"+uuid+"'");
            logger.info("select * from t_file where newName='"+uuid+"'");
            while (resultSet.next()){
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
        } finally {
            JDBCUtil.close();
        }
        out.write(jsonArray.toString());
    }

    @Override
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }
}
