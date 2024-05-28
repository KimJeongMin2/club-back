package com.clubcommunity.service;

import com.clubcommunity.domain.*;
import com.clubcommunity.dto.ClubJoinDTO;
import com.clubcommunity.dto.PostDTO;
import com.clubcommunity.repository.ClubJoinRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
@Service
@Slf4j
public class ClubJoinService {
    private final ClubJoinRepository clubJoinRepository;
    private final MemberService memberService;
    private final ClubService clubService;

    public ClubJoinService(ClubJoinRepository clubJoinRepository, MemberService memberService, ClubService clubService){
        this.clubJoinRepository = clubJoinRepository;
        this.memberService = memberService;
        this.clubService = clubService;
    }

    public ClubJoin createClubJoin(ClubJoinDTO clubJoinDTO, MultipartFile files) {
        ClubJoin.ClubJoinBuilder clubJoinBuilder = ClubJoin.builder()
                .member(memberService.convertMemberDTOToMember(clubJoinDTO.getMember()))
                .club(clubService.convertClubDTOToClub(clubJoinDTO.getClub()))
                .title(clubJoinDTO.getTitle())
                .memberStatus(MemberStatus.ACTIVITY)
                .status(Status.GO_OVER)
                .createAt(clubJoinDTO.getCreatedAt());

        try {
            clubJoinBuilder.file(files.getBytes());
        } catch (IOException e) {
            log.error("Error uploading file: {}", e.getMessage());
        }

        ClubJoin clubJoin = clubJoinBuilder.build();
        return clubJoinRepository.save(clubJoin);
    }




}
