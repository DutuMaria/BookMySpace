package com.unibuc.bookmyspace.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    @NotNull
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "desk_id", nullable = false)
    private Desk desk;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ReservationStatus status;

    public Reservation(LocalDate date, Desk desk, AppUser user, ReservationStatus status) {
        this.date = date;
        this.desk = desk;
        this.user = user;
        this.status = status;
    }
}
