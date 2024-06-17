package com.clubcommunity.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity(name="club-join-member")
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ClubJoinMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clubJoinMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_join_id")
    private ClubJoin clubJoin;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private MemberStatus memberStatus;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String refusalReason;
    @Enumerated(EnumType.STRING)
    private RoleType roleType;
}
