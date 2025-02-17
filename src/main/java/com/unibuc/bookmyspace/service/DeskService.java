package com.unibuc.bookmyspace.service;

import com.unibuc.bookmyspace.entity.Desk;
import com.unibuc.bookmyspace.entity.Room;
import com.unibuc.bookmyspace.exception.DeskAlreadyExistsException;
import com.unibuc.bookmyspace.exception.DeskNotFoundException;
import com.unibuc.bookmyspace.exception.RoomCapacityExceededException;
import com.unibuc.bookmyspace.exception.RoomNotFoundException;
import com.unibuc.bookmyspace.repository.DeskRepository;
import com.unibuc.bookmyspace.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeskService {

    private final DeskRepository deskRepository;
    private final RoomRepository roomRepository;

    @Autowired
    public DeskService(DeskRepository deskRepository, RoomRepository roomRepository) {
        this.deskRepository = deskRepository;
        this.roomRepository = roomRepository;
    }

    public Desk addDesk(Long roomId, Integer deskNumber) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room with ID " + roomId + " not found"));

        boolean deskExists = deskRepository.existsByRoom_RoomIdAndDeskNumber(roomId, deskNumber);
        if (deskExists) {
            throw new DeskAlreadyExistsException(
                    "Desk with number " + deskNumber + " already exists in room with ID " + roomId);
        }

        Integer nrOfExistingDesksInARoom = deskRepository.countDeskByRoom_RoomId(room.getRoomId());

        if (nrOfExistingDesksInARoom >= room.getCapacity()) {
            throw new RoomCapacityExceededException("Cannot add desk. Room capacity (" + room.getCapacity() + ") is already reached.");
        }

        Desk desk = new Desk(deskNumber, room);
        return deskRepository.save(desk);
    }

    public List<Desk> getAllDesks() {
        return deskRepository.findAll();
    }

    public Desk getDeskById(Long deskId) {
        return deskRepository.findById(deskId)
                .orElseThrow(() -> new DeskNotFoundException("Desk with ID " + deskId + " not found"));
    }

    public void deleteDesk(Long deskId) {
        if (!deskRepository.existsById(deskId)) {
            throw new DeskNotFoundException("Desk with ID " + deskId + " not found");
        }
        deskRepository.deleteById(deskId);
    }
}

