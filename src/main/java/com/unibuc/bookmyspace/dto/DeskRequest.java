package com.unibuc.bookmyspace.dto;

import com.unibuc.bookmyspace.entity.Room;
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

    @NotNull(message = "RoomId cannot be null")
    @Schema(description = "RoomId where the desk will be placed", required = true)
    private Long roomId;
}