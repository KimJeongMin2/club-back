package com.clubcommunity.controller;

import com.clubcommunity.domain.ClubJoin;
import com.clubcommunity.domain.ClubJoinMember;
import com.clubcommunity.dto.ClubJoinDTO;
import com.clubcommunity.dto.ClubJoinRejectDTO;
import com.clubcommunity.service.ClubJoinMemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/join-club")
public class ClubJoinMemberController {
    private final ClubJoinMemberService clubJoinMemberService;

    public ClubJoinMemberController(ClubJoinMemberService clubJoinMemberService) {
        this.clubJoinMemberService = clubJoinMemberService;
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<ClubJoinMember> approveClubJoin(@PathVariable(name = "id") Long id) {
        ClubJoinMember approvedClubJoin = clubJoinMemberService.approveClubJoin(id);
        return ResponseEntity.ok(approvedClubJoin);
    }

    @PutMapping("/approve")
    public ResponseEntity<List<ClubJoinMember>> approveMultipleClubJoins(@RequestBody List<Long> clubJoinIds) {
        System.out.println("clubJoinIds = " + clubJoinIds);
        List<ClubJoinMember> approvedClubJoins = clubJoinMemberService.approveMultipleClubJoins(clubJoinIds);
        return ResponseEntity.ok(approvedClubJoins);
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<ClubJoinMember> rejectClubJoin(
            @PathVariable(name = "id") Long id,
            @RequestBody ClubJoinRejectDTO rejectDTO) {
        ClubJoinMember rejectedClubJoin = clubJoinMemberService.rejectClubJoin(id, rejectDTO.getRefusalReason());
        return ResponseEntity.ok(rejectedClubJoin);
    }

    @PutMapping("/reject")
    public ResponseEntity<List<ClubJoinMember>> rejectMultipleClubJoins(@RequestBody List<ClubJoinRejectDTO> rejectionList) {
        List<ClubJoinMember> rejectedClubJoins = clubJoinMemberService.rejectMultipleClubJoins(rejectionList);
        return ResponseEntity.ok(rejectedClubJoins);
    }



}
