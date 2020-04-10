package com.qyuesuo.service;

import com.qyuesuo.pojo.MyFile;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.List;

public interface FileService {

    String fileUpload(HttpServletRequest request);
    InputStream fileDownLoad(String id);
    MyFile fileFind(String id);
    List<MyFile> queryTen();
}
