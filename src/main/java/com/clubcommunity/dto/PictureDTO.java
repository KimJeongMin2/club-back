package com.clubcommunity.dto;

import com.clubcommunity.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class PictureDTO {
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class Request {
        private Long postId;
        private String title;
        private String content;
        private MemberDTO member;
        private LocalDateTime createdAt;
        private Category category;
        private LocalDateTime updateAt;
        private String photoBase64;
    }
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class UpdateRequest {
        private String id;
        private String title;
        private String content;
        private byte[] photo;
    }
}
