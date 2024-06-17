package com.clubcommunity.controller;

import com.clubcommunity.domain.Member;
import com.clubcommunity.dto.MemberDTO;
import com.clubcommunity.service.MemberService;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToFile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }
    @PostMapping("/members/signup")
    public ResponseEntity<String> completeSignUp(@RequestBody MemberDTO memberDTO) {
        try {
            memberService.registerMember(memberDTO);
            return ResponseEntity.ok("회원가입 완료");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("회원가입 실패: " + e.getMessage());
        }
    }


    @GetMapping("/api/members/baseInfo/{uid}")
    public ResponseEntity<MemberDTO> getMemberBaseInfo(@PathVariable("uid") String uid) {
        System.out.println("여기 uid????????" + uid);
        MemberDTO member = memberService.getMemberBaseInfo(uid);
        return ResponseEntity.ok(member);
    }
}