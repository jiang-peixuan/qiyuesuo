package com.qyuesuo.service.Impl;


import com.qyuesuo.dao.FileDao;
import com.qyuesuo.pojo.MyFile;
import com.qyuesuo.service.FileService;
import com.qyuesuo.utils.AESCrpyt;
import com.qyuesuo.utils.RSAUtil;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.*;
import java.security.interfaces.RSAPublicKey;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class FileServiceImpl implements FileService {
    private static Logger logger = Logger.getLogger(FileServiceImpl.class);
    private String BASE_PATH = "upload/";
    private FileDao fileDao = new FileDao();
    private String TEMP_PATH = "upload/temp/tmp";

    /**
     * 文件上传
     *
     * @param request
     * @return
     * @throws Exception
     */

    @Override
    public String fileUpload(HttpServletRequest request) {

        MyFile fileInfoModel = new MyFile();
        //
        //从request 中获取文件
        Part part = null;
        InputStream in = null;
        try {
            part = request.getPart("file");
            in = part.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        }


        //获取文件的相关属性
        String cd = part.getHeader("Content-Disposition");
        String[] cds = cd.split(";");
        String oldName = cds[2].substring(cds[2].indexOf("=") + 1).substring(cds[2].lastIndexOf("//") + 1).replace("\"", "");
        long fileSize = part.getSize();
        String uuid = UUID.randomUUID().toString().replaceAll("-","");

        // 文件信息
        String ext = oldName.substring(oldName.lastIndexOf("."));
        String newName = uuid + "." + ext;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String path = BASE_PATH + sdf.format(new Date()) + File.separator + uuid;
        // 将文件存储为临时文件
       saveFile(in, TEMP_PATH);
        //对临时文件进行 AES 加密
        //创建AES key
        String uukey = UUID.randomUUID().toString().replaceAll("-", "");
        AESCrpyt aesCrpyt = new AESCrpyt();
        // 加密后保存新路径
        aesCrpyt.crypt(TEMP_PATH, path, uukey);
        //RSA加密
        String mi = null;
        try {
            RSAPublicKey pubKey = RSAUtil.getPublicKey();
            mi = RSAUtil.encryptByPublicKey(uukey, pubKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //保存fileInfo 信息
        fileInfoModel.setDigitalEnvelope(mi);
        fileInfoModel.setNewName(newName);
        fileInfoModel.setOldName(oldName);
        fileInfoModel.setExt(ext);
        fileInfoModel.setSize(String.valueOf(fileSize));
        fileInfoModel.setPath(path);
        //入库
        fileDao.save(fileInfoModel);
        //删除临时文件
        File file = new File(TEMP_PATH);
        file.delete();
        return fileInfoModel.getNewName();
    }

    /**
     * 文件下载
     *
     * @param uuid
     * @return
     */
    @Override
    public InputStream fileDownLoad(String uuid) {
        InputStream inputStream = null;
        MyFile myFile = new MyFile();
        myFile = fileDao.findById(uuid);
        if (myFile != null) {
            File filePath = new File(myFile.getPath());
            try {
                inputStream = new FileInputStream(filePath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }
        }
        return inputStream;
    }

    /**
     * 文件信息查询
     *
     * @param id
     * @return
     */
    @Override
    public MyFile fileFind(String id) {
        MyFile myFile = fileDao.findById(id);
        return myFile;
    }

    @Override
    public List<MyFile>  queryTen() {
        return fileDao.queryTen();
    }

    public static void saveFile(InputStream inputStream, String path) {

        byte[] bs = new byte[1024];
        int len;
        try {
            File file = new File(path);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
            OutputStream  os = new FileOutputStream(file);
            while ((len = inputStream.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            os.close();
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
