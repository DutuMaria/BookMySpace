package com.unibuc.bookmyspace.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Set;

@Data
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
public class Desk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deskId;

    @NotNull
    private Integer deskNumber;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id", referencedColumnName = "roomId")
    @NotNull
    private Room room;

    @OneToMany(mappedBy = "desk", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonIgnore
    private Set<Reservation> reservations;

    @OneToMany(mappedBy = "favouriteDesk", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonIgnore
    private Set<AppUser> users;

    public Desk(Integer deskNumber, Room room) {
        this.deskNumber = deskNumber;
        this.room = room;
    }
}
