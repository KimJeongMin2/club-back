package com.clubcommunity.dto;

import com.clubcommunity.domain.Category;

import com.clubcommunity.domain.NoticeVisibilityType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class PostDTO {
    private Long postId;
    private String title;
    private String content;
    private Category category;
    private NoticeVisibilityType noticeVisibilityType;
    private LocalDateTime createdAt;
    private MemberDTO member;

    private ClubDTO club;
    private byte[] photo;

    private String uploadFileName;
    private byte[] file;

}
