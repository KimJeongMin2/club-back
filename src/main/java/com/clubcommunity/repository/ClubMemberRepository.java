package com.clubcommunity.repository;


import com.clubcommunity.domain.ClubMember;

import com.clubcommunity.domain.Member;
import com.clubcommunity.domain.RoleType;
import com.clubcommunity.domain.Status;

import com.clubcommunity.domain.*;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {

    List<ClubMember> findByMemberAndRoleType(Member member, RoleType roleType);

    List<ClubMember> findByMember(Member member);



    ClubMember findByClubAndStatus(Club club, Status status);

}
