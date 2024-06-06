package com.clubcommunity.repository;

import com.clubcommunity.domain.*;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubJoinRepository extends JpaRepository<ClubJoin, Long> {
    List<ClubJoin> findByClub(Club club);
}
