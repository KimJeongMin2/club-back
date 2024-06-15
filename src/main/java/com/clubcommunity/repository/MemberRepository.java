package com.clubcommunity.repository;

import com.clubcommunity.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByUid(String uid);

    Optional<Member> findByUid(String kakaoId);
}
