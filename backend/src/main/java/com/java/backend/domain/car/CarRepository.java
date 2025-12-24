package com.java.backend.domain.car;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    // 대여 가능한 차량만 필터링해서 찾기
    List<Car> findByIsAvailableTrue();
}