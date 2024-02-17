package com.bookingbustickets.bookingbustickets.controller;

import com.bookingbustickets.bookingbustickets.controller.response.ResponseReservationDto;
import com.bookingbustickets.bookingbustickets.controller.response.ResponseTicketDto;
import com.bookingbustickets.bookingbustickets.domain.model.Reservation;
import com.bookingbustickets.bookingbustickets.domain.model.Ticket;
import com.bookingbustickets.bookingbustickets.domain.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/{id}")
    public ResponseReservationDto findReservationById(@PathVariable Long id) {
        Reservation reservation = reservationService.findReservationById(id);
        List<Ticket> tickets = reservation.getTickets();
        List<ResponseTicketDto> responseTicketDtos = new ArrayList<>();
        for (Ticket ticket : tickets) {
            ResponseTicketDto responseTicketDto = new ResponseTicketDto(
                    ticket.getId(),
                    ticket.getPrice(),
                    ticket.getOneWaySchedule().getId(),
                    ticket.getReturnSchedule() == null ? null : ticket.getReturnSchedule().getId(),
                    ticket.getReservation().getId(),
                    ticket.getOneWayRoute().getId(),
                    ticket.getReturnRoute() == null ? null : ticket.getReturnRoute().getId(),
                    ticket.getPassengerCategory().getId());
            responseTicketDtos.add(responseTicketDto);
        }
        return new ResponseReservationDto(reservation.getId(), reservation.getDateOfReservation(), reservation.getStatus(), responseTicketDtos);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseReservationDto createReservation() {
        Reservation createdReservation = reservationService.createReservation();
        return new ResponseReservationDto(createdReservation.getId(), createdReservation.getDateOfReservation(), createdReservation.getStatus());
    }

    @PutMapping("/confirm/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void confirmReservation(@PathVariable("id") Long id) {
        reservationService.confirmReservation(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReservation(@PathVariable("id") Long id) {
        reservationService.deleteReservation(id);
    }
}


