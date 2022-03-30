/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model;

import java.util.Date;

/**
 *
 * @author William Dias
 */
public class Image extends Integration {
    
    private String title;
    
    private String url;
    
    private String urlImage;
    
    private ImageType imageType;
    
    public String attachFileDate;
    
    public String fileName;
    
    public String attachFileName;
    
    public String directory;
    
    public String contentType;
    
    public Status status;

    public Image() {
    }

    public Image(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public Image(String title, String url, String urlImage, ImageType imageType, String attachFileDate, String fileName, String attachFileName, String directory, String contentType, Status status, int id, String insertDate, String updateDate, int updatePersonId) {
        super(id, insertDate, updateDate, updatePersonId);
        this.title = title;
        this.url = url;
        this.urlImage = urlImage;
        this.imageType = imageType;
        this.attachFileDate = attachFileDate;
        this.fileName = fileName;
        this.attachFileName = attachFileName;
        this.directory = directory;
        this.contentType = contentType;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public ImageType getImageType() {
        return imageType;
    }

    public void setImageType(ImageType imageType) {
        this.imageType = imageType;
    }

    public String getAttachFileDate() {
        return attachFileDate;
    }

    public void setAttachFileDate(String attachFileDate) {
        this.attachFileDate = attachFileDate;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getAttachFileName() {
        return attachFileName;
    }

    public void setAttachFileName(String attachFileName) {
        this.attachFileName = attachFileName;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    
}
