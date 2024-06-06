package com.clubcommunity.repository;

import com.clubcommunity.domain.ClubMember;

import com.clubcommunity.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {

    List<ClubMember> findByMember(Member member);
}
