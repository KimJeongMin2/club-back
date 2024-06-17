package com.clubcommunity.dto;

import com.clubcommunity.domain.Category;
import lombok.*;

import java.time.LocalDateTime;

public class VideoDTO {

    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class Request {
        private Long postId;
        private String title; // 동영상 제목
        private String content; // 동영상 url
        private MemberDTO member;
        private LocalDateTime createdAt;
        private Category category;
        private LocalDateTime updateAt;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class UpdateRequest {
        private String id;
        private String title; // 동영상 제목
        private String content; // 동영상 url
    }



}
