package com.java.backend.domain.car;

import com.java.backend.domain.car.dto.CarRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @PostMapping
    public ResponseEntity<String> registerCar(@RequestBody CarRequestDto requestDto) {
        carService.register(requestDto);
        return ResponseEntity.ok("차량 등록 완료!");
    }

    @GetMapping
    public ResponseEntity<List<com.java.backend.domain.car.CarResponseDto>> getAllCars() {
        return ResponseEntity.ok(carService.getAllCars());
    }
}