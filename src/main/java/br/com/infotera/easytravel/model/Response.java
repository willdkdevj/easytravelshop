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
public class Response extends Integration {
    
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
    private String cancellationPolicy;
    private List<CancellationPolicy> cancellationPolicies;
    private String activityName;
    private String activityType;
    private String activityDescription;
    private String activityObservation;
    private String activityDuration;
    private Date activityDate;
    private Date activityEndDate;
    private List<String> includes;
    private List<String> notIncludes;
    private List<String> whatToKnow;
    private String emergencyName;
    private String emergencyPhone;
    private String phone24Hours;

    public Response() {
    }

    public Response(String companyPhoto, Integer fileId, Integer bookingId, Integer filePackageId, Date bookingDate, String code, String qrCode, String agencyName, String agentName, List<Passenger> passengers, String locationTo, String cancellationPolicy, List<CancellationPolicy> cancellationPolicies, String activityName, String activityType, String activityDescription, String activityObservation, String activityDuration, Date activityDate, Date activityEndDate, List<String> includes, List<String> notIncludes, List<String> whatToKnow, String emergencyName, String emergencyPhone, String phone24Hours) {
        this.companyPhoto = companyPhoto;
        this.fileId = fileId;
        this.bookingId = bookingId;
        this.filePackageId = filePackageId;
        this.bookingDate = bookingDate;
        this.code = code;
        this.qrCode = qrCode;
        this.agencyName = agencyName;
        this.agentName = agentName;
        this.passengers = passengers;
        this.locationTo = locationTo;
        this.cancellationPolicy = cancellationPolicy;
        this.cancellationPolicies = cancellationPolicies;
        this.activityName = activityName;
        this.activityType = activityType;
        this.activityDescription = activityDescription;
        this.activityObservation = activityObservation;
        this.activityDuration = activityDuration;
        this.activityDate = activityDate;
        this.activityEndDate = activityEndDate;
        this.includes = includes;
        this.notIncludes = notIncludes;
        this.whatToKnow = whatToKnow;
        this.emergencyName = emergencyName;
        this.emergencyPhone = emergencyPhone;
        this.phone24Hours = phone24Hours;
    }

    public Response(String companyPhoto, Integer fileId, Integer bookingId, Integer filePackageId, Date bookingDate, String code, String qrCode, String agencyName, String agentName, List<Passenger> passengers, String locationTo, String cancellationPolicy, List<CancellationPolicy> cancellationPolicies, String activityName, String activityType, String activityDescription, String activityObservation, String activityDuration, Date activityDate, Date activityEndDate, List<String> includes, List<String> notIncludes, List<String> whatToKnow, String emergencyName, String emergencyPhone, String phone24Hours, Integer id, String insertDate, String updateDate, Integer updatePersonId) {
        super(id, insertDate, updateDate, updatePersonId);
        this.companyPhoto = companyPhoto;
        this.fileId = fileId;
        this.bookingId = bookingId;
        this.filePackageId = filePackageId;
        this.bookingDate = bookingDate;
        this.code = code;
        this.qrCode = qrCode;
        this.agencyName = agencyName;
        this.agentName = agentName;
        this.passengers = passengers;
        this.locationTo = locationTo;
        this.cancellationPolicy = cancellationPolicy;
        this.cancellationPolicies = cancellationPolicies;
        this.activityName = activityName;
        this.activityType = activityType;
        this.activityDescription = activityDescription;
        this.activityObservation = activityObservation;
        this.activityDuration = activityDuration;
        this.activityDate = activityDate;
        this.activityEndDate = activityEndDate;
        this.includes = includes;
        this.notIncludes = notIncludes;
        this.whatToKnow = whatToKnow;
        this.emergencyName = emergencyName;
        this.emergencyPhone = emergencyPhone;
        this.phone24Hours = phone24Hours;
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

    public String getCancellationPolicy() {
        return cancellationPolicy;
    }

    public void setCancellationPolicy(String cancellationPolicy) {
        this.cancellationPolicy = cancellationPolicy;
    }

    public List<CancellationPolicy> getCancellationPolicies() {
        return cancellationPolicies;
    }

    public void setCancellationPolicies(List<CancellationPolicy> cancellationPolicies) {
        this.cancellationPolicies = cancellationPolicies;
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

    public String getActivityObservation() {
        return activityObservation;
    }

    public void setActivityObservation(String activityObservation) {
        this.activityObservation = activityObservation;
    }

    public String getActivityDuration() {
        return activityDuration;
    }

    public void setActivityDuration(String activityDuration) {
        this.activityDuration = activityDuration;
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

    public List<String> getWhatToKnow() {
        return whatToKnow;
    }

    public void setWhatToKnow(List<String> whatToKnow) {
        this.whatToKnow = whatToKnow;
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
