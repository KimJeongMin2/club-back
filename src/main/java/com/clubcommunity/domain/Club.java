package com.clubcommunity.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity(name="club")
@NoArgsConstructor
public class Club {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clubId;

    @Enumerated(EnumType.STRING)
    private Type type;
    private String clubName;
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
