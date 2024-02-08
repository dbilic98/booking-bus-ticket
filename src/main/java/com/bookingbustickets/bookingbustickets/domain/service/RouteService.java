package com.bookingbustickets.bookingbustickets.domain.service;

import com.bookingbustickets.bookingbustickets.controller.request.RequestRouteDto;
import com.bookingbustickets.bookingbustickets.controller.response.ResponseRouteDto;
import com.bookingbustickets.bookingbustickets.domain.model.Place;
import com.bookingbustickets.bookingbustickets.domain.model.Route;
import com.bookingbustickets.bookingbustickets.domain.repository.PlaceRepository;
import com.bookingbustickets.bookingbustickets.domain.repository.RouteRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RouteService {

    private final RouteRepository routeRepository;

    private final PlaceRepository placeRepository;

    public RouteService(RouteRepository routeRepository, PlaceRepository placeRepository) {
        this.routeRepository = routeRepository;
        this.placeRepository = placeRepository;
    }

    public Route findRouteById(Long id) {
        Optional<Route> optionalRoute = routeRepository.findById(id);
        if (optionalRoute.isEmpty()) {
            throw new RuntimeException("Route with ID" + id + "does not exist");
        }
        return optionalRoute.get();
    }

    public Route createRoute(RequestRouteDto requestRouteDto) {
        Optional<Place> optionalStartPlace = placeRepository.findById(requestRouteDto.getStartPlaceId());
        Optional<Place> optionalEndPlace = placeRepository.findById(requestRouteDto.getEndPlaceId());

        if (optionalStartPlace.isEmpty() || optionalEndPlace.isEmpty()) {
            throw new RuntimeException("Place with the given ID is not found");
        }
        Route createdRoute = new Route(requestRouteDto.getBasePrice(), requestRouteDto.getTotalDistance(), optionalStartPlace.get(), optionalEndPlace.get());
        return routeRepository.save(createdRoute);
    }

    public Route updateRoute(Long id, RequestRouteDto requestRouteDto) {
        Route updatedRoute = findRouteById(id);
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

    public List<ResponseRouteDto> findRoutesByStartAndEndPlaceAndScheduleDate(Long startPlaceId, Long endPlaceId, LocalDate scheduleDate) {
        List<Route> routeList = routeRepository.findRoutesBetweenPlacesAndDate(startPlaceId, endPlaceId, scheduleDate);
        List<ResponseRouteDto> responseRouteDtos = new ArrayList<>();

        for (Route route : routeList) {
            ResponseRouteDto routeDto = mapToResponseRouteDto(route);
            responseRouteDtos.add(routeDto);
        }
        return responseRouteDtos;
    }

    private ResponseRouteDto mapToResponseRouteDto(Route route) {
        ResponseRouteDto routeDto = new ResponseRouteDto();
        routeDto.setId(route.getId());
        routeDto.setBasePrice(route.getBasePrice());
        routeDto.setTotalDistance(route.getTotalDistance());
        routeDto.setStartPlaceId(route.getStartPlace().getId());
        routeDto.setEndPlaceId(route.getEndPlace().getId());
        routeDto.setScheduleList(route.getScheduleList());
        return routeDto;
    }
}
