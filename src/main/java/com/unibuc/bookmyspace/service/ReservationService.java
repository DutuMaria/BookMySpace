package com.unibuc.bookmyspace.service;

import com.unibuc.bookmyspace.entity.Desk;
import com.unibuc.bookmyspace.entity.Reservation;
import com.unibuc.bookmyspace.entity.ReservationStatus;
import com.unibuc.bookmyspace.exception.DeskAlreadyBookedException;
import com.unibuc.bookmyspace.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public void checkIfActiveReservationExists(Desk desk, LocalDate date) {
        boolean exists = reservationRepository.existsByDeskAndDateAndStatus(desk, date, ReservationStatus.ACTIVE);
        if (exists) {
            throw new DeskAlreadyBookedException("The desk is already booked for the selected date.");
        }
    }

    public Reservation createReservation(Reservation reservation) {
        reservation.setStatus(ReservationStatus.ACTIVE);
        checkIfActiveReservationExists(reservation.getDesk(), reservation.getDate());
        return reservationRepository.save(reservation);
    }

    public List<Reservation> getAllReservationsByDate(LocalDate date) {
        return reservationRepository.findAllByDate(date);
    }

    public List<Reservation> getAllReservationsByUser(UUID userId) {
        return reservationRepository.findByUser_UserId(userId);
    }

    public Reservation cancelReservation(Long reservationId, UUID userId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        if (!reservation.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("User is not authorized to cancel this reservation");
        }

        reservation.setStatus(ReservationStatus.CANCELED);
        return reservationRepository.save(reservation);
    }
}
