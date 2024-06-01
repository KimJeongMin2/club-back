package com.clubcommunity.repository;

import com.clubcommunity.domain.Category;
import com.clubcommunity.domain.ClubJoin;

import com.clubcommunity.domain.Post;
import com.clubcommunity.domain.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubJoinRepository extends JpaRepository<ClubJoin, Long> {
    List<ClubJoin> findByStatus(Status status);
}
