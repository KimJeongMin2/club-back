package com.clubcommunity.service;

import com.clubcommunity.domain.Gender;
import com.clubcommunity.domain.Member;
import com.clubcommunity.domain.RoleType;
import com.clubcommunity.dto.MemberDTO;
import com.clubcommunity.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member convertMemberDTOToMember(MemberDTO memberDTO) {
        Member member = new Member();
        member.setStudentId(memberDTO.getStudentId());
        member.setName(memberDTO.getName());
        member.setBirth(memberDTO.getBirth());
        member.setGender(memberDTO.getGender());
        member.setDepartment(memberDTO.getDepartment());
        member.setPhoneNum(memberDTO.getPhoneNum());
        member.setEmail(memberDTO.getEmail());
//        member.setRoleType(memberDTO.getRoleType());
        return member;
    }
    public MemberDTO convertMemberToMemberDTO(Member member) {
        System.out.println("member임 = " + member);
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setStudentId(member.getStudentId());
        memberDTO.setName(member.getName());
        memberDTO.setBirth(member.getBirth());
        memberDTO.setGender(member.getGender());
        memberDTO.setDepartment(member.getDepartment());
        memberDTO.setPhoneNum(member.getPhoneNum());
        memberDTO.setEmail(member.getEmail());
//        memberDTO.setRoleType(member.getRoleType());
        return memberDTO;
    }
    public Member findMemberById(Long userId) {
        return memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다. ID: " + userId));
    }

    public void registerMember(MemberDTO memberDTO) throws Exception {
        if (memberRepository.existsByUid(memberDTO.getUid())) {
            throw new Exception("이미 존재하는 ID입니다.");
        }

        Member member = new Member();
        member.setUid(memberDTO.getUid());
        member.setPw(memberDTO.getPw());
        member.setName(memberDTO.getName());
        member.setEmail(memberDTO.getEmail());
        member.setPhoneNum(memberDTO.getPhoneNum());
        member.setGender(Gender.valueOf(String.valueOf(memberDTO.getGender()).toUpperCase()));
        member.setBirth(Long.valueOf(memberDTO.getBirth()));
        member.setDepartment(memberDTO.getDepartment());
        member.setStudentId(Long.valueOf(memberDTO.getStudentId()));
        member.setRoleType(RoleType.valueOf(String.valueOf(memberDTO.getRoleType())));

        memberRepository.save(member);
    }
}
