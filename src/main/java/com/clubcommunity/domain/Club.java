package com.clubcommunity.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity(name="club")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Club {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clubId;

    @Enumerated(EnumType.STRING)
    private Type type;
    private String clubName;

    private String applicantName; // 신청자 이름
    private String applicantDepartment; //신청자 학과
    private Long applicantId; //신청자 학번
    private String applicantPhone; //신청자 전화번호

    private String professorName;
    private String professorMajor;
    private String professorPhone;

    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] registration;
    private String introduction;
    private String history;
    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] photo;
    @CreatedDate
    private LocalDateTime meetingTime;
    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] staffList;

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClubMember> clubMembers = new ArrayList<>();
}
