package com.java.backend.domain.reservation;

import com.java.backend.domain.car.Car;
import com.java.backend.domain.car.CarRepository;
import com.java.backend.domain.member.Member;
import com.java.backend.domain.member.MemberRepository;
import com.java.backend.domain.reservation.dto.ReservationRequestDto;
import com.java.backend.domain.reservation.dto.ReservationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본적으로 읽기 전용 (성능 최적화)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final CarRepository carRepository;

    /**
     * 1. 차량 예약 생성
     */
    @Transactional // 쓰기 작업이므로 Transactional 추가
    public void createReservation(String email, ReservationRequestDto requestDto) {
        // 1. 회원 조회
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 2. 차량 조회 및 예약 가능 상태 확인
        Car car = carRepository.findById(requestDto.getCarId())
                .orElseThrow(() -> new RuntimeException("차량을 찾을 수 없습니다."));

        if (!car.isAvailable()) {
            throw new RuntimeException("해당 차량은 이미 예약 중입니다.");
        }

        // 3. 예약 엔티티 생성
        Reservation reservation = Reservation.builder()
                .member(member)
                .car(car)
                .startTime(requestDto.getStartTime())
                .endTime(requestDto.getEndTime())
                .build();

        // 4. 예약 저장 및 차량 상태 변경 (isAvailable = false)
        reservationRepository.save(reservation);
        car.updateAvailability(false);
    }

    /**
     * 2. 내 예약 내역 조회
     */
    public List<ReservationResponseDto> getMyReservations(String email) {
        // 이메일로 해당 회원의 예약 리스트를 모두 가져옴
        List<Reservation> reservations = reservationRepository.findByMemberEmail(email);

        // 엔티티 리스트를 응답용 DTO 리스트로 변환
        return reservations.stream()
                .map(ReservationResponseDto::new)
                .collect(Collectors.toList());
    }

    // ReservationService.java에 추가
    @Transactional
    public void cancelReservation(Long reservationId, String email) {
        // 1. 해당 예약이 존재하는지 확인
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("예약 내역을 찾을 수 없습니다."));

        // 2. 본인의 예약이 맞는지 권한 확인 (보안)
        if (!reservation.getMember().getEmail().equals(email)) {
            throw new RuntimeException("취소 권한이 없습니다.");
        }

        // 3. 연관된 차량을 다시 대여 가능(true) 상태로 변경
        Car car = reservation.getCar();
        car.updateAvailability(true);

        // 4. 예약 데이터 삭제
        reservationRepository.delete(reservation);
    }
}