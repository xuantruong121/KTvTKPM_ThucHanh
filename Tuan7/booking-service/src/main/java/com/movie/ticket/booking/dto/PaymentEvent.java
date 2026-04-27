package com.movie.ticket.booking.dto;

public class PaymentEvent {
    private Long bookingId;
    private String status;

    public PaymentEvent() {}

    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
