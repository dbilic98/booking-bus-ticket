package com.bookingbustickets.bookingbustickets.domain.service;

import com.bookingbustickets.bookingbustickets.domain.model.Reservation;
import com.bookingbustickets.bookingbustickets.domain.model.User;
import com.bookingbustickets.bookingbustickets.domain.repository.ReservationRepository;
import com.bookingbustickets.bookingbustickets.exception.ExpiredReservationException;
import com.bookingbustickets.bookingbustickets.exception.InvalidReservationException;
import com.bookingbustickets.bookingbustickets.exception.ReservationNotFoundException;
import com.bookingbustickets.bookingbustickets.exception.ReservationStatusException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.bookingbustickets.bookingbustickets.domain.enumeration.ReservationStatus.CONFIRMED;
import static com.bookingbustickets.bookingbustickets.domain.enumeration.ReservationStatus.PENDING;

@Service
public class ReservationService {

    private static final long ALLOWED_MINUTES = 10;
    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Page<Reservation> getAllReservations(int pageNumber, int pageSize){
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return reservationRepository.findAll(pageable);
    }
    public Reservation findReservationById(Long id) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);
        if (optionalReservation.isEmpty()) {
            throw new ReservationNotFoundException("Reservation with ID " + id + " is not found");
        }
        return optionalReservation.get();
    }

  public Page<Reservation> getAllReservationsByCompanyUuid(
      int pageNumber, int pageSize, UUID companyUuid) {
    Pageable pageable = PageRequest.of(pageNumber, pageSize);
    return reservationRepository.findAllByCompanyId(companyUuid, pageable);
  }

  public Page<Reservation> getAllReservationsByUserUuid(
      int pageNumber, int pageSize, UUID userUuid) {
    Pageable pageable = PageRequest.of(pageNumber, pageSize);
    return reservationRepository.findAllByUserUserUuid(userUuid, pageable);
  }

  public Reservation createReservation(User user) {
    Reservation createdReservation = new Reservation(LocalDateTime.now(), PENDING, user);
    return reservationRepository.save(createdReservation);
  }

  public void confirmReservation(Long id) {
        Reservation reservation = findReservationById(id);
        validateReservation(reservation);
        reservation.setStatus(CONFIRMED);
        reservationRepository.save(reservation);
    }

    public void cancelPendingReservations() {
        LocalDateTime thresholdDatetime = LocalDateTime.now().minusMinutes(ALLOWED_MINUTES);
        reservationRepository.cancelPendingReservations(thresholdDatetime);
    }

    public void deleteReservation(Long id) {
        if (reservationRepository.existsById(id)) {
            reservationRepository.deleteById(id);
        } else {
            throw new ReservationNotFoundException("Reservation with ID " + id + " is not found");
        }
    }

    private void validateReservation(Reservation reservation) {
        if (!reservation.hasAnyTickets()) {
            throw new InvalidReservationException("Reservation with ID " + reservation.getId() + " does not have any tickets");
        }
        if (!reservation.isPending()) {
            throw new ReservationStatusException("The reservation status is not pending");
        }
        if (reservation.isOlderThanDefinedThreshold(ALLOWED_MINUTES)) {
            throw new ExpiredReservationException("Reservation is older than " + ALLOWED_MINUTES + " minutes. Can not be confirmed.");
        }
    }
}
