package com.clubcommunity.repository;

import com.clubcommunity.domain.Club;
import com.clubcommunity.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClubRepository  extends JpaRepository<Club, Long> {
    @Query("SELECT DISTINCT c FROM club c JOIN c.clubMembers cm WHERE cm.status = 'APPROVAL'")
    List<Club> findAllWithApprovedMembers();
}
