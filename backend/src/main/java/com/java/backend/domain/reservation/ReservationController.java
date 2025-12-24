package com.java.backend.domain.reservation;

import com.java.backend.domain.reservation.dto.ReservationRequestDto;
import com.java.backend.domain.reservation.dto.ReservationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    /**
     * [차량 예약하기]
     * POST /api/reservations
     * 헤더에 Authorization: Bearer <JWT_TOKEN> 필수
     */
    @PostMapping
    public ResponseEntity<String> createReservation(
            @RequestBody ReservationRequestDto requestDto,
            Authentication authentication) {

        // 1. 토큰으로부터 현재 로그인한 사용자의 이메일을 가져옴
        // (JwtAuthenticationFilter에서 채워준 정보)
        String email = authentication.getName();

        // 2. 서비스 레이어에 예약 처리를 맡김
        reservationService.createReservation(email, requestDto);

        return ResponseEntity.ok("차량 예약이 성공적으로 완료되었습니다!");
    }

    /**
     * [내 예약 내역 조회하기]
     * GET /api/reservations/me
     * 본인의 예약 리스트만 반환
     */
    @GetMapping("/me")
    public ResponseEntity<List<ReservationResponseDto>> getMyReservations(Authentication authentication) {

        // 1. 토큰에서 이메일 추출
        String email = authentication.getName();

        // 2. 내 예약 목록 가져오기
        List<ReservationResponseDto> myReservations = reservationService.getMyReservations(email);

        return ResponseEntity.ok(myReservations);
    }

    // ReservationController.java에 추가
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<String> cancelReservation(
            @PathVariable Long reservationId,
            Authentication authentication) {

        String email = authentication.getName();
        reservationService.cancelReservation(reservationId, email);

        return ResponseEntity.ok("예약이 성공적으로 취소되었습니다.");
    }
}