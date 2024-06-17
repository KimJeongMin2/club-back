package com.clubcommunity.controller;

import com.clubcommunity.domain.Gender;
import com.clubcommunity.domain.Member;
import com.clubcommunity.domain.RoleType;
import com.clubcommunity.dto.MemberDTO;
import com.clubcommunity.repository.MemberRepository;
import com.clubcommunity.service.MemberService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class KakaoOAuthController {

    @Value("${kakao.rest.api.key}")
    private String kakaoRestApiKey;

    @Value("${kakao.redirect.uri}")
    private String kakaoRedirectUri;

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    private Map<String,String> userDataMap = new HashMap<>();


    public KakaoOAuthController(MemberService memberService, MemberRepository memberRepository) {
        this.memberService = memberService;
        this.memberRepository = memberRepository;
    }
    @GetMapping("/session")
    public ResponseEntity<Object> checkSessionValidity(HttpSession session) {
        if (session.isNew() || session.getAttribute("userInfo") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            return ResponseEntity.ok().build();
        }
    }
    @GetMapping("/user-data")
    public ResponseEntity<Map<String, Object>> getUserData(HttpSession session) {
        Map<String, Object> userInfo = (Map<String, Object>) session.getAttribute("userInfo");

        if (userInfo == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(userInfo);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        Cookie cookie = new Cookie("JSESSIONID", "");
        cookie.setMaxAge(0); // 유효기간을 0으로 설정하여 쿠키 삭제
        cookie.setPath("/"); // 쿠키의 경로 설정
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/oauth/callback/kakao")
    public ResponseEntity<Object> handleKakaoCallback(@RequestParam("code") String code, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("Received code: " + code);
        String accessToken = getAccessToken(code);
        if (accessToken == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("토큰 요청 실패");
        }

        JsonNode userInfo = getUserInfo(accessToken);
        if (userInfo == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("사용자 정보 요청 실패");
        }
        System.out.println("userInfo = " + userInfo);
        String kakaoId = userInfo.get("id").asText();
        Member member = memberRepository.findByUid(kakaoId).orElse(null);
        if (member == null) {
            return handleSignup(userInfo, kakaoId);
        } else {
            return handleLogin(member, request, response);
        }
    }

    private String getAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String tokenUrl = "https://kauth.kakao.com/oauth/token";
        String body = "grant_type=authorization_code" +
                "&client_id=" + kakaoRestApiKey +
                "&redirect_uri=" + kakaoRedirectUri +
                "&code=" + code;
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(response.getBody());
                return jsonNode.get("access_token").asText();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private JsonNode getUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readTree(response.getBody());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private ResponseEntity<Object> handleSignup(JsonNode userInfo, String kakaoId) {
        if (memberRepository.existsByUid(kakaoId)) {
            return ResponseEntity.badRequest().body("이미 존재하는 ID입니다.");
        }

        String email = userInfo.path("kakao_account").path("email").asText();
        String gender = userInfo.path("kakao_account").path("gender").asText();
        String name = userInfo.path("kakao_account").path("name").asText();
        String phoneNum = userInfo.path("kakao_account").path("phone_number").asText();

        userDataMap.put("uid", kakaoId);
        userDataMap.put("email", email);
        userDataMap.put("gender", gender.toUpperCase());
        userDataMap.put("name", name);
        userDataMap.put("phoneNum", phoneNum);


        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", "http://localhost:3000/KakaoSignup?userId=" + kakaoId)
                .build();
    }

    private ResponseEntity<Object> handleLogin(Member member, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> userInfo = new HashMap<>();
        if (member.getUid() != null) {
            userInfo.put("id", member.getUid());
        }
        if (member.getRoleType() != null) {
            userInfo.put("roleType", member.getRoleType());
        }
        if (member.getName() != null) {
            userInfo.put("name", member.getName());
        }

        userInfo.put("isLoggedIn", true);

        Cookie cookieId = new Cookie("userId", member.getUid());
        cookieId.setMaxAge(3600);
        cookieId.setPath("/");
        response.addCookie(cookieId);

        Cookie cookieRoleType = new Cookie("roleType", member.getRoleType().toString());
        cookieRoleType.setMaxAge(3600);
        cookieRoleType.setPath("/");
        response.addCookie(cookieRoleType);

        Cookie cookieName = new Cookie("name", member.getName());
        cookieName.setMaxAge(3600);
        cookieName.setPath("/");
        response.addCookie(cookieName);

        Cookie cookieIsLoggedIn = new Cookie("isLoggedIn", "true");
        cookieIsLoggedIn.setMaxAge(3600);
        cookieIsLoggedIn.setPath("/");
        response.addCookie(cookieIsLoggedIn);

        HttpSession session = request.getSession();
        session.setAttribute("userInfo", userInfo);
        session.setMaxInactiveInterval(1800);

        Cookie sessionIdCookie = new Cookie("JSESSIONID", session.getId());
        sessionIdCookie.setMaxAge(3600);
        sessionIdCookie.setPath("/");
        response.addCookie(sessionIdCookie);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("http://localhost:3000"));

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> completeSignUp(@RequestBody Map<String, Object> additionalData) {
        System.out.println("additionalData = " + additionalData);

        userDataMap.put("birth", String.valueOf(Long.valueOf(additionalData.get("birth").toString())));
        userDataMap.put("department", (String) additionalData.get("department"));
        userDataMap.put("studentId", String.valueOf(Long.valueOf(additionalData.get("studentId").toString())));
        userDataMap.put("roleType", (String) additionalData.get("roleType"));

        System.out.println("userDataMap = " + userDataMap);

        MemberDTO memberDTO = MemberDTO.builder()
                .uid(userDataMap.get("uid"))
                .name(userDataMap.get("name"))
                .email(userDataMap.get("email"))
                .phoneNum(userDataMap.get("phoneNum"))
                .gender(Gender.valueOf(userDataMap.get("gender")))
                .birth(Long.valueOf(userDataMap.get("birth")))
                .department(userDataMap.get("department"))
                .studentId(Long.valueOf(userDataMap.get("studentId")))
                .roleType(RoleType.valueOf( userDataMap.get("roleType")))
                .build();

        try {
            memberService.registerMember(memberDTO);
            return ResponseEntity.status(HttpStatus.OK).body("회원가입 완료");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입 실패: " + e.getMessage());
        }
    }
}