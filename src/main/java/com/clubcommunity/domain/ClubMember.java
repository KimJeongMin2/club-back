package com.clubcommunity.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity(name="club-memebr")
@AllArgsConstructor
@NoArgsConstructor
public class ClubMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cmId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @Enumerated(EnumType.STRING)
    private MemberStatus memberStatus;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String refusalReason;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    public void approve() {
        this.status = Status.APPROVAL;
        this.roleType = RoleType.MASTER;
    }

    public void reject(String refusalReason) {
        this.status = Status.REFUSE;
        this.refusalReason = refusalReason;
    }
}
