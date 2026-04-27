package com.movie.ticket.booking.dto;

public class BookingCreatedEvent {
    private Long bookingId;
    private Long userId;
    private Long movieId;
    private Double amount;

    public BookingCreatedEvent() {}

    public BookingCreatedEvent(Long bookingId, Long userId, Long movieId, Double amount) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.movieId = movieId;
        this.amount = amount;
    }

    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getMovieId() { return movieId; }
    public void setMovieId(Long movieId) { this.movieId = movieId; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
}
