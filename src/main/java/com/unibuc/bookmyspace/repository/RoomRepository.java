package com.unibuc.bookmyspace.repository;

import com.unibuc.bookmyspace.entity.Role;
import com.unibuc.bookmyspace.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface RoomRepository  extends JpaRepository<Room, Long> {

    Optional<Room> findByNameAndFloor(String name, Long floor);
}
