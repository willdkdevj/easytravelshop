/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel.model;

import com.google.gson.annotations.SerializedName;
import java.util.Date;
import java.util.List;

/**
 *
 * @author William Dias
 */
public class File extends Integration {
    
    @SerializedName("Id")
    private Integer idReq;
    
    public Integer companyId;
    public String fileDate;
    public String fileDateDeadline;
    public String fileCancelationDate;
    public Person fileCancelationPerson;
    public Currency currency;
    public String startDate;
    public String endDate;
    public Double priceRate;
    public Double priceDiscount;
    public Double priceTax;
    public Double priceCharges;
    public Double priceTotal;
    public Double paymentAddition;
    public Double markup;
    public Person personAgent;
    public List<Booking> bookings;
    public List<Passenger> passengers;
    public List<FileVoucher> fileVoucher;
    public Financial financial;
    public List<FollowUpExecution> followUpExecutions;
    public Double overPrice;
    public Boolean pendingSupplierDeadlineDate;
    public Status status;
    
    @SerializedName("Payments")
    public List<PaymentPlan> payments;
    
    public File() {
        super();
    }

    public File(Integer id){
        this.idReq = id;
    }
    
    public File(Integer idReq, Integer companyId, String fileDate, String fileDateDeadline, String fileCancelationDate, Person fileCancelationPerson, Currency currency, String startDate, String endDate, Double priceRate, Double priceDiscount, Double priceTax, Double priceCharges, Double priceTotal, Double paymentAddition, Double markup, Person personAgent, List<Booking> bookings, List<Passenger> passengers, Financial financial, List<FollowUpExecution> followUpExecutions, Double overPrice, Boolean pendingSupplierDeadlineDate, Status status, Integer id, String insertDate, String updateDate, Integer updatePersonId) {
        super(id, insertDate, updateDate, updatePersonId);
        this.idReq = idReq;
        this.companyId = companyId;
        this.fileDate = fileDate;
        this.fileDateDeadline = fileDateDeadline;
        this.fileCancelationDate = fileCancelationDate;
        this.fileCancelationPerson = fileCancelationPerson;
        this.currency = currency;
        this.startDate = startDate;
        this.endDate = endDate;
        this.priceRate = priceRate;
        this.priceDiscount = priceDiscount;
        this.priceTax = priceTax;
        this.priceCharges = priceCharges;
        this.priceTotal = priceTotal;
        this.paymentAddition = paymentAddition;
        this.markup = markup;
        this.personAgent = personAgent;
        this.bookings = bookings;
        this.passengers = passengers;
        this.financial = financial;
        this.followUpExecutions = followUpExecutions;
        this.overPrice = overPrice;
        this.pendingSupplierDeadlineDate = pendingSupplierDeadlineDate;
        this.status = status;
    }

    public Integer getIdReq() {
        return idReq;
    }

    public void setIdReq(Integer idReq) {
        this.idReq = idReq;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getFileDate() {
        return fileDate;
    }

    public void setFileDate(String fileDate) {
        this.fileDate = fileDate;
    }

    public String getFileDateDeadline() {
        return fileDateDeadline;
    }

    public void setFileDateDeadline(String fileDateDeadline) {
        this.fileDateDeadline = fileDateDeadline;
    }

    public String getFileCancelationDate() {
        return fileCancelationDate;
    }

    public void setFileCancelationDate(String fileCancelationDate) {
        this.fileCancelationDate = fileCancelationDate;
    }

    public Person getFileCancelationPerson() {
        return fileCancelationPerson;
    }

    public void setFileCancelationPerson(Person fileCancelationPerson) {
        this.fileCancelationPerson = fileCancelationPerson;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Double getPriceRate() {
        return priceRate;
    }

    public void setPriceRate(Double priceRate) {
        this.priceRate = priceRate;
    }

    public Double getPriceDiscount() {
        return priceDiscount;
    }

    public void setPriceDiscount(Double priceDiscount) {
        this.priceDiscount = priceDiscount;
    }

    public Double getPriceTax() {
        return priceTax;
    }

    public void setPriceTax(Double priceTax) {
        this.priceTax = priceTax;
    }

    public Double getPriceCharges() {
        return priceCharges;
    }

    public void setPriceCharges(Double priceCharges) {
        this.priceCharges = priceCharges;
    }

    public Double getPriceTotal() {
        return priceTotal;
    }

    public void setPriceTotal(Double priceTotal) {
        this.priceTotal = priceTotal;
    }

    public Double getPaymentAddition() {
        return paymentAddition;
    }

    public void setPaymentAddition(Double paymentAddition) {
        this.paymentAddition = paymentAddition;
    }

    public Double getMarkup() {
        return markup;
    }

    public void setMarkup(Double markup) {
        this.markup = markup;
    }

    public Person getPersonAgent() {
        return personAgent;
    }

    public void setPersonAgent(Person personAgent) {
        this.personAgent = personAgent;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }

    public Financial getFinancial() {
        return financial;
    }

    public void setFinancial(Financial financial) {
        this.financial = financial;
    }

    public List<FollowUpExecution> getFollowUpExecutions() {
        return followUpExecutions;
    }

    public void setFollowUpExecutions(List<FollowUpExecution> followUpExecutions) {
        this.followUpExecutions = followUpExecutions;
    }

    public Double getOverPrice() {
        return overPrice;
    }

    public void setOverPrice(Double overPrice) {
        this.overPrice = overPrice;
    }

    public Boolean isPendingSupplierDeadlineDate() {
        return pendingSupplierDeadlineDate;
    }

    public void setPendingSupplierDeadlineDate(Boolean pendingSupplierDeadlineDate) {
        this.pendingSupplierDeadlineDate = pendingSupplierDeadlineDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<PaymentPlan> getPayments() {
        return payments;
    }

    public void setPayments(List<PaymentPlan> payments) {
        this.payments = payments;
    }

    public List<FileVoucher> getFileVoucher() {
        return fileVoucher;
    }

    public void setFileVoucher(List<FileVoucher> fileVoucher) {
        this.fileVoucher = fileVoucher;
    }
    
}
