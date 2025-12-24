package com.java.backend.domain.car;

import com.java.backend.domain.car.Car;
import com.java.backend.domain.car.CarRepository;
import com.java.backend.domain.car.CarResponseDto;
import com.java.backend.domain.car.dto.CarRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CarService {

    private final CarRepository carRepository;

    @Transactional
    public Long register(CarRequestDto requestDto) {
        Car car = Car.builder()
                .modelName(requestDto.getModelName())
                .carNumber(requestDto.getCarNumber())
                .pricePerDay(requestDto.getPricePerDay())
                .isAvailable(true)
                .build();
        return carRepository.save(car).getId();
    }

    public List<CarResponseDto> getAllCars() {
        return carRepository.findAll().stream()
                .map(CarResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<CarResponseDto> getAvailableCars() {
        return carRepository.findByIsAvailableTrue().stream()
                .map(CarResponseDto::new)
                .collect(Collectors.toList());
    }
}