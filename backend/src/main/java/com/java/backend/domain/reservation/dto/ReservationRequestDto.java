package com.java.backend.domain.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequestDto {
    private Long carId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}