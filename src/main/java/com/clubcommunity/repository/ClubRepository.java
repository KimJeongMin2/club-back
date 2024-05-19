package com.clubcommunity.repository;

import com.clubcommunity.domain.Club;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubRepository  extends JpaRepository<Club, Long> {
}
