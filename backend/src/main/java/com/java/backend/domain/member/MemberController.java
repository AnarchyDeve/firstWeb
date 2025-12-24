package com.java.backend.domain.member;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication; // 이 부분이 핵심 수정 포인트!
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody MemberRequestDto requestDto) {
        memberService.register(requestDto);

        // String 대신 JSON 객체(Map)를 반환하여 프론트엔드와 형식을 맞춤
        return ResponseEntity.ok(java.util.Map.of("message", "회원가입 성공!"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String password = loginData.get("password");

        String token = memberService.login(email, password);
        return ResponseEntity.ok(Map.of("accessToken", token));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo() {
        // SecurityContextHolder에서 현재 요청의 인증 정보를 가져옵니다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증 정보가 없거나 비정상적인 경우 401 에러 반환
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("인증되지 않은 사용자입니다.");
        }

        // authentication.getName()은 토큰 생성 시 넣었던 주인공(email)을 반환합니다.
        String email = authentication.getName();
        return ResponseEntity.ok("현재 로그인한 사용자는: " + email + " 입니다.");
    }
}