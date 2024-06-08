package com.clubcommunity.controller;

import com.clubcommunity.domain.ClubJoin;
import com.clubcommunity.domain.Post;
import com.clubcommunity.dto.ClubJoinDTO;
import com.clubcommunity.dto.ClubJoinMemberDTO;
import com.clubcommunity.dto.PostDTO;
import com.clubcommunity.service.ClubJoinService;
import com.clubcommunity.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
        System.out.println("files.getOriginalFilename() = " + files.getOriginalFilename());
        System.out.println("Title: " + clubJoinDTO.getTitle());
        System.out.println("Member: " + clubJoinDTO.getMember());
        System.out.println("files: " + files);
        ClubJoin savedPost = clubJoinService.createClubJoin(clubJoinDTO, files);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPost);
    }

    @GetMapping
    public ResponseEntity<List<ClubJoinDTO>> getAllClubJoins(@RequestHeader(value="userId", required = true) Long userId) {
        System.out.println("userId = " + userId);
        List<ClubJoinDTO> posts = clubJoinService.getAllClubJoinForUser(userId);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/approved-members")
    public ResponseEntity<List<ClubJoinMemberDTO>> getApprovedMembersForClub(@RequestHeader(value="userId", required = true) Long userId) {
        System.out.println("userId = " + userId);
        List<ClubJoinMemberDTO> approvedMembers = clubJoinService.getApprovedMembersForClub(userId);
        return ResponseEntity.ok(approvedMembers);
    }


//    @GetMapping
//    public ResponseEntity<List<ClubJoinDTO>> getAllClubJoins(@RequestHeader(value="userId", required = true) String userId) {
//        List<ClubJoinDTO> posts = clubJoinService.getAllClubJoinForUser(userId);
//        return ResponseEntity.ok(posts);
//    }

//    @GetMapping
//    public ResponseEntity<List<ClubJoinDTO>> getAllClubJoins() {
//        List<ClubJoinDTO> posts = clubJoinService.getAllClubJoin();
//        return ResponseEntity.ok(posts);
//    }
//    @PutMapping("/{id}/approve")
//    public ResponseEntity<ClubJoin> approveClubJoin(@PathVariable(name = "id") Long id) {
//        ClubJoin approvedClubJoin = clubJoinService.approveClubJoin(id);
//        return ResponseEntity.ok(approvedClubJoin);
//    }
//
//    @PutMapping("/approve")
//    public ResponseEntity<List<ClubJoin>> approveMultipleClubJoins(@RequestBody List<Long> clubJoinIds) {
//        System.out.println("clubJoinIds = " + clubJoinIds);
//        List<ClubJoin> approvedClubJoins = clubJoinService.approveMultipleClubJoins(clubJoinIds);
//        return ResponseEntity.ok(approvedClubJoins);
//    }
//
//    @PutMapping("/{id}/reject")
//    public ResponseEntity<ClubJoin> rejectClubJoin(@PathVariable(name = "id") Long id) {
//        ClubJoin rejectedClubJoin = clubJoinService.rejectClubJoin(id);
//        return ResponseEntity.ok(rejectedClubJoin);
//    }
//
//    @PutMapping("/reject")
//    public ResponseEntity<List<ClubJoin>> rejectMultipleClubJoins(@RequestBody List<Long> clubJoinIds) {
//        System.out.println("clubJoinIds = " + clubJoinIds);
//        List<ClubJoin> rejectedClubJoins = clubJoinService.rejectMultipleClubJoins(clubJoinIds);
//        return ResponseEntity.ok(rejectedClubJoins);
//    }


}
