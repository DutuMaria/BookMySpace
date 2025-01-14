package com.unibuc.bookmyspace.service;

import com.unibuc.bookmyspace.entity.Desk;
import com.unibuc.bookmyspace.entity.Room;
import com.unibuc.bookmyspace.exception.RoomAlreadyExistsException;
import com.unibuc.bookmyspace.exception.RoomNotFoundException;
import com.unibuc.bookmyspace.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Room createRoom(Room room) {
        roomRepository.findByNameAndFloor(room.getName(), room.getFloor()).ifPresent(existingRoom -> {
            throw new RoomAlreadyExistsException("There is already a room with that name on that floor!");
        });
        return roomRepository.save(room);
    }

    public Room getRoomById(Long roomId) {
        return roomRepository.findById(roomId).orElseThrow(() -> new RoomNotFoundException("Room with ID " + roomId + " not found"));
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public List<Desk> getDesksForRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room not found with ID: " + roomId));
        return new ArrayList<>(room.getDesks());
    }

    public void deleteRoom(Long roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RoomNotFoundException("Room with ID " + roomId + " not found"));
        roomRepository.delete(room);
    }
}
