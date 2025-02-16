package com.unibuc.bookmyspace.controller;

import com.unibuc.bookmyspace.dto.AddReservationRequest;
import com.unibuc.bookmyspace.entity.AppUser;
import com.unibuc.bookmyspace.entity.Desk;
import com.unibuc.bookmyspace.entity.Reservation;
import com.unibuc.bookmyspace.entity.ReservationStatus;
import com.unibuc.bookmyspace.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationControllerTest {

    @InjectMocks
    private ReservationController reservationController;

    @Mock
    private ReservationService reservationService;

    private Reservation reservation;
    private AddReservationRequest addReservationRequest;

    @BeforeEach
    public void setUp() {
        addReservationRequest = new AddReservationRequest();

        Desk desk = new Desk();
        desk.setDeskId(1L);

        AppUser appUser = new AppUser();
        appUser.setUserId(1L);

        reservation = new Reservation();
        reservation.setDesk(desk);
        reservation.setUser(appUser);
        reservation.setDate(LocalDate.now());
        reservation.setStatus(ReservationStatus.ACTIVE);
    }

    @Test
    public void testCreateReservation() {
        when(reservationService.createReservation(any(AddReservationRequest.class))).thenReturn(reservation);

        ResponseEntity<?> response = reservationController.createReservation(addReservationRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(reservation, response.getBody());
        verify(reservationService, times(1)).createReservation(any(AddReservationRequest.class));
    }

    @Test
    public void testGetAllReservationsByDateFound() {
        List<Reservation> reservations = Collections.singletonList(reservation);
        when(reservationService.getAllReservationsByDate(any(LocalDate.class))).thenReturn(reservations);

        ResponseEntity<List<Reservation>> response = reservationController.getAllReservationsByDate(LocalDate.now());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reservations, response.getBody());
        verify(reservationService, times(1)).getAllReservationsByDate(any(LocalDate.class));
    }

    @Test
    public void testGetAllReservationsByDateNotFound() {
        when(reservationService.getAllReservationsByDate(any(LocalDate.class))).thenReturn(List.of());

        ResponseEntity<List<Reservation>> response = reservationController.getAllReservationsByDate(LocalDate.now());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(reservationService, times(1)).getAllReservationsByDate(any(LocalDate.class));
    }

    @Test
    public void testGetReservationsByUserFound() {
        List<Reservation> reservations = Collections.singletonList(reservation);
        when(reservationService.getAllReservationsByUser(anyLong())).thenReturn(reservations);

        ResponseEntity<List<Reservation>> response = reservationController.getReservationsByUser(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reservations, response.getBody());
        verify(reservationService, times(1)).getAllReservationsByUser(anyLong());
    }

    @Test
    public void testGetReservationsByUserNotFound() {
        when(reservationService.getAllReservationsByUser(anyLong())).thenReturn(List.of());

        ResponseEntity<List<Reservation>> response = reservationController.getReservationsByUser(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(reservationService, times(1)).getAllReservationsByUser(anyLong());
    }

    @Test
    public void testCancelReservationSuccess() {
        when(reservationService.cancelReservation(anyLong(), anyLong())).thenReturn(reservation);

        ResponseEntity<Reservation> response = reservationController.cancelReservation(1L, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reservation, response.getBody());
        verify(reservationService, times(1)).cancelReservation(anyLong(), anyLong());
    }

    @Test
    public void testCancelReservationNotFound() {
        when(reservationService.cancelReservation(anyLong(), anyLong())).thenThrow(new RuntimeException("Reservation not found"));

        ResponseEntity<Reservation> response = reservationController.cancelReservation(1L, 1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(reservationService, times(1)).cancelReservation(anyLong(), anyLong());
    }

    @Test
    public void testCancelReservationForbidden() {
        when(reservationService.cancelReservation(anyLong(), anyLong())).thenThrow(new RuntimeException("User is not authorized to cancel this reservation"));

        ResponseEntity<Reservation> response = reservationController.cancelReservation(1L, 1L);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(reservationService, times(1)).cancelReservation(anyLong(), anyLong());
    }
}
