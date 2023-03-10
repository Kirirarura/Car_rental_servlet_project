package com.pavlenko.kyrylo.model.entity;

import com.pavlenko.kyrylo.model.entity.builder.BookingBuilder;
import com.pavlenko.kyrylo.model.dto.BookingDto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Booking implements Serializable {
    private Long id;
    private User user;
    private BookingStatus bookingStatus;
    private Car car;
    private String userDetails;
    private boolean withDriver;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal price;
    private String declineInfo;
    private BigDecimal additionalFee;

    public Booking() {
    }

    public Booking(BookingDto bookingDto) {
        this.user = bookingDto.getUser();
        this.car = bookingDto.getCar();
        this.userDetails = bookingDto.getUserDetails();
        this.withDriver = bookingDto.isWithDriver();
        this.startDate = LocalDate.parse(bookingDto.getStartDate());
        this.endDate = LocalDate.parse(bookingDto.getEndDate());
        this.price = bookingDto.getPrice();
    }

    public Booking(Long id, User user, BookingStatus bookingStatus, Car car, String userDetails,
                   boolean withDriver, LocalDate startDate, LocalDate endDate,
                   BigDecimal price, String declineInfo, BigDecimal additionalFee) {
        this.id = id;
        this.user = user;
        this.bookingStatus = bookingStatus;
        this.car = car;
        this.userDetails = userDetails;
        this.withDriver = withDriver;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.declineInfo = declineInfo;
        this.additionalFee = additionalFee;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public String getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(String userDetails) {
        this.userDetails = userDetails;
    }

    public boolean isWithDriver() {
        return withDriver;
    }

    public void setWithDriver(boolean withDriver) {
        this.withDriver = withDriver;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }


    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDeclineInfo() {
        return declineInfo;
    }

    public void setDeclineInfo(String declineInfo) {
        this.declineInfo = declineInfo;
    }

    public BigDecimal getAdditionalFee() {
        return additionalFee;
    }

    public void setAdditionalFee(BigDecimal additionalFee) {
        this.additionalFee = additionalFee;
    }

    public static BookingBuilder builder() {
        return new BookingBuilder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return isWithDriver() == booking.isWithDriver() && Objects.equals(getId(), booking.getId()) && Objects.equals(getUser(), booking.getUser()) && Objects.equals(getBookingStatus(), booking.getBookingStatus()) && Objects.equals(getCar(), booking.getCar()) && Objects.equals(getUserDetails(), booking.getUserDetails()) && Objects.equals(getStartDate(), booking.getStartDate()) && Objects.equals(getEndDate(), booking.getEndDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUser(), getBookingStatus(), getCar(), getUserDetails(), isWithDriver(), getStartDate(), getEndDate());
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", user=" + user +
                ", bookingStatus=" + bookingStatus +
                ", car=" + car +
                ", userDetails='" + userDetails + '\'' +
                ", withDriver=" + withDriver +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", price=" + price +
                ", declineInfo='" + declineInfo + '\'' +
                ", additionalFee=" + additionalFee +
                '}';
    }
}
