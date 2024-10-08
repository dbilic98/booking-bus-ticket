package com.bookingbustickets.bookingbustickets.controller;

import com.bookingbustickets.bookingbustickets.controller.request.RequestPassengerCategoryDto;
import com.bookingbustickets.bookingbustickets.controller.response.PaginatedResponse;
import com.bookingbustickets.bookingbustickets.controller.response.ResponsePassengerCategoryDto;
import com.bookingbustickets.bookingbustickets.domain.model.PassengerCategory;
import com.bookingbustickets.bookingbustickets.domain.service.PassengerCategoryService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/passenger-categories")
public class PassengerCategoryController {

    private final PassengerCategoryService passengerCategoryService;

    public PassengerCategoryController(PassengerCategoryService passengerCategoryService) {
        this.passengerCategoryService = passengerCategoryService;
    }

    @GetMapping
    public PaginatedResponse<ResponsePassengerCategoryDto> getPassengerCategories(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize) {
        Page<PassengerCategory> allPassengerCategory = passengerCategoryService.getAllPassengerCategories(pageNumber, pageSize);
        Page<ResponsePassengerCategoryDto> map = allPassengerCategory.map(this::toResponseDto);
        return new PaginatedResponse<>(map);
    }

    private ResponsePassengerCategoryDto toResponseDto(PassengerCategory passengerCategory){
        return new ResponsePassengerCategoryDto(
                passengerCategory.getId(),
                passengerCategory.getCategoryName(),
                passengerCategory.getDiscountPercentage()
        );
    }

    @GetMapping("/{id}")
    public ResponsePassengerCategoryDto findPassengerCategoryById(@PathVariable Long id) {
        PassengerCategory passengerCategory = passengerCategoryService.findPassengerCategoryById(id);
        return new ResponsePassengerCategoryDto(
                passengerCategory.getId(),
                passengerCategory.getCategoryName(),
                passengerCategory.getDiscountPercentage());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponsePassengerCategoryDto createPassengerCategory(@Valid @RequestBody RequestPassengerCategoryDto requestPassengerCategoryDto) {
        PassengerCategory createdPassengerCategory = passengerCategoryService.createPassengerCategory(requestPassengerCategoryDto);
        return new ResponsePassengerCategoryDto(
                createdPassengerCategory.getId(),
                createdPassengerCategory.getCategoryName(),
                createdPassengerCategory.getDiscountPercentage());
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponsePassengerCategoryDto updatePassengerCategory(@Valid @PathVariable("id") Long id, @RequestBody RequestPassengerCategoryDto requestPassengerCategoryDto) {
        PassengerCategory updatedPassengerCategory = passengerCategoryService.updatePassengerCategory(id, requestPassengerCategoryDto);
        return new ResponsePassengerCategoryDto(
                updatedPassengerCategory.getId(),
                updatedPassengerCategory.getCategoryName(),
                updatedPassengerCategory.getDiscountPercentage());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePassengerCategory(@PathVariable("id") Long id) {
        passengerCategoryService.deletePassengerCategory(id);
    }
}
