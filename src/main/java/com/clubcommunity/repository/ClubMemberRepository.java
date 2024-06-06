package com.clubcommunity.repository;

import com.clubcommunity.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {

    List<ClubMember> findByMemberAndRoleType(Member member, RoleType roleType);

    ClubMember findByClubAndStatus(Club club, Status status);
}
