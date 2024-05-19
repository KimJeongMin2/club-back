package com.clubcommunity.service;

import com.clubcommunity.domain.*;
import com.clubcommunity.dto.ClubDTO;
import com.clubcommunity.repository.ClubMemberRepository;
import com.clubcommunity.repository.ClubRepository;
import com.clubcommunity.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClubService {

    private final ClubRepository clubRepository;
    private final MemberRepository memberRepository;
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


}
