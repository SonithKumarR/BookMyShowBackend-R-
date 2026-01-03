package com.showtime.repository;

import com.showtime.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByBookingId(Long bookingId);
    Optional<Ticket> findByTicketNumber(String ticketNumber);
    List<Ticket> findByBookingUserId(Long userId);
}