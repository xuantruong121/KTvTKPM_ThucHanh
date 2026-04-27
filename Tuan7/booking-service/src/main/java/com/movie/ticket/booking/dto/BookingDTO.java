package com.movie.ticket.booking.dto;

public class BookingDTO {
    private Long userId;
    private Long movieId;
    private String seatNumber;

    public BookingDTO() {}

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getMovieId() { return movieId; }
    public void setMovieId(Long movieId) { this.movieId = movieId; }
    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }
}
