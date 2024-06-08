package com.clubcommunity.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity(name="club-join")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClubJoin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clubJoinId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "student_id")
//    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    private String title;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] file;

    private String uploadFileName;
    @CreationTimestamp
    private LocalDateTime createAt = LocalDateTime.now();

}
