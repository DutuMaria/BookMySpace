package com.unibuc.bookmyspace.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Required details for creating a new room")
public class RoomRequest {

    @NotBlank(message = "Room name cannot be null or empty")
    @Schema(description = "Name of the room", required = true)
    private String name;

    @NotNull(message = "Floor number cannot be null")
    @Schema(description = "Floor number where the room is located", required = true)
    private Long floor;

    @NotNull(message = "Room capacity cannot be null")
    @Schema(description = "Capacity of the room", required = true)
    private Long capacity;
}
