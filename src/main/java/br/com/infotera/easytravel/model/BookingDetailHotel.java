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
public class BookingDetailHotel extends Integration {
    
    public Integer bookingId;
    public HotelAddress hotelAddress;
    public HotelPhone hotelPhone;
    public Integer roomId;

    public BookingDetailHotel() {
        super();
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public HotelAddress getHotelAddress() {
        return hotelAddress;
    }

    public void setHotelAddress(HotelAddress hotelAddress) {
        this.hotelAddress = hotelAddress;
    }

    public HotelPhone getHotelPhone() {
        return hotelPhone;
    }

    public void setHotelPhone(HotelPhone hotelPhone) {
        this.hotelPhone = hotelPhone;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

   
}
