package com.movie.ticket.booking.entity;

import jakarta.persistence.*;

@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long movieId;
    private String seatNumber;
    private String status; // PENDING, CONFIRMED, FAILED

    public Booking() {}

    public Booking(Long id, Long userId, Long movieId, String seatNumber, String status) {
        this.id = id;
        this.userId = userId;
        this.movieId = movieId;
        this.seatNumber = seatNumber;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getMovieId() { return movieId; }
    public void setMovieId(Long movieId) { this.movieId = movieId; }
    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
