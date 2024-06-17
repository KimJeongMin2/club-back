package com.clubcommunity.repository;

import com.clubcommunity.domain.Member;
import com.clubcommunity.domain.RoleType;
import com.clubcommunity.dto.MemberDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {
    Member findByUidAndPw(String uid, String pw);

}
