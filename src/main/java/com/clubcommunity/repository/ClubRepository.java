package com.clubcommunity.repository;

import com.clubcommunity.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubRepository  extends JpaRepository<Member, Long> {
}
