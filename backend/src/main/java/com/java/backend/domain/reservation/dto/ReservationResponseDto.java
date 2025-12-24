package com.java.backend.domain.reservation.dto;

import com.java.backend.domain.reservation.Reservation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReservationResponseDto {
    private Long reservationId;
    private String modelName;
    private String carNumber;
    private String memberEmail;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long totalPrice;

    public ReservationResponseDto(Reservation reservation) {
        this.reservationId = reservation.getId();
        this.modelName = reservation.getCar().getModelName();
        this.carNumber = reservation.getCar().getCarNumber();
        this.memberEmail = reservation.getMember().getEmail();
        this.startTime = reservation.getStartTime();
        this.endTime = reservation.getEndTime();
        this.totalPrice = reservation.getTotalPrice();
    }
}