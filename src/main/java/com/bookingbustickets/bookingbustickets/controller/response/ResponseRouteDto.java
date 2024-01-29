package com.bookingbustickets.bookingbustickets.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseRouteDto {

    private Long id;

    private float basePrice;

    private BigDecimal totalDistance;

    private long startPlaceId;

    private long endPlaceId;

}
