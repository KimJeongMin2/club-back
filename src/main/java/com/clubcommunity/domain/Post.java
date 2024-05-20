package com.clubcommunity.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Member member;

    private String title;
    private String content;

    @Enumerated(EnumType.STRING)
    private Category category;
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] photo;

    @Enumerated(EnumType.STRING)
    private NoticeVisibilityType noticeVisibilityType;

    @CreationTimestamp
    private LocalDateTime createAt = LocalDateTime.now();
}
