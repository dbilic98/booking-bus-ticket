package com.bookingbustickets.bookingbustickets.domain.service;

import com.bookingbustickets.bookingbustickets.controller.request.RequestRouteDto;
import com.bookingbustickets.bookingbustickets.domain.model.Place;
import com.bookingbustickets.bookingbustickets.domain.model.Route;
import com.bookingbustickets.bookingbustickets.domain.repository.PlaceRepository;
import com.bookingbustickets.bookingbustickets.domain.repository.RouteRepository;
import com.bookingbustickets.bookingbustickets.exception.PlaceNotFoundException;
import com.bookingbustickets.bookingbustickets.exception.RouteNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RouteService {

    private final RouteRepository routeRepository;

    private final PlaceRepository placeRepository;

    public RouteService(RouteRepository routeRepository, PlaceRepository placeRepository) {
        this.routeRepository = routeRepository;
        this.placeRepository = placeRepository;
    }
    public Page<Route> getAllRoutes(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return routeRepository.findAll(pageable);
    }

    public Page<Route> getAllRoutesByCompany(UUID companyUuid, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return routeRepository.findRoutesByCompanyUuid(companyUuid, pageable);
    }

    public Route findRouteById(Long id) {
        Optional<Route> optionalRoute = routeRepository.findById(id);
        if (optionalRoute.isEmpty()) {
            throw new RouteNotFoundException("Route with ID " + id + " id not found");
        }
        return optionalRoute.get();
    }

    public Route createRoute(RequestRouteDto requestRouteDto) {
        Optional<Place> optionalStartPlace = placeRepository.findById(requestRouteDto.startPlaceId());
        Optional<Place> optionalEndPlace = placeRepository.findById(requestRouteDto.endPlaceId());

        if (optionalStartPlace.isEmpty() || optionalEndPlace.isEmpty()) {
            throw new PlaceNotFoundException("Place with the given ID is not found");
        }
        Route createdRoute = new Route(requestRouteDto.basePrice(), requestRouteDto.totalDistance(), optionalStartPlace.get(), optionalEndPlace.get());
        return routeRepository.save(createdRoute);
    }

    public Route updateRoute(Long id, RequestRouteDto requestRouteDto) {
        Route updatedRoute = findRouteById(id);
        updatedRoute.setBasePrice(requestRouteDto.basePrice());
        updatedRoute.setTotalDistance(requestRouteDto.totalDistance());
        return routeRepository.save(updatedRoute);
    }

    public void deleteRoute(Long id) {
        if (routeRepository.existsById(id)) {
            routeRepository.deleteById(id);
        } else {
            throw new RouteNotFoundException("Route with ID " + id + " id not found");
        }
    }

    public List<Route> findFilteredRoutes(Long startPlaceId, Long endPlaceId, LocalDate scheduleDate) {
        return routeRepository.findRoutesBetweenPlacesAndDate(startPlaceId, endPlaceId, scheduleDate);
    }
}