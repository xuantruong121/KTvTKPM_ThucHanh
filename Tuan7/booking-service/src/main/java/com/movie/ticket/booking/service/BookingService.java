package com.movie.ticket.booking.service;

import com.movie.ticket.booking.dto.BookingCreatedEvent;
import com.movie.ticket.booking.dto.BookingDTO;
import com.movie.ticket.booking.dto.PaymentEvent;
import com.movie.ticket.booking.entity.Booking;
import com.movie.ticket.booking.repository.BookingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {
    private static final Logger log = LoggerFactory.getLogger(BookingService.class);
    private final BookingRepository bookingRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public BookingService(BookingRepository bookingRepository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.bookingRepository = bookingRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public List<Booking> getAll() {
        return bookingRepository.findAll();
    }

    public Booking createBooking(BookingDTO dto) {
        log.info("Creating booking for user {}", dto.getUserId());
        Booking booking = new Booking(null, dto.getUserId(), dto.getMovieId(), dto.getSeatNumber(), "PENDING");
        Booking savedBooking = bookingRepository.save(booking);

        log.info("Booking created with ID {}", savedBooking.getId());

        // Publish event
        BookingCreatedEvent event = new BookingCreatedEvent(savedBooking.getId(), savedBooking.getUserId(),
                savedBooking.getMovieId(), 10.0);
        kafkaTemplate.send("BOOKING_CREATED", event);

        return savedBooking;
    }

    @KafkaListener(topics = "PAYMENT_COMPLETED", groupId = "booking-group")
    public void handlePaymentSuccess(PaymentEvent event) {
        log.info("Payment success for booking {}", event.getBookingId());
        bookingRepository.findById(event.getBookingId()).ifPresent(b -> {
            b.setStatus("CONFIRMED");
            bookingRepository.save(b);
        });
    }

    @KafkaListener(topics = "BOOKING_FAILED", groupId = "booking-group")
    public void handlePaymentFailure(PaymentEvent event) {
        log.info("Payment failure for booking {}", event.getBookingId());
        bookingRepository.findById(event.getBookingId()).ifPresent(b -> {
            b.setStatus("FAILED");
            bookingRepository.save(b);
        });
    }
}
