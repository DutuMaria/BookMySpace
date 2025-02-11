package com.unibuc.bookmyspace.controller;

import com.unibuc.bookmyspace.dto.RoomRequest;
import com.unibuc.bookmyspace.entity.Desk;
import com.unibuc.bookmyspace.entity.Room;
import com.unibuc.bookmyspace.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/room")
@Tag(name = "Room", description = "Controller for managing rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/create")
    @Operation(summary = "Create a room", description = "Creates a new room based on the information received in the request's body")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The room has been successfully created!"),
            @ApiResponse(responseCode = "400", description = "A room with the same name already exists on this floor!")
    })
    public ResponseEntity<Room> create(@RequestBody @Valid @Parameter(description = "Room data") RoomRequest room) {
        return new ResponseEntity<>(roomService.createRoom(room), HttpStatus.CREATED);
    }

    @GetMapping("/getById/{id}")
    @Operation(summary = "Get a room by ID", description = "Retrieve information about a room by providing its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Room was found in the database"),
            @ApiResponse(responseCode = "404", description = "Room was NOT found in the database")
    })
    public ResponseEntity<Room> getById(@PathVariable("id") @Parameter(description = "The ID of the room to retrieve") Long id) {
        return ResponseEntity.ok(roomService.getRoomById(id));
    }

    @GetMapping("/getAll")
    @Operation(summary = "Get all rooms", description = "Retrieve a list of all rooms in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A list containing all the rooms in the database")
    })
    public ResponseEntity<List<Room>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    @GetMapping("/getDesks/{roomId}")
    @Operation(summary = "Get all desks for a specific room", description = "Retrieve a list of all desks in a room by providing the room ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A list of desks for the specified room"),
            @ApiResponse(responseCode = "404", description = "Room not found or no desks in this room")
    })
    public ResponseEntity<List<Desk>> getDesksForRoom(@PathVariable("roomId") @Parameter(description = "The ID of the room to retrieve desks for") Long roomId) {
        List<Desk> desks = roomService.getDesksForRoom(roomId);
        if (desks != null && !desks.isEmpty()) {
            return ResponseEntity.ok(desks);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete a room", description = "Delete a room from the database by providing its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Room was successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Room was NOT found in the database")
    })
    public ResponseEntity<?> deleteRoom(@PathVariable("id") @Parameter(description = "The ID of the room to delete") Long id) {
        roomService.deleteRoom(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
