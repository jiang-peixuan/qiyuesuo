package com.qyuesuo.dao;

import com.qyuesuo.pojo.MyFile;
import com.qyuesuo.utils.JDBCUtil;
import org.apache.log4j.Logger;

import java.sql.*;
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
                Date uploadTime = new Date(Long.valueOf(date));
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
                Date uploadTime = new Date(Long.valueOf(date));
                 myFile = new MyFile(oldName,newName,ext,path,size,uploadTime,digitalEnvelope);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }finally {
            JDBCUtil.close(resultSet);
        }
        return myFile;
    }
    
    
}
