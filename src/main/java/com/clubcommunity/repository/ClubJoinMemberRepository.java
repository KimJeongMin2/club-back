package com.clubcommunity.repository;

import com.clubcommunity.domain.Club;
import com.clubcommunity.domain.ClubJoin;
import com.clubcommunity.domain.ClubJoinMember;
import com.clubcommunity.domain.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubJoinMemberRepository extends JpaRepository<ClubJoinMember, Long> {
    List<ClubJoinMember> findByStatus(Status status);
    List<ClubJoinMember> findByClubJoinAndStatus(ClubJoin clubJoin, Status status);
}
