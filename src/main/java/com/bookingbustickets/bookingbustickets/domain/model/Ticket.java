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
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private float price;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "one_way_route_id ")
    private Route oneWayRoute;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "return_route_id ")
    private Route returnRoute;

    @ManyToOne
    @JoinColumn(name = "passenger_category_id")
    private PassengerCategory passengerCategory;
}