package com.qyuesuo.dao;

import com.qyuesuo.pojo.MyFile;
import com.qyuesuo.utils.JDBCUtil;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class FileDao {
    private static Logger logger = Logger.getLogger(FileDao.class);

    public void save(MyFile myFile) {
        String sql = "INSERT INTO t_file(oldName,newName,ext,path,size,digitalEnvelope) " +
                "VALUES ('" + myFile.getOldName() + "','" + myFile.getNewName() + "','" + myFile.getExt() + "','" + myFile.getPath() +
                "','" + myFile.getSize() + "','" + myFile.getDigitalEnvelope() + "')";
        try {
            Statement statement = JDBCUtil.getStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtil.close();
        }
    }
    
    public List<MyFile> queryTen(){
        java.util.Date uploadTime = null;
        String format= null;
        List<MyFile> list = new ArrayList();
        ResultSet resultSet = null;
        String sql = "select * from t_file ORDER BY uploadTime DESC";
        try {
            Statement statement = JDBCUtil.getStatement();
            resultSet = statement.executeQuery(sql);
            for (int i = 0; i < 10&&resultSet.next(); i++) {
                //oldName,newName,ext,path,size,digitalEnvelope
                String oldName = resultSet.getString("oldName");
                String newName = resultSet.getString("newName");
                String ext = resultSet.getString("ext");
                String path = resultSet.getString("path");
                String size = resultSet.getString("size");
                String digitalEnvelope = resultSet.getString("digitalEnvelope");
                String date = resultSet.getString("uploadTime");
                System.out.println(date);
                SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:SSS");
                try {
                  uploadTime = simpleDateFormat.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                MyFile myFile = new MyFile(oldName,newName,ext,path,size,uploadTime,digitalEnvelope);
                list.add(myFile);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        finally {
            JDBCUtil.close(resultSet);
        }
        return list;
    }


    @Test
    public MyFile findById(String uuid){
       ResultSet resultSet = null;
        MyFile myFile =null;
        try {
            Statement statement = JDBCUtil.getStatement();
            resultSet = statement.executeQuery("select * from t_file where newName='"+uuid+"'");
            while (resultSet.next()){
                String oldName = resultSet.getString("oldName");
                String newName = resultSet.getString("newName");
                String ext = resultSet.getString("ext");
                String path = resultSet.getString("path");
                String size = resultSet.getString("size");
                String digitalEnvelope = resultSet.getString("digitalEnvelope");
                String date = resultSet.getString("uploadTime");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SSS");
                java.util.Date parse = simpleDateFormat.parse(date);
                myFile = new MyFile(oldName,newName,ext,path,size,parse,digitalEnvelope);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(resultSet);
        }
        return myFile;
    }
    
    
}
