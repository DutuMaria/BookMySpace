package com.unibuc.bookmyspace.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Required details for creating a reservation")
public class AddReservationRequest {

    @NotNull(message = "User ID cannot be null or empty")
    @Schema(description = "User ID", required = true)
    private Long userId;

    @NotNull(message = "Desk ID cannot be null")
    @Schema(description = "Desk ID", required = true)
    private Long deskId;

    @NotNull(message = "Reservation date cannot be null")
    @Schema(description = "Reservation date", required = true)
    private LocalDate date;
}
