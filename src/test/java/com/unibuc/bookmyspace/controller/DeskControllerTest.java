package com.unibuc.bookmyspace.controller;

import com.unibuc.bookmyspace.entity.Desk;
import com.unibuc.bookmyspace.service.DeskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeskControllerTest {

    @Mock
    private DeskService deskService;

    @InjectMocks
    private DeskController deskController;

    private Desk desk;

    @BeforeEach
    void setUp() {
        desk = new Desk();
        desk.setDeskId(1L);
    }

    @Test
    void testAddDesk() {
        when(deskService.addDesk(1L, 101)).thenReturn(desk);
        ResponseEntity<Desk> response = deskController.addDesk(1L, 101);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getDeskId());
    }

    @Test
    void testGetDeskById() {
        when(deskService.getDeskById(1L)).thenReturn(desk);
        ResponseEntity<Desk> response = deskController.getDeskById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getDeskId());
    }

    @Test
    void testGetAllDesks() {
        List<Desk> desks = Arrays.asList(desk, new Desk());
        when(deskService.getAllDesks()).thenReturn(desks);
        ResponseEntity<List<Desk>> response = deskController.getAllDesks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testDeleteDesk() {
        doNothing().when(deskService).deleteDesk(1L);
        ResponseEntity<?> response = deskController.deleteDesk(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(deskService, times(1)).deleteDesk(1L);
    }
}
