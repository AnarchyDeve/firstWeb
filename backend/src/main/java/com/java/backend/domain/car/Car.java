package com.java.backend.domain.car;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String modelName; // 차량 모델명 (예: 아반떼)

    @Column(nullable = false, unique = true)
    private String carNumber; // 차량 번호

    @Column(nullable = false)
    private Long pricePerDay; // 하루 대여 비용

    @Builder.Default
    private boolean isAvailable = true; // 대여 가능 여부 (기본값 true)

    // 차량 상태 변경 메서드
    public void updateAvailability(boolean status) {
        this.isAvailable = status;
    }
}