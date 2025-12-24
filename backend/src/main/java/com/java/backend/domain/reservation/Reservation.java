package com.java.backend.domain.reservation;

import com.java.backend.domain.car.Car;
import com.java.backend.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 한 명의 회원은 여러 번 예약할 수 있음 (N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 한 대의 차량은 여러 번 예약될 수 있음 (N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")
    private Car car;

    private LocalDateTime startTime; // 대여 시작 시간
    private LocalDateTime endTime;   // 반납 시간

    // 총 결제 금액 (필요시 나중에 계산 로직 추가)
    private Long totalPrice;
}