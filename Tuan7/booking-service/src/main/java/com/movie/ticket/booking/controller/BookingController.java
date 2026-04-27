package com.movie.ticket.booking.controller;

import com.movie.ticket.booking.dto.BookingDTO;
import com.movie.ticket.booking.entity.Booking;
import com.movie.ticket.booking.service.BookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private static final Logger log = LoggerFactory.getLogger(BookingController.class);
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public Booking create(@RequestBody BookingDTO dto) {
        log.info("REST request to create booking for user: {}", dto.getUserId());
        return bookingService.createBooking(dto);
    }

    @GetMapping
    public List<Booking> getAll() {
        log.info("REST request to get all bookings");
        return bookingService.getAll();
    }
}
