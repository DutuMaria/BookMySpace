package com.unibuc.bookmyspace.service;

import com.unibuc.bookmyspace.dto.AddReservationRequest;
import com.unibuc.bookmyspace.entity.AppUser;
import com.unibuc.bookmyspace.entity.Desk;
import com.unibuc.bookmyspace.entity.Reservation;
import com.unibuc.bookmyspace.entity.ReservationStatus;
import com.unibuc.bookmyspace.exception.DeskAlreadyBookedException;
import com.unibuc.bookmyspace.mapper.ReservationMapper;
import com.unibuc.bookmyspace.repository.DeskRepository;
import com.unibuc.bookmyspace.repository.ReservationRepository;
import com.unibuc.bookmyspace.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final DeskRepository deskRepository;

    private final UserRepository userRepository;

    private final ReservationMapper reservationMapper;

    public ReservationService(ReservationRepository reservationRepository, DeskRepository deskRepository, UserRepository userRepository, ReservationMapper reservationMapper) {
        this.reservationRepository = reservationRepository;
        this.deskRepository = deskRepository;
        this.userRepository = userRepository;
        this.reservationMapper = reservationMapper;
    }

    public void checkIfActiveReservationExists(Long deskId, LocalDate date) {
        boolean exists = reservationRepository.existsByDesk_DeskIdAndDateAndStatus(deskId, date, ReservationStatus.ACTIVE);
        if (exists) {
            throw new DeskAlreadyBookedException("The desk is already booked for the selected date.");
        }
    }

    public Reservation createReservation(AddReservationRequest addReservationRequest) {
        Desk desk = deskRepository.findById(addReservationRequest.getDeskId())
                .orElseThrow(() -> new EntityNotFoundException("Desk not found with ID: " + addReservationRequest.getDeskId()));

        AppUser user = userRepository.findById(addReservationRequest.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + addReservationRequest.getUserId()));

        checkIfActiveReservationExists(addReservationRequest.getDeskId(), addReservationRequest.getDate());

        Reservation reservation = reservationMapper.reservationRequestToReservation(addReservationRequest, user, desk);
        return reservationRepository.save(reservation);
    }

    public List<Reservation> getAllReservationsByDate(LocalDate date) {
        return reservationRepository.findAllByDate(date);
    }

    public List<Reservation> getAllReservationsByUser(Long userId) {
        return reservationRepository.findByUser_UserId(userId);
    }

    public Reservation cancelReservation(Long reservationId, Long userId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        if (!reservation.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("User is not authorized to cancel this reservation");
        }

        reservation.setStatus(ReservationStatus.CANCELED);
        return reservationRepository.save(reservation);
    }
}
