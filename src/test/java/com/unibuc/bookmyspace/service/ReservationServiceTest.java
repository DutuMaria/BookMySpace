package com.unibuc.bookmyspace.service;

import com.unibuc.bookmyspace.dto.AddReservationRequest;
import com.unibuc.bookmyspace.entity.*;
import com.unibuc.bookmyspace.exception.DeskAlreadyBookedException;
import com.unibuc.bookmyspace.mapper.ReservationMapper;
import com.unibuc.bookmyspace.repository.DeskRepository;
import com.unibuc.bookmyspace.repository.ReservationRepository;
import com.unibuc.bookmyspace.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private DeskRepository deskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReservationMapper reservationMapper;

    @InjectMocks
    private ReservationService reservationService;

    private AddReservationRequest request;
    private Desk desk;
    private AppUser user;
    private Reservation reservation;

    @BeforeEach
    void setUp() {
        desk = new Desk();
        desk.setDeskId(1L);

        user = new AppUser();
        user.setUserId(1L);

        request = new AddReservationRequest();
        request.setDeskId(1L);
        request.setUserId(1L);
        request.setDate(LocalDate.of(2025, 2, 20));

        reservation = new Reservation();
        reservation.setDesk(desk);
        reservation.setUser(user);
        reservation.setDate(request.getDate());
        reservation.setStatus(ReservationStatus.ACTIVE);
    }

    @Test
    void createReservation_ShouldThrowException_WhenDeskNotFound() {
        when(deskRepository.findById(request.getDeskId())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> reservationService.createReservation(request));

        assertEquals("Desk not found with ID: " + request.getDeskId(), exception.getMessage());
    }

    @Test
    void createReservation_ShouldThrowException_WhenUserNotFound() {
        when(deskRepository.findById(request.getDeskId())).thenReturn(Optional.of(desk));
        when(userRepository.findById(request.getUserId())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> reservationService.createReservation(request));

        assertEquals("User not found with ID: " + request.getUserId(), exception.getMessage());
    }

    @Test
    void createReservation_ShouldThrowException_WhenDeskIsAlreadyBooked() {
        when(deskRepository.findById(request.getDeskId())).thenReturn(Optional.of(desk));
        when(userRepository.findById(request.getUserId())).thenReturn(Optional.of(user));
        when(reservationRepository.existsByDesk_DeskIdAndDateAndStatus(request.getDeskId(), request.getDate(), ReservationStatus.ACTIVE))
                .thenReturn(true);

        DeskAlreadyBookedException exception = assertThrows(DeskAlreadyBookedException.class,
                () -> reservationService.createReservation(request));

        assertEquals("The desk is already booked for the selected date.", exception.getMessage());
    }

    @Test
    void createReservation_ShouldSaveReservation_WhenDataIsValid() {
        when(deskRepository.findById(request.getDeskId())).thenReturn(Optional.of(desk));
        when(userRepository.findById(request.getUserId())).thenReturn(Optional.of(user));
        when(reservationRepository.existsByDesk_DeskIdAndDateAndStatus(request.getDeskId(), request.getDate(), ReservationStatus.ACTIVE))
                .thenReturn(false);
        when(reservationMapper.reservationRequestToReservation(request, user, desk)).thenReturn(reservation);
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        Reservation savedReservation = reservationService.createReservation(request);

        assertNotNull(savedReservation);
        assertEquals(ReservationStatus.ACTIVE, savedReservation.getStatus());
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void getAllReservationsByDate_ShouldReturnReservations() {
        LocalDate date = LocalDate.of(2025, 2, 20);
        List<Reservation> reservations = List.of(reservation);

        when(reservationRepository.findAllByDate(date)).thenReturn(reservations);

        List<Reservation> result = reservationService.getAllReservationsByDate(date);

        assertEquals(1, result.size());
        assertEquals(reservation, result.get(0));
    }

    @Test
    void cancelReservation_ShouldThrowException_WhenReservationNotFound() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> reservationService.cancelReservation(1L, 1L));

        assertEquals("Reservation not found", exception.getMessage());
    }

    @Test
    void cancelReservation_ShouldThrowException_WhenUserIsNotAuthorized() {
        AppUser anotherUser = new AppUser();
        anotherUser.setUserId(2L);
        reservation.setUser(anotherUser);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> reservationService.cancelReservation(1L, 1L));

        assertEquals("User is not authorized to cancel this reservation", exception.getMessage());
    }

    @Test
    void cancelReservation_ShouldUpdateStatus_WhenUserIsAuthorized() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        Reservation canceledReservation = reservationService.cancelReservation(1L, 1L);

        assertEquals(ReservationStatus.CANCELED, canceledReservation.getStatus());
        verify(reservationRepository, times(1)).save(reservation);
    }
}
