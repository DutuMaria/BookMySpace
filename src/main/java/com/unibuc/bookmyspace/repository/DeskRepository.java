package com.unibuc.bookmyspace.repository;

import com.unibuc.bookmyspace.entity.Desk;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeskRepository extends JpaRepository<Desk, Long>  {

    boolean existsByRoom_RoomIdAndDeskNumber(Long roomId, @NotNull Integer deskNumber);
}
