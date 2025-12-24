package com.java.backend.domain.car;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CarResponseDto {
    private Long id;
    private String modelName;
    private String carNumber;
    private Long pricePerDay;
    private boolean isAvailable;

    // Entity를 DTO로 변환하는 생성자
    public CarResponseDto(Car car) {
        this.id = car.getId();
        this.modelName = car.getModelName();
        this.carNumber = car.getCarNumber();
        this.pricePerDay = car.getPricePerDay();
        this.isAvailable = car.isAvailable();
    }
}