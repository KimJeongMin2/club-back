package com.clubcommunity.repository;

import com.clubcommunity.domain.Club;
import com.clubcommunity.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubRepository  extends JpaRepository<Club, Long> {
}
