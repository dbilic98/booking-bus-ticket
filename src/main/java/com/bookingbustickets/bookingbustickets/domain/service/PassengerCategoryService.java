package com.bookingbustickets.bookingbustickets.domain.service;

import com.bookingbustickets.bookingbustickets.controller.request.RequestPassengerCategoryDto;
import com.bookingbustickets.bookingbustickets.domain.model.PassengerCategory;
import com.bookingbustickets.bookingbustickets.domain.repository.PassengerCategoryRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PassengerCategoryService {

    private final PassengerCategoryRepository passengerCategoryRepository;

    public PassengerCategoryService(PassengerCategoryRepository passengerCategoryRepository) {
        this.passengerCategoryRepository = passengerCategoryRepository;
    }

    public PassengerCategory findPassengerCategoryById(Long id) {
        Optional<PassengerCategory> optionalPassengerCategory = passengerCategoryRepository.findById(id);
        if (optionalPassengerCategory.isEmpty()) {
            throw new RuntimeException("Passenger Category with ID " + id + " does not exist");
        }
        return optionalPassengerCategory.get();
    }

    public PassengerCategory createPassengerCategory(RequestPassengerCategoryDto requestPassengerCategoryDto) {
        PassengerCategory createdPassengerCategory = new PassengerCategory(requestPassengerCategoryDto.getCategoryName(), requestPassengerCategoryDto.getDiscountPercentage());
        return passengerCategoryRepository.save(createdPassengerCategory);
    }

    public PassengerCategory updatePassengerCategory(Long id, RequestPassengerCategoryDto requestPassengerCategoryDto) {
        PassengerCategory passengerCategoryToUpdate = findPassengerCategoryById(id);
        passengerCategoryToUpdate.setCategoryName(requestPassengerCategoryDto.getCategoryName());
        passengerCategoryToUpdate.setDiscountPercentage(requestPassengerCategoryDto.getDiscountPercentage());
        return passengerCategoryRepository.save(passengerCategoryToUpdate);
    }

    public void deletePassengerCategory(Long id) {
        try {
            passengerCategoryRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("Passenger Category with id " + id + "does not exist");
        }
    }
}
