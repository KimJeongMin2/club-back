package com.clubcommunity.dto;

import com.clubcommunity.domain.Category;

import com.clubcommunity.domain.NoticeVisibilityType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;


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
}
