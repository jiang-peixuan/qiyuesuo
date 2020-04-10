package com.qiyuesuo.pojo;

import java.util.Date;

public class MyFile {
    private Integer id;
    private String oldName;
    private String newName;
    private String ext;
    private String path;
    private String size;
    private Date  uploadTime;
    private  String digitalEnvelope;

    public MyFile() {
    }

    public MyFile(String oldName, String newName, String ext, String path, String size) {
        this.oldName = oldName;
        this.newName = newName;
        this.ext = ext;
        this.path = path;
        this.size = size;
    }

    public MyFile(String oldName, String newName, String ext, String path, String size, String digitalEnvelope) {
        this.oldName = oldName;
        this.newName = newName;
        this.ext = ext;
        this.path = path;
        this.size = size;
        this.digitalEnvelope = digitalEnvelope;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }


    public String getDigitalEnvelope() {
        return digitalEnvelope;
    }

    public void setDigitalEnvelope(String digitalEnvelope) {
        this.digitalEnvelope = digitalEnvelope;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    @Override
    public String toString() {
        return "MyFile{" +
                "id=" + id +
                ", oldName='" + oldName + '\'' +
                ", newName='" + newName + '\'' +
                ", ext='" + ext + '\'' +
                ", path='" + path + '\'' +
                ", size='" + size + '\'' +
                ", uploadTime=" + uploadTime +
                ", digitalEnvelope='" + digitalEnvelope + '\'' +
                '}';
    }
}
