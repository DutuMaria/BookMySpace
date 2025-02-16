package com.unibuc.bookmyspace.service;

import com.unibuc.bookmyspace.dto.AddRoomRequest;
import com.unibuc.bookmyspace.entity.Desk;
import com.unibuc.bookmyspace.entity.Room;
import com.unibuc.bookmyspace.exception.RoomAlreadyExistsException;
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
class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private DeskRepository deskRepository;

    @InjectMocks
    private RoomService roomService;

    private Room room;
    private AddRoomRequest addRoomRequest;

    @BeforeEach
    void setUp() {
        room = new Room();
        room.setRoomId(1L);
        room.setName("Conference Room");
        room.setFloor(2L);
        room.setCapacity(10L);

        addRoomRequest = new AddRoomRequest();
        addRoomRequest.setName("Conference Room");
        addRoomRequest.setFloor(2L);
        addRoomRequest.setCapacity(10L);
    }

    @Test
    void createRoom_ShouldThrowException_WhenRoomAlreadyExists() {
        when(roomRepository.findByNameAndFloor(room.getName(), room.getFloor()))
                .thenReturn(Optional.of(room));

        RoomAlreadyExistsException exception = assertThrows(RoomAlreadyExistsException.class,
                () -> roomService.createRoom(addRoomRequest));

        assertEquals("There is already a room with that name on that floor!", exception.getMessage());
    }

    @Test
    void createRoom_ShouldSaveRoom_WhenValid() {
        when(roomRepository.findByNameAndFloor(room.getName(), room.getFloor()))
                .thenReturn(Optional.empty());
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        Room savedRoom = roomService.createRoom(addRoomRequest);

        assertNotNull(savedRoom);
        assertEquals(room.getName(), savedRoom.getName());
        verify(roomRepository, times(1)).save(any(Room.class));
    }

    @Test
    void getRoomById_ShouldThrowException_WhenRoomNotFound() {
        when(roomRepository.findById(room.getRoomId())).thenReturn(Optional.empty());

        RoomNotFoundException exception = assertThrows(RoomNotFoundException.class,
                () -> roomService.getRoomById(room.getRoomId()));

        assertEquals("Room with ID " + room.getRoomId() + " not found", exception.getMessage());
    }

    @Test
    void getRoomById_ShouldReturnRoom_WhenExists() {
        when(roomRepository.findById(room.getRoomId())).thenReturn(Optional.of(room));

        Room foundRoom = roomService.getRoomById(room.getRoomId());

        assertNotNull(foundRoom);
        assertEquals(room.getRoomId(), foundRoom.getRoomId());
    }

    @Test
    void getAllRooms_ShouldReturnListOfRooms() {
        List<Room> rooms = List.of(room);
        when(roomRepository.findAll()).thenReturn(rooms);

        List<Room> result = roomService.getAllRooms();

        assertEquals(1, result.size());
        assertEquals(room, result.get(0));
    }

    @Test
    void getDesksForRoom_ShouldThrowException_WhenRoomNotFound() {
        when(roomRepository.findById(room.getRoomId())).thenReturn(Optional.empty());

        RoomNotFoundException exception = assertThrows(RoomNotFoundException.class,
                () -> roomService.getDesksForRoom(room.getRoomId()));

        assertEquals("Room not found with ID: " + room.getRoomId(), exception.getMessage());
    }

    @Test
    void getDesksForRoom_ShouldReturnDesks_WhenRoomExists() {
        Desk desk = new Desk(1, room);
        List<Desk> desks = List.of(desk);

        when(roomRepository.findById(room.getRoomId())).thenReturn(Optional.of(room));
        when(deskRepository.findByRoom_RoomId(room.getRoomId())).thenReturn(desks);

        List<Desk> result = roomService.getDesksForRoom(room.getRoomId());

        assertEquals(1, result.size());
        assertEquals(desk, result.get(0));
    }

    @Test
    void deleteRoom_ShouldThrowException_WhenRoomNotFound() {
        when(roomRepository.findById(room.getRoomId())).thenReturn(Optional.empty());

        RoomNotFoundException exception = assertThrows(RoomNotFoundException.class,
                () -> roomService.deleteRoom(room.getRoomId()));

        assertEquals("Room with ID " + room.getRoomId() + " not found", exception.getMessage());
    }

    @Test
    void deleteRoom_ShouldDeleteRoom_WhenExists() {
        when(roomRepository.findById(room.getRoomId())).thenReturn(Optional.of(room));
        doNothing().when(roomRepository).delete(room);

        roomService.deleteRoom(room.getRoomId());

        verify(roomRepository, times(1)).delete(room);
    }
}
