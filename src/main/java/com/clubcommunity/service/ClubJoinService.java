package com.clubcommunity.service;

import com.clubcommunity.domain.*;
import com.clubcommunity.dto.ClubDTO;
import com.clubcommunity.dto.ClubJoinDTO;
import com.clubcommunity.dto.ClubJoinMemberDTO;
import com.clubcommunity.dto.PostDTO;
import com.clubcommunity.repository.ClubJoinMemberRepository;
import com.clubcommunity.repository.ClubJoinRepository;
import com.clubcommunity.repository.ClubMemberRepository;
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
    private final ClubJoinMemberRepository clubJoinMemberRepository;
    private final MemberService memberService;
    private final ClubService clubService;

    public ClubJoinService(ClubJoinRepository clubJoinRepository, ClubJoinMemberRepository clubJoinMemberRepository, MemberService memberService, ClubService clubService){
        this.clubJoinRepository = clubJoinRepository;
        this.clubJoinMemberRepository = clubJoinMemberRepository;
        this.memberService = memberService;
        this.clubService = clubService;
    }
    public ClubJoin createClubJoin(ClubJoinDTO clubJoinDTO, MultipartFile files) {
        Member member = memberService.convertMemberDTOToMember(clubJoinDTO.getMember());

        ClubJoin.ClubJoinBuilder clubJoinBuilder = ClubJoin.builder()
                .club(clubService.convertClubDTOToClub(clubJoinDTO.getClub()))
                .title(clubJoinDTO.getTitle())
                .createAt(clubJoinDTO.getCreatedAt());

        try {
            clubJoinBuilder.file(files.getBytes());
            clubJoinBuilder.uploadFileName(files.getOriginalFilename());
        } catch (IOException e) {
            log.error("Error uploading file: {}", e.getMessage());
        }

        ClubJoin clubJoin = clubJoinBuilder.build();
        clubJoin = clubJoinRepository.save(clubJoin);
        System.out.println("clubJoin = " + clubJoin);

        ClubJoinMember clubJoinMember = ClubJoinMember.builder()
                .clubJoin(clubJoin)
                .member(member) // Member 설정
                .memberStatus(MemberStatus.ACTIVITY)
                .status(Status.GO_OVER)
                .refusalReason(clubJoinDTO.getRefusalReason())
                .roleType(RoleType.MEMBER)
                .build();
        clubJoinMemberRepository.save(clubJoinMember);

        return clubJoin;
    }

    public List<ClubJoinDTO> getAllClubJoinForUser(String uid) {
        Member member = memberService.findByUid(uid);
        System.out.println("member = " + member);
        List<Club> clubs = clubService.getClubsForMember(member);

        List<ClubJoinDTO> clubJoinDTOS = new ArrayList<>();
        for (Club club : clubs) {
            List<ClubJoin> clubJoins = clubJoinRepository.findByClub(club);
            for (ClubJoin clubJoin : clubJoins) {
                List<ClubJoinMember> clubJoinMembers = clubJoinMemberRepository.findByClubJoinAndStatus(clubJoin, Status.GO_OVER);
                for (ClubJoinMember clubJoinMember : clubJoinMembers) {
                    System.out.println("clubJoinMember.getMember() = " + clubJoinMember.getMember());
                    ClubJoinDTO clubJoinDTO = new ClubJoinDTO();
                    clubJoinDTO.setClubJoinId(clubJoin.getClubJoinId());
                    clubJoinDTO.setTitle(clubJoin.getTitle());
                    clubJoinDTO.setCreatedAt(clubJoin.getCreateAt());
                    clubJoinDTO.setMember(memberService.convertMemberToMemberDTO(clubJoinMember.getMember()));
                    clubJoinDTO.setClub(clubService.convertClubToClubDTO(clubJoin.getClub()));
                    clubJoinDTO.setStatus(clubJoinMember.getStatus());
                    clubJoinDTO.setFile(clubJoin.getFile());
                    clubJoinDTO.setUploadFileName(clubJoin.getUploadFileName());
                    clubJoinDTOS.add(clubJoinDTO);
                }
            }
        }
        return clubJoinDTOS;
    }

//    public ClubJoin createClubJoin(ClubJoinDTO clubJoinDTO, MultipartFile files) {
//        ClubJoin.ClubJoinBuilder clubJoinBuilder = ClubJoin.builder()
//                .member(memberService.convertMemberDTOToMember(clubJoinDTO.getMember()))
//                .club(clubService.convertClubDTOToClub(clubJoinDTO.getClub()))
//                .title(clubJoinDTO.getTitle())
//                .createAt(clubJoinDTO.getCreatedAt());
//
//        try {
//            clubJoinBuilder.file(files.getBytes());
//        } catch (IOException e) {
//            log.error("Error uploading file: {}", e.getMessage());
//        }
//
//        ClubJoin clubJoin = clubJoinBuilder.build();
//        clubJoin = clubJoinRepository.save(clubJoin);
//        System.out.println("clubJoin = " + clubJoin);
//        // ClubMember 생성 및 저장
//        ClubJoinMember clubJoinMember = ClubJoinMember.builder()
//                .clubJoin(clubJoin)
//                .memberStatus(MemberStatus.ACTIVITY)
//                .status(Status.GO_OVER)
//                .refusalReason(clubJoinDTO.getRefusalReason())
//                .roleType(RoleType.MEMBER)
//                .build();
//        clubJoinMemberRepository.save(clubJoinMember);
//
//        return clubJoin;
//    }

//    public List<ClubJoinDTO> getAllClubJoinForUser(Long userId) {
//        Member member = memberService.findMemberById(userId);
//        System.out.println("member = " + member);
//        List<Club> clubs = clubService.getClubsForMember(member);
//
//        List<ClubJoinDTO> clubJoinDTOS = new ArrayList<>();
//        for (Club club : clubs) {
//            List<ClubJoin> clubJoins = clubJoinRepository.findByClub(club);
//            for (ClubJoin clubJoin : clubJoins) {
//                List<ClubJoinMember> clubJoinMembers = clubJoinMemberRepository.findByClubJoinAndStatus(clubJoin, Status.GO_OVER);
//                for (ClubJoinMember clubJoinMember : clubJoinMembers) {
//                    ClubJoinDTO clubJoinDTO = new ClubJoinDTO();
//                    clubJoinDTO.setClubJoinId(clubJoin.getClubJoinId());
//                    clubJoinDTO.setTitle(clubJoin.getTitle());
//                    clubJoinDTO.setCreatedAt(clubJoin.getCreateAt());
//                    clubJoinDTO.setMember(memberService.convertMemberToMemberDTO(clubJoin.getMember()));
//                    clubJoinDTO.setClub(clubService.convertClubToClubDTO(clubJoin.getClub()));
//                    clubJoinDTO.setStatus(clubJoinMember.getStatus());
//                    clubJoinDTO.setFile(clubJoin.getFile());
//                    clubJoinDTOS.add(clubJoinDTO);
//                }
//            }
//        }
//        return clubJoinDTOS;
//    }

    public List<ClubJoinMemberDTO> getApprovedMembersForClub(String userId) {
        Member clubOwner = memberService.findMemberById(userId);
        List<Club> clubs = clubService.getClubsForMember(clubOwner);

        List<ClubJoinMemberDTO> approvedMembers = new ArrayList<>();
        for (Club club : clubs) {
            List<ClubJoin> clubJoins = clubJoinRepository.findByClub(club);
            for (ClubJoin clubJoin : clubJoins) {
                List<ClubJoinMember> clubJoinMembers = clubJoinMemberRepository.findByClubJoinAndStatus(clubJoin, Status.APPROVAL);
                for (ClubJoinMember clubJoinMember : clubJoinMembers) {
                    ClubJoinMemberDTO clubJoinMemberDTO = new ClubJoinMemberDTO();
                    clubJoinMemberDTO.setClubJoinId(clubJoin.getClubJoinId());
                    clubJoinMemberDTO.setMember(memberService.convertMemberToMemberDTO(clubJoinMember.getMember()));
                    clubJoinMemberDTO.setStatus(clubJoinMember.getStatus());
                    clubJoinMemberDTO.setMemberStatus(clubJoinMember.getMemberStatus());
                    ClubDTO clubDTO = new ClubDTO();
                    clubDTO.setClubId(club.getClubId());
                    clubDTO.setClubName(club.getClubName());
                    clubJoinMemberDTO.setClub(clubDTO);

                    approvedMembers.add(clubJoinMemberDTO);
                }
            }
        }
        return approvedMembers;
    }




//public List<ClubJoinDTO> getAllClubJoin() {
//    List<ClubJoinMember> clubJoinMembers = clubJoinMemberRepository.findByStatus(Status.GO_OVER);
//    System.out.println("clubMembers = " + clubJoinMembers);
//    List<ClubJoinDTO> clubJoinDTOS = new ArrayList<>();
//    for (ClubJoinMember clubJoinMember : clubJoinMembers) {
//        ClubJoin clubJoin = clubJoinMember.getClubJoin();
//        System.out.println("clubJoin = " + clubJoin);
//        ClubJoinDTO clubJoinDTO = new ClubJoinDTO();
//        clubJoinDTO.setClubJoinId(clubJoin.getClubJoinId());
//        clubJoinDTO.setTitle(clubJoin.getTitle());
//        clubJoinDTO.setCreatedAt(clubJoin.getCreateAt());
//        clubJoinDTO.setMember(memberService.convertMemberToMemberDTO(clubJoin.getMember())); // Member 엔티티를 MemberDTO로 변환
//        clubJoinDTO.setClub(clubService.convertClubToClubDTO(clubJoin.getClub()));
//        clubJoinDTO.setStatus(clubJoinMember.getStatus());
//        clubJoinDTO.setFile(clubJoin.getFile());
//        clubJoinDTOS.add(clubJoinDTO);
//    }
//    return clubJoinDTOS;
//}

//    public ClubJoin approveClubJoin(Long clubJoinId) {
//        ClubJoin clubJoin = clubJoinRepository.findById(clubJoinId)
//                .orElseThrow(() -> new RuntimeException("ClubJoin not found with id: " + clubJoinId));
//        clubJoin.setStatus(Status.APPROVAL);
//        return clubJoinRepository.save(clubJoin);
//    }
//
//    public List<ClubJoin> approveMultipleClubJoins(List<Long> clubJoinIds) {
//        List<ClubJoin> clubJoins = clubJoinRepository.findAllById(clubJoinIds);
//        clubJoins.forEach(clubJoin -> clubJoin.setStatus(Status.APPROVAL));
//        return clubJoinRepository.saveAll(clubJoins);
//    }
//
//    public ClubJoin rejectClubJoin(Long clubJoinId) {
//        ClubJoin clubJoin = clubJoinRepository.findById(clubJoinId)
//                .orElseThrow(() -> new RuntimeException("ClubJoin not found with id: " + clubJoinId));
//        clubJoin.setStatus(Status.REFUSE);
//        return clubJoinRepository.save(clubJoin);
//    }
//
//    public List<ClubJoin> rejectMultipleClubJoins(List<Long> clubJoinIds) {
//        List<ClubJoin> clubJoins = clubJoinRepository.findAllById(clubJoinIds);
//        clubJoins.forEach(clubJoin -> clubJoin.setStatus(Status.REFUSE));
//        return clubJoinRepository.saveAll(clubJoins);
//    }
}
