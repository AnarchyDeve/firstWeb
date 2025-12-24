package com.java.backend.domain.car.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CarRequestDto {
    private String modelName;
    private String carNumber;
    private Long pricePerDay;
}