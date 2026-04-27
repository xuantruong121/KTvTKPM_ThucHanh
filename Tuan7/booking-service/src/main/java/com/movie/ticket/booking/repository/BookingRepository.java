package com.movie.ticket.booking.repository;

import com.movie.ticket.booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
