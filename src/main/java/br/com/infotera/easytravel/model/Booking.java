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
public class Booking extends Integration {
    
    private Integer fileId;
    private String searchId;
    private Date bookingDate;
    private String bookingCancelationDate;
    private Date startDate;
    private Date endDate;
    private ServiceType serviceType;
    private Currency currency;
    private Double priceRate;
    private Double priceTotal;
    private Double markup;
    private Location locationFrom;
    private Location locationTo;
    private IntegrationBooking integrationBooking;
    private String operatorDeadlineDate;
    private BookingDetailHotel bookingDetailHotel;
    private BookingDetailService bookingDetailService;
    private List<CancellationPolicy> cancellationPolicy;
    private List<Passenger> passenger;
    private Image image;
    private Boolean voucherSupplier;
    private Boolean voucherSupplierUpload;
    private Status status;

    public Booking() {
    }

    public Booking(Integer fileId, String searchId, Date bookingDate, String bookingCancelationDate, Date startDate, Date endDate, ServiceType serviceType, Currency currency, Double priceRate, Double priceTotal, Double markup, Location locationFrom, Location locationTo, IntegrationBooking integrationBooking, String operatorDeadlineDate, BookingDetailHotel bookingDetailHotel, BookingDetailService bookingDetailService, List<CancellationPolicy> cancellationPolicy, List<Passenger> passenger, Image image, Boolean voucherSupplier, Boolean voucherSupplierUpload, Status status, Integer id, String insertDate, String updateDate, Integer updatePersonId) {
        super(id, insertDate, updateDate, updatePersonId);
        this.fileId = fileId;
        this.searchId = searchId;
        this.bookingDate = bookingDate;
        this.bookingCancelationDate = bookingCancelationDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.serviceType = serviceType;
        this.currency = currency;
        this.priceRate = priceRate;
        this.priceTotal = priceTotal;
        this.markup = markup;
        this.locationFrom = locationFrom;
        this.locationTo = locationTo;
        this.integrationBooking = integrationBooking;
        this.operatorDeadlineDate = operatorDeadlineDate;
        this.bookingDetailHotel = bookingDetailHotel;
        this.bookingDetailService = bookingDetailService;
        this.cancellationPolicy = cancellationPolicy;
        this.passenger = passenger;
        this.image = image;
        this.voucherSupplier = voucherSupplier;
        this.voucherSupplierUpload = voucherSupplierUpload;
        this.status = status;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookingCancelationDate() {
        return bookingCancelationDate;
    }

    public void setBookingCancelationDate(String bookingCancelationDate) {
        this.bookingCancelationDate = bookingCancelationDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Double getPriceRate() {
        return priceRate;
    }

    public void setPriceRate(Double priceRate) {
        this.priceRate = priceRate;
    }

    public Double getPriceTotal() {
        return priceTotal;
    }

    public void setPriceTotal(Double priceTotal) {
        this.priceTotal = priceTotal;
    }

    public Double getMarkup() {
        return markup;
    }

    public void setMarkup(Double markup) {
        this.markup = markup;
    }

    public Location getLocationFrom() {
        return locationFrom;
    }

    public void setLocationFrom(Location locationFrom) {
        this.locationFrom = locationFrom;
    }

    public Location getLocationTo() {
        return locationTo;
    }

    public void setLocationTo(Location locationTo) {
        this.locationTo = locationTo;
    }

    public IntegrationBooking getIntegrationBooking() {
        return integrationBooking;
    }

    public void setIntegrationBooking(IntegrationBooking integrationBooking) {
        this.integrationBooking = integrationBooking;
    }

    public String getOperatorDeadlineDate() {
        return operatorDeadlineDate;
    }

    public void setOperatorDeadlineDate(String operatorDeadlineDate) {
        this.operatorDeadlineDate = operatorDeadlineDate;
    }

    public BookingDetailHotel getBookingDetailHotel() {
        return bookingDetailHotel;
    }

    public void setBookingDetailHotel(BookingDetailHotel bookingDetailHotel) {
        this.bookingDetailHotel = bookingDetailHotel;
    }

    public BookingDetailService getBookingDetailService() {
        return bookingDetailService;
    }

    public void setBookingDetailService(BookingDetailService bookingDetailService) {
        this.bookingDetailService = bookingDetailService;
    }

    public List<Passenger> getPassenger() {
        return passenger;
    }

    public void setPassenger(List<Passenger> passenger) {
        this.passenger = passenger;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public boolean isVoucherSupplier() {
        return voucherSupplier;
    }

    public void setVoucherSupplier(boolean voucherSupplier) {
        this.voucherSupplier = voucherSupplier;
    }

    public boolean isVoucherSupplierUpload() {
        return voucherSupplierUpload;
    }

    public void setVoucherSupplierUpload(boolean voucherSupplierUpload) {
        this.voucherSupplierUpload = voucherSupplierUpload;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<CancellationPolicy> getCancellationPolicy() {
        return cancellationPolicy;
    }

    public void setCancellationPolicy(List<CancellationPolicy> cancellationPolicy) {
        this.cancellationPolicy = cancellationPolicy;
    }
    
}
