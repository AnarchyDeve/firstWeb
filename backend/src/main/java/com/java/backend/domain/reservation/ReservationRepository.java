package com.java.backend.domain.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    // 특정 회원의 예약 내역만 찾아오는 기능 (나중에 사용)
    List<Reservation> findByMemberEmail(String email);
}