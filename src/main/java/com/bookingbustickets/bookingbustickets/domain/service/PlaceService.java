package com.bookingbustickets.bookingbustickets.domain.service;

import com.bookingbustickets.bookingbustickets.controller.request.RequestPlaceDto;
import com.bookingbustickets.bookingbustickets.domain.model.Place;
import com.bookingbustickets.bookingbustickets.domain.repository.PlaceRepository;
import com.bookingbustickets.bookingbustickets.exception.PlaceNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlaceService {

    private final PlaceRepository placeRepository;

    public PlaceService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public Place findPlaceById(Long id) {
        Optional<Place> placeOptional = placeRepository.findById(id);
        if (placeOptional.isEmpty()) {
            throw new PlaceNotFoundException("Place with ID " + id + " is not found");
        }
        return placeOptional.get();
    }

    public Place createPlace(RequestPlaceDto requestPlaceDto) {
        Place createdPlace = new Place(requestPlaceDto.getPlaceName());
        return placeRepository.save(createdPlace);
    }

    public Place updatePlace(Long id, RequestPlaceDto requestPlaceDto) {
        Place placeToUpdate = findPlaceById(id);
        placeToUpdate.setPlaceName(requestPlaceDto.getPlaceName());
        return placeRepository.save(placeToUpdate);
    }

    public void deletePlace(Long id) {
        try {
            deletePlace(id);
        } catch (EmptyResultDataAccessException e) {
            throw new PlaceNotFoundException("Place with ID " + id + " is not found");
        }
    }
}
