package com.clubcommunity.repository;

import com.clubcommunity.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubJoinMemberRepository extends JpaRepository<ClubJoinMember, Long> {
    List<ClubJoinMember> findByStatus(Status status);
    List<ClubJoinMember> findByClubJoinAndStatus(ClubJoin clubJoin, Status status);

    List<ClubJoinMember> findByClubJoinAndMemberStatus(ClubJoin clubJoin, MemberStatus memberStatus);
}
