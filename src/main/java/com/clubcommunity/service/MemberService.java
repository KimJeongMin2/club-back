package com.clubcommunity.service;

import com.clubcommunity.domain.Member;
import com.clubcommunity.dto.MemberDTO;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

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
}
