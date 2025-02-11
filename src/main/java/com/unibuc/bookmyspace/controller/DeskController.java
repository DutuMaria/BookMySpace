package com.unibuc.bookmyspace.controller;

import com.unibuc.bookmyspace.entity.Desk;
import com.unibuc.bookmyspace.service.DeskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/desk")
@Tag(name = "Desk", description = "Controller for managing desks")
public class DeskController {

    private final DeskService deskService;

    public DeskController(DeskService deskService) {
        this.deskService = deskService;
    }

    @PostMapping("/addDesk/{roomId}")
    @Operation(summary = "Add a new desk", description = "Creates a new desk in the specified room")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The desk has been successfully created!"),
            @ApiResponse(responseCode = "400", description = "Invalid input data!")
    })
    public ResponseEntity<Desk> addDesk(@PathVariable("roomId") @Parameter(description = "ID of the room") Long roomId, @RequestBody @Parameter(description = "Desk data") Desk desk) {
        return new ResponseEntity<>(deskService.addDesk(roomId, desk), HttpStatus.CREATED);
    }

    @GetMapping("getDesk/{id}")
    @Operation(summary = "Get desk by ID", description = "Retrieve desk details by providing its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Desk found"),
            @ApiResponse(responseCode = "404", description = "Desk not found")
    })
    public ResponseEntity<Desk> getDeskById(@PathVariable("id") @Parameter(description = "ID of the desk") Long id) {
        return ResponseEntity.ok(deskService.getDeskById(id));
    }

    @GetMapping("/getAll")
    @Operation(summary = "Get all desks", description = "Retrieve all desks in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A list containing all the desks in the database")
    })
    public ResponseEntity<List<Desk>> getAllDesks() {
        return ResponseEntity.ok(deskService.getAllDesks());
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete a desk", description = "Delete a desk by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Desk successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Desk not found")
    })
    public ResponseEntity<?> deleteDesk(@PathVariable("id") @Parameter(description = "ID of the desk") Long id) {
        deskService.deleteDesk(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

