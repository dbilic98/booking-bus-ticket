package com.bookingbustickets.bookingbustickets.domain.service;

import com.bookingbustickets.bookingbustickets.controller.request.RequestRouteDto;
import com.bookingbustickets.bookingbustickets.domain.model.Route;
import com.bookingbustickets.bookingbustickets.domain.repository.RouteRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RouteService {

    private RouteRepository routeRepository;

    public RouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    public Route findRouteById(Long id) {
        Optional<Route> optionalRoute = routeRepository.findById(id);
        if (optionalRoute.isEmpty()) {
            throw new RuntimeException("Route with ID" + id + "does not exist");
        }
        return optionalRoute.get();
    }

    public Route createRoute(RequestRouteDto requestRouteDto) {
        Route createdRoute = new Route(requestRouteDto.getStartPoint(), requestRouteDto.getEndPoint(), requestRouteDto.getBasePrice(), requestRouteDto.getTotalDistance());
        return routeRepository.save(createdRoute);
    }

    public Route updateRoute(Long id, RequestRouteDto requestRouteDto) {
        Route updatedRoute = findRouteById(id);
        updatedRoute.setStartPoint(requestRouteDto.getStartPoint());
        updatedRoute.setEndPoint(requestRouteDto.getEndPoint());
        updatedRoute.setBasePrice(requestRouteDto.getBasePrice());
        updatedRoute.setTotalDistance(requestRouteDto.getTotalDistance());
        return routeRepository.save(updatedRoute);
    }

    public void deleteRoute(Long id) {
        try {
            routeRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("Route with ID " + id + "does not exist");
        }
    }
}
