package com.unibuc.bookmyspace.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Schema(description = "Required details for creating or updating a desk")
public class DeskRequest {

    @NotNull(message = "Desk number cannot be null")
    @Schema(description = "Desk number used to identify the desk in the room", required = true)
    private Integer deskNumber;

    @NotNull(message = "Room ID cannot be null")
    @Schema(description = "ID of the room where the desk will be placed", required = true)
    private Long roomId;
}