package com.bookingbustickets.bookingbustickets.domain.repository;

import com.bookingbustickets.bookingbustickets.domain.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
