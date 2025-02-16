package com.unibuc.bookmyspace.repository;

import com.unibuc.bookmyspace.entity.Desk;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeskRepository extends JpaRepository<Desk, Long>  {

    boolean existsByRoom_RoomIdAndDeskNumber(Long roomId, @NotNull Integer deskNumber);

    List<Desk> findByRoom_RoomId(Long roomId);

    Integer countDeskByRoom_RoomId(Long roomId);
}
