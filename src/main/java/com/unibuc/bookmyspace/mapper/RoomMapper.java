package com.unibuc.bookmyspace.mapper;

import com.unibuc.bookmyspace.dto.AddRoomRequest;
import com.unibuc.bookmyspace.entity.Room;
import org.springframework.stereotype.Component;

@Component
public class RoomMapper {
    public Room roomRequestToRoom(AddRoomRequest roomRequest) {
        return new Room(
                roomRequest.getName(),
                roomRequest.getFloor(),
                roomRequest.getCapacity()
        );
    }
}

