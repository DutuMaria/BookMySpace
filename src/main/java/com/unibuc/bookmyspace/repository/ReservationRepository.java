package com.unibuc.bookmyspace.repository;

import com.unibuc.bookmyspace.entity.Desk;
import com.unibuc.bookmyspace.entity.Reservation;
import com.unibuc.bookmyspace.entity.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsByDeskAndDateAndStatus(Desk desk, LocalDate date, ReservationStatus status);

    List<Reservation> findAllByDate(LocalDate date);

    List<Reservation> findByUser_UserId(UUID userId);
}