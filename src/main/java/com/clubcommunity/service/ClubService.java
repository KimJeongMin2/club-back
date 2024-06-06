package com.clubcommunity.service;

import com.clubcommunity.domain.*;
import com.clubcommunity.dto.ClubDTO;
import com.clubcommunity.dto.MemberDTO;
import com.clubcommunity.dto.PostDTO;
import com.clubcommunity.repository.ClubMemberRepository;
import com.clubcommunity.repository.ClubRepository;
import com.clubcommunity.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubService {

    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final MemberService memberService;


    public Club makeClub(ClubDTO clubDTO) {

        Club club = Club.builder()
                .type(clubDTO.getType())
                .clubName(clubDTO.getClubName())
                .applicantName(clubDTO.getApplicantName())
                .applicantDepartment(clubDTO.getApplicantDepartment())
                .applicantId(clubDTO.getApplicantId())
                .applicantPhone(clubDTO.getApplicantPhone())
                .professorName(clubDTO.getProfessorName())
                .professorMajor(clubDTO.getProfessorMajor())
                .professorPhone(clubDTO.getProfessorPhone())
                .build();

        Club savedClub = clubRepository.save(club);

        ClubMember clubMember = ClubMember.builder()
                .member(memberService.convertMemberDTOToMember(clubDTO.getMember()))
                .club(savedClub)
                .memberStatus(MemberStatus.ACTIVITY)
                .status(Status.GO_OVER)
                .roleType(RoleType.MASTER)
                .build();

        clubMemberRepository.save(clubMember);

        return savedClub;
    }
    public List<ClubDTO> getAllClub() {
        List<ClubDTO> clubDTOs = new ArrayList<>();

        List<Club> clubs = clubRepository.findAll();

        for (Club club : clubs) {
            ClubDTO clubDTO = new ClubDTO();
            clubDTO.setClubId(club.getClubId());
            clubDTO.setType(club.getType());
            clubDTO.setClubName(club.getClubName());
            clubDTO.setApplicantName(club.getApplicantName());
            clubDTO.setApplicantDepartment(club.getApplicantDepartment());
            clubDTO.setApplicantId(club.getApplicantId());
            clubDTO.setApplicantPhone(club.getApplicantPhone());
            clubDTO.setProfessorName(club.getProfessorName());
            clubDTO.setProfessorMajor(club.getProfessorMajor());
            clubDTO.setProfessorPhone(club.getProfessorPhone());

            clubDTOs.add(clubDTO);
        }

        return clubDTOs;
    }

    public Club convertClubDTOToClub(ClubDTO clubDTO) {
        Club club = new Club();
        club.setClubId(clubDTO.getClubId());
        return club;
    }


    public ClubDTO convertClubToClubDTO(Club club) {
        ClubDTO clubDTO = new ClubDTO();
        clubDTO.setClubId(club.getClubId());
        return clubDTO;
    }

    public List<Club> getClubsForMember(Member member) {
        List<ClubMember> clubMembers = clubMemberRepository.findByMember(member);
        List<Club> clubs = new ArrayList<>();
        for (ClubMember clubMember : clubMembers) {
            clubs.add(clubMember.getClub());
        }
        return clubs;
    }


}
