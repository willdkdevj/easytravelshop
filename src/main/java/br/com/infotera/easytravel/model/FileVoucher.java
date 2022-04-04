/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model;

import java.util.Date;
import java.util.List;

/**
 *
 * @author William Dias
 */
public class FileVoucher extends Integration{
    
    private String companyPhoto;
    private Integer fileId;
    private Integer bookingId;
    private Integer filePackageId;
    private Date bookingDate;
    private String code;
    private String qrCode;
    private String agencyName;
    private String agentName;
    private List<Passenger> passengers;
    private String locationTo;
    private String activityName;
    private String activityType;
    private String activityDescription;
    private Date activityDate;
    private Date activityEndDate;
    private List<String> includes;
    private List<String> notIncludes;
    private String emergencyName;
    private String emergencyPhone;
    private String phone24Hours;

    public FileVoucher() {
    }

    public String getCompanyPhoto() {
        return companyPhoto;
    }

    public void setCompanyPhoto(String companyPhoto) {
        this.companyPhoto = companyPhoto;
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public Integer getFilePackageId() {
        return filePackageId;
    }

    public void setFilePackageId(Integer filePackageId) {
        this.filePackageId = filePackageId;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }

    public String getLocationTo() {
        return locationTo;
    }

    public void setLocationTo(String locationTo) {
        this.locationTo = locationTo;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }

    public Date getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(Date activityDate) {
        this.activityDate = activityDate;
    }

    public Date getActivityEndDate() {
        return activityEndDate;
    }

    public void setActivityEndDate(Date activityEndDate) {
        this.activityEndDate = activityEndDate;
    }

    public List<String> getIncludes() {
        return includes;
    }

    public void setIncludes(List<String> includes) {
        this.includes = includes;
    }

    public List<String> getNotIncludes() {
        return notIncludes;
    }

    public void setNotIncludes(List<String> notIncludes) {
        this.notIncludes = notIncludes;
    }

    public String getEmergencyName() {
        return emergencyName;
    }

    public void setEmergencyName(String emergencyName) {
        this.emergencyName = emergencyName;
    }

    public String getEmergencyPhone() {
        return emergencyPhone;
    }

    public void setEmergencyPhone(String emergencyPhone) {
        this.emergencyPhone = emergencyPhone;
    }

    public String getPhone24Hours() {
        return phone24Hours;
    }

    public void setPhone24Hours(String phone24Hours) {
        this.phone24Hours = phone24Hours;
    }
    
    
}
