package com.clubcommunity.service;

import com.clubcommunity.domain.*;
import com.clubcommunity.dto.ClubJoinDTO;
import com.clubcommunity.dto.PostDTO;
import com.clubcommunity.repository.ClubJoinRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    public List<ClubJoinDTO> getAllClubJoin() {
        List<ClubJoin> clubJoins = clubJoinRepository.findByStatus(Status.GO_OVER);
        List<ClubJoinDTO> clubJoinDTOS = new ArrayList<>();
        for (ClubJoin clubJoin : clubJoins) {
            ClubJoinDTO clubJoinDTO = new ClubJoinDTO();
            clubJoinDTO.setClubJoinId(clubJoin.getClubJoinId());
            clubJoinDTO.setTitle(clubJoin.getTitle());
            clubJoinDTO.setCreatedAt(clubJoin.getCreateAt());
            clubJoinDTO.setMember(memberService.convertMemberToMemberDTO(clubJoin.getMember())); // Member 엔티티를 MemberDTO로 변환
            clubJoinDTO.setClub(clubService.convertClubToClubDTO(clubJoin.getClub()));
            clubJoinDTO.setStatus(clubJoin.getStatus());
            clubJoinDTOS.add(clubJoinDTO);
        }
        return clubJoinDTOS;
    }
    public ClubJoin approveClubJoin(Long clubJoinId) {
        ClubJoin clubJoin = clubJoinRepository.findById(clubJoinId)
                .orElseThrow(() -> new RuntimeException("ClubJoin not found with id: " + clubJoinId));
        clubJoin.setStatus(Status.APPROVAL);
        return clubJoinRepository.save(clubJoin);
    }

    public List<ClubJoin> approveMultipleClubJoins(List<Long> clubJoinIds) {
        List<ClubJoin> clubJoins = clubJoinRepository.findAllById(clubJoinIds);
        clubJoins.forEach(clubJoin -> clubJoin.setStatus(Status.APPROVAL));
        return clubJoinRepository.saveAll(clubJoins);
    }
}
