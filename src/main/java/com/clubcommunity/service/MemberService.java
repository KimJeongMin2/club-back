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
        member.setRoleType(memberDTO.getRoleType());
        return member;
    }
}
