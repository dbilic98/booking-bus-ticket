package com.bookingbustickets.bookingbustickets.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TINYINT")
    private Short seatNumber;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "bus_id")
    private Bus bus;

    public Seat(Short seatNumber, Bus bus) {
        this.seatNumber = seatNumber;
        this.bus = bus;
    }
}
