package com.unibuc.bookmyspace.mapper;

import com.unibuc.bookmyspace.dto.AddReservationRequest;
import com.unibuc.bookmyspace.entity.AppUser;
import com.unibuc.bookmyspace.entity.Desk;
import com.unibuc.bookmyspace.entity.Reservation;
import com.unibuc.bookmyspace.entity.ReservationStatus;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {
    public Reservation reservationRequestToReservation(AddReservationRequest addReservationRequest, AppUser appUser, Desk desk) {
        return new Reservation(
                addReservationRequest.getDate(),
                desk,
                appUser,
                ReservationStatus.ACTIVE
        );
    }
}
