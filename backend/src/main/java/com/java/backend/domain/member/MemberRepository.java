package com.java.backend.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // 이메일로 회원을 찾는 기능 추가 (로그인 시 사용)
    Optional<Member> findByEmail(String email);
}