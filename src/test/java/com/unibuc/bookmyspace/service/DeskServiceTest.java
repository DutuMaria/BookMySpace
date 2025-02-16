package com.unibuc.bookmyspace.service;

import com.unibuc.bookmyspace.entity.Desk;
import com.unibuc.bookmyspace.entity.Room;
import com.unibuc.bookmyspace.exception.DeskAlreadyExistsException;
import com.unibuc.bookmyspace.exception.DeskNotFoundException;
import com.unibuc.bookmyspace.exception.RoomCapacityExceededException;
import com.unibuc.bookmyspace.exception.RoomNotFoundException;
import com.unibuc.bookmyspace.repository.DeskRepository;
import com.unibuc.bookmyspace.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeskServiceTest {

    @Mock
    private DeskRepository deskRepository;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private DeskService deskService;

    private Room room;
    private Desk desk;

    @BeforeEach
    void setUp() {
        room = new Room();
        room.setRoomId(1L);
        room.setCapacity(5L);

        desk = new Desk(1, room);
        desk.setDeskId(1L);
    }

    @Test
    void addDesk_ShouldThrowException_WhenRoomNotFound() {
        when(roomRepository.findById(room.getRoomId())).thenReturn(Optional.empty());

        RoomNotFoundException exception = assertThrows(RoomNotFoundException.class,
                () -> deskService.addDesk(room.getRoomId(), 1));

        assertEquals("Room with ID " + room.getRoomId() + " not found", exception.getMessage());
    }

    @Test
    void addDesk_ShouldThrowException_WhenDeskAlreadyExists() {
        when(roomRepository.findById(room.getRoomId())).thenReturn(Optional.of(room));
        when(deskRepository.existsByRoom_RoomIdAndDeskNumber(room.getRoomId(), desk.getDeskNumber()))
                .thenReturn(true);

        DeskAlreadyExistsException exception = assertThrows(DeskAlreadyExistsException.class,
                () -> deskService.addDesk(room.getRoomId(), desk.getDeskNumber()));

        assertEquals("Desk with number " + desk.getDeskNumber() + " already exists in room with ID " + room.getRoomId(), exception.getMessage());
    }

    @Test
    void addDesk_ShouldThrowException_WhenRoomCapacityExceeded() {
        when(roomRepository.findById(room.getRoomId())).thenReturn(Optional.of(room));
        when(deskRepository.existsByRoom_RoomIdAndDeskNumber(room.getRoomId(), desk.getDeskNumber()))
                .thenReturn(false);
        when(deskRepository.countDeskByRoom_RoomId(room.getRoomId())).thenReturn(5);

        RoomCapacityExceededException exception = assertThrows(RoomCapacityExceededException.class,
                () -> deskService.addDesk(room.getRoomId(), desk.getDeskNumber()));

        assertEquals("Cannot add desk. Room capacity (" + room.getCapacity() + ") is already reached.", exception.getMessage());
    }

    @Test
    void addDesk_ShouldSaveDesk_WhenValid() {
        when(roomRepository.findById(room.getRoomId())).thenReturn(Optional.of(room));
        when(deskRepository.existsByRoom_RoomIdAndDeskNumber(room.getRoomId(), desk.getDeskNumber()))
                .thenReturn(false);
        when(deskRepository.countDeskByRoom_RoomId(room.getRoomId())).thenReturn(4);
        when(deskRepository.save(any(Desk.class))).thenReturn(desk);

        Desk savedDesk = deskService.addDesk(room.getRoomId(), desk.getDeskNumber());

        assertNotNull(savedDesk);
        assertEquals(desk.getDeskNumber(), savedDesk.getDeskNumber());
        verify(deskRepository, times(1)).save(any(Desk.class));
    }

    @Test
    void getAllDesks_ShouldReturnListOfDesks() {
        List<Desk> desks = List.of(desk);
        when(deskRepository.findAll()).thenReturn(desks);

        List<Desk> result = deskService.getAllDesks();

        assertEquals(1, result.size());
        assertEquals(desk, result.get(0));
    }

    @Test
    void getDeskById_ShouldThrowException_WhenDeskNotFound() {
        when(deskRepository.findById(desk.getDeskId())).thenReturn(Optional.empty());

        DeskNotFoundException exception = assertThrows(DeskNotFoundException.class,
                () -> deskService.getDeskById(desk.getDeskId()));

        assertEquals("Desk with ID " + desk.getDeskId() + " not found", exception.getMessage());
    }

    @Test
    void getDeskById_ShouldReturnDesk_WhenExists() {
        when(deskRepository.findById(desk.getDeskId())).thenReturn(Optional.of(desk));

        Desk foundDesk = deskService.getDeskById(desk.getDeskId());

        assertNotNull(foundDesk);
        assertEquals(desk.getDeskId(), foundDesk.getDeskId());
    }

    @Test
    void deleteDesk_ShouldThrowException_WhenDeskNotFound() {
        when(deskRepository.existsById(desk.getDeskId())).thenReturn(false);

        DeskNotFoundException exception = assertThrows(DeskNotFoundException.class,
                () -> deskService.deleteDesk(desk.getDeskId()));

        assertEquals("Desk with ID " + desk.getDeskId() + " not found", exception.getMessage());
    }

    @Test
    void deleteDesk_ShouldDeleteDesk_WhenExists() {
        when(deskRepository.existsById(desk.getDeskId())).thenReturn(true);
        doNothing().when(deskRepository).deleteById(desk.getDeskId());

        deskService.deleteDesk(desk.getDeskId());

        verify(deskRepository, times(1)).deleteById(desk.getDeskId());
    }
}
