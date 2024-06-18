package com.clubcommunity.controller;

import com.clubcommunity.domain.Member;
import com.clubcommunity.domain.RoleType;
import com.clubcommunity.dto.MemberDTO;
import com.clubcommunity.repository.MemberRepository;
import com.clubcommunity.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/")
public class MemberController {
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    public MemberController(MemberService memberService, MemberRepository memberRepository) {
        this.memberService = memberService;
        this.memberRepository = memberRepository;
    }
//    public MemberController(MemberService memberService) {
//        this.memberService = memberService;
//    }

    @PostMapping("/members/signup")
    public ResponseEntity<String> completeSignUp(@RequestBody MemberDTO memberDTO) {
        try {
            memberService.registerMember(memberDTO);
            return ResponseEntity.ok("회원가입 완료");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("회원가입 실패: " + e.getMessage());
        }
    }
    @PostMapping("/login")
    public ResponseEntity<Object> handleLoFgin(@RequestBody Map<String, String> credentials, HttpServletRequest request, HttpServletResponse response) {
        String userId = credentials.get("userId");
        String password = credentials.get("password");

        Member member = memberService.findByUidAndPw(userId, password);
        System.out.println("member = " + member);
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", member.getUid());
        userInfo.put("name", member.getName());
        userInfo.put("roleType", member.getRoleType());
        userInfo.put("isLoggedIn", true);
        System.out.println("userInfo = " + userInfo);

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
        System.out.println("response = " + response);


        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("http://localhost:3000"));
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @GetMapping("/members/baseInfo/{uid}")
    public ResponseEntity<MemberDTO> getMemberBaseInfo(@PathVariable("uid") String uid) {
        System.out.println("여기 uid????????" + uid);
        MemberDTO member = memberService.getMemberBaseInfo(uid);
        return ResponseEntity.ok(member);
    }
//    @PostMapping("/login")
//    public ResponseEntity<Map<String, Object>> handleLogin(@RequestBody Map<String, String> credentials, HttpServletRequest request, HttpServletResponse response) {
//        String userId = credentials.get("userId");
//        String password = credentials.get("password");
//
//        Member member = memberService.findByUidAndPw(userId, password);
//
//        if (member != null) {
//            Map<String, Object> userInfo = new HashMap<>();
//            userInfo.put("id", member.getUid());
//            userInfo.put("name", member.getName());
//            userInfo.put("roleType", member.getRoleType());
//            userInfo.put("isLoggedIn", true);
//
//            // 쿠키 및 세션 설정 부분은 동일
//
//            return ResponseEntity.ok(userInfo);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

//    public ResponseEntity<Object> login(@RequestBody Member member, HttpServletRequest request, HttpServletResponse response) {
//        Member foundMember = memberService.findByUidAndPw(member.getUid(), member.getPw());
//
//        if (foundMember != null) {
//            return handleLogin(foundMember, request, response);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//    @PostMapping("/login")
//    private ResponseEntity<Object> handleLogin(Member member, HttpServletRequest request, HttpServletResponse response) {
//        Map<String, Object> userInfo = new HashMap<>();
//        if (member.getUid() != null) {
//            userInfo.put("id", member.getUid());
//        }
//        RoleType roleType = member.getRoleType();
//        if (roleType != null) {
//            userInfo.put("roleType", roleType);
//        }
//        String name = memberRepository.findNameByUid(member.getUid());
//        if (member.getName() != null) {
//            userInfo.put("name", member.getName());
//        }
//        userInfo.put("isLoggedIn", true);
//



}