package com.clubcommunity.controller;

import com.clubcommunity.domain.ClubJoin;
import com.clubcommunity.domain.Post;
import com.clubcommunity.dto.ClubJoinDTO;
import com.clubcommunity.dto.PostDTO;
import com.clubcommunity.service.ClubJoinService;
import com.clubcommunity.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/join-club")
public class ClubJoinController {

    private final ClubJoinService clubJoinService;

    public ClubJoinController(ClubJoinService clubJoinService){
        this.clubJoinService = clubJoinService;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ClubJoin> createPost(
            @RequestPart(value = "dto", required = false) ClubJoinDTO clubJoinDTO,
            @RequestPart(value = "files", required = false) MultipartFile files
    ) {

        System.out.println("Title: " + clubJoinDTO.getTitle());
        System.out.println("Member: " + clubJoinDTO.getMember());
        System.out.println("files: " + files);
        ClubJoin savedPost = clubJoinService.createClubJoin(clubJoinDTO, files);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPost);
    }

}
