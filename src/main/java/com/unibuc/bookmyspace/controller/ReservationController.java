package com.unibuc.bookmyspace.controller;

import com.unibuc.bookmyspace.dto.AddReservationRequest;
import com.unibuc.bookmyspace.entity.Reservation;
import com.unibuc.bookmyspace.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reservation")
@Tag(name = "Reservation", description = "Controller for managing reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/create")
    @Operation(summary = "Create a reservation", description = "Creates a new reservation for a desk. Only one active reservation is allowed for a desk on a specific date.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The reservation has been successfully created!"),
            @ApiResponse(responseCode = "400", description = "A reservation already exists for this desk on the selected date!"),
            @ApiResponse(responseCode = "404", description = "Desk or user not found!"),
            @ApiResponse(responseCode = "409", description = "Conflict with existing active reservation!")
    })
    public ResponseEntity<?> createReservation(@RequestBody @Valid AddReservationRequest addReservationRequest) {
        Reservation createdReservation = reservationService.createReservation(addReservationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReservation);
    }

    @GetMapping("/getAllReservationsByDate/{date}")
    @Operation(summary = "Get all reservations on a specific date", description = "Returns all reservations for a specific date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A list containing all reservations for the specified date"),
            @ApiResponse(responseCode = "404", description = "No reservations found for the specified date")
    })
    public ResponseEntity<List<Reservation>> getAllReservationsByDate(
            @Parameter(description = "Date in format YYYY-MM-DD", example = "2025-02-16")
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<Reservation> reservations = reservationService.getAllReservationsByDate(date);
        if (reservations.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @GetMapping("/getReservationsByUser/{userId}")
    @Operation(summary = "Get all reservations for a specific user", description = "Returns all reservations for a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A list containing all reservations for the specified user"),
            @ApiResponse(responseCode = "404", description = "No reservations found for the specified user")
    })
    public ResponseEntity<List<Reservation>> getReservationsByUser(
            @PathVariable Long userId) {
        List<Reservation> reservations = reservationService.getAllReservationsByUser(userId);
        if (reservations.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(reservations);
    }

    @Operation(summary = "Cancel a reservation", description = "Cancels the reservation for a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservation successfully canceled"),
            @ApiResponse(responseCode = "404", description = "Reservation not found"),
            @ApiResponse(responseCode = "403", description = "User not authorized to cancel this reservation")
    })
    @PutMapping("/cancelReservation/{reservationId}/{userId}")
    public ResponseEntity<Reservation> cancelReservation(@PathVariable Long reservationId, @PathVariable Long userId) {
        try {
            Reservation cancelledReservation = reservationService.cancelReservation(reservationId, userId);
            return new ResponseEntity<>(cancelledReservation, HttpStatus.OK);
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Reservation not found")) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else if (e.getMessage().equals("User is not authorized to cancel this reservation")) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

