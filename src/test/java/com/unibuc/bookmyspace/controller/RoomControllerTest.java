package com.unibuc.bookmyspace.controller;

import com.unibuc.bookmyspace.dto.AddRoomRequest;
import com.unibuc.bookmyspace.entity.Desk;
import com.unibuc.bookmyspace.entity.Room;
import com.unibuc.bookmyspace.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomControllerTest {

    @Mock
    private RoomService roomService;

    @InjectMocks
    private RoomController roomController;

    private Room room;
    private AddRoomRequest addRoomRequest;
    private Desk desk;

    @BeforeEach
    void setUp() {
        room = new Room();
        room.setRoomId(1L);
        room.setName("Conference Room");

        addRoomRequest = new AddRoomRequest();
        addRoomRequest.setName("Conference Room");
        addRoomRequest.setFloor(1L);

        desk = new Desk();
        desk.setDeskId(1L);
    }

    @Test
    void testCreateRoom() {
        when(roomService.createRoom(any(AddRoomRequest.class))).thenReturn(room);

        ResponseEntity<Room> response = roomController.create(addRoomRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(room.getName(), response.getBody().getName());
    }

    @Test
    void testGetRoomById() {
        when(roomService.getRoomById(1L)).thenReturn(room);

        ResponseEntity<Room> response = roomController.getById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getRoomId());
    }

    @Test
    void testGetAllRooms() {
        when(roomService.getAllRooms()).thenReturn(Collections.singletonList(room));

        ResponseEntity<List<Room>> response = roomController.getAllRooms();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void testGetDesksForRoom_Success() {
        when(roomService.getDesksForRoom(1L)).thenReturn(Collections.singletonList(desk));

        ResponseEntity<List<Desk>> response = roomController.getDesksForRoom(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void testGetDesksForRoom_NotFound() {
        when(roomService.getDesksForRoom(1L)).thenReturn(List.of());

        ResponseEntity<List<Desk>> response = roomController.getDesksForRoom(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testDeleteRoom() {
        doNothing().when(roomService).deleteRoom(1L);

        ResponseEntity<?> response = roomController.deleteRoom(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
