package com.clubcommunity.service;

import com.clubcommunity.domain.*;
import com.clubcommunity.dto.ClubApplicationDTO;
import com.clubcommunity.dto.ClubDTO;
import com.clubcommunity.dto.ClubDetailDTO;
import com.clubcommunity.repository.ClubMemberRepository;
import com.clubcommunity.repository.ClubRepository;
import com.clubcommunity.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubService {

    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final MemberService memberService;
    private final MemberRepository memberRepository;


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
                .roleType(RoleType.MEMBER)
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

    public List<ClubDetailDTO> getMyClubs(Long studentId) {
        Member member = memberRepository.findById(studentId)
                .orElseThrow(()-> new RuntimeException("해당하는 유저를 찾을 수 없습니다."));

        List<ClubMember> clubMembers = clubMemberRepository.findByMemberAndRoleType(member, RoleType.MASTER);
        List<ClubDetailDTO> clubDTOs = new ArrayList<>();

        for (ClubMember clubMember : clubMembers) {
            Club club = clubMember.getClub();

            ClubDetailDTO clubDTO = ClubDetailDTO.builder()
                    .clubId(club.getClubId())
                    .clubName(club.getClubName())
                    .introduction(club.getIntroduction())
                    .history(club.getHistory())
                    .meetingTime(club.getMeetingTime())
                    .photo(club.getPhoto())
                    .staffList(club.getStaffList())
                    .build();

            clubDTOs.add(clubDTO);
        }
        return clubDTOs;
    }

    public Club makeClubBaseInfo(Long clubId, ClubDetailDTO clubDetailDTO, MultipartFile photo, MultipartFile file
    ) throws IOException {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(()-> new RuntimeException("해당하는 동아리를 찾을 수 없습니다."));

        club.updateBaseInfo(clubDetailDTO.getClubName(), clubDetailDTO.getIntroduction(), clubDetailDTO.getHistory()
                , clubDetailDTO.getMeetingTime(), photo, file);

        return clubRepository.save(club);
    }


    public Club getClubBaseInfo(Long clubId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(()-> new RuntimeException("해당하는 동아리를 찾을 수 없습니다."));

        return club;
    }

    public List<ClubApplicationDTO> getAllApplicationClubList() {
        List<ClubApplicationDTO> clubDTOs = new ArrayList<>();

        List<Club> clubs = clubRepository.findAll();

        for (Club club : clubs) {
            ClubMember clubMember = clubMemberRepository.findByClubAndStatus(club, Status.GO_OVER);

            if(clubMember != null) {
                ClubApplicationDTO clubDTO = ClubApplicationDTO.builder()
                        .clubId(club.getClubId())
                        .type(club.getType().getText())
                        .clubName(club.getClubName())
                        .applicantName(club.getApplicantName())
                        .applicantDepartment(club.getApplicantDepartment())
                        .applicantId(club.getApplicantId())
                        .applicantPhone(club.getApplicantPhone())
                        .professorName(club.getProfessorName())
                        .professorMajor(club.getProfessorMajor())
                        .professorPhone(club.getProfessorPhone())
                        .clubStatus(clubMember.getStatus().getText())
                        .build();

                clubDTOs.add(clubDTO);
            }
        }

        return clubDTOs;

    }

    public void approveClub(Long clubId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(()-> new RuntimeException("해당하는 동아리가 존재하지 않습니다."));

        ClubMember clubMember = clubMemberRepository.findByClubAndStatus(club, Status.GO_OVER);
        clubMember.approve();
        clubMemberRepository.save(clubMember);
    }

    public void approveAllClubs(List<Long> clubIds) {
        clubIds.forEach(clubId -> {
            Club club = clubRepository.findById(clubId)
                    .orElseThrow(() -> new RuntimeException("해당하는 동아리가 존재하지 않습니다."));

            ClubMember clubMember = clubMemberRepository.findByClubAndStatus(club, Status.GO_OVER);
            if (clubMember != null) {
                clubMember.approve();
            } else {
                throw new RuntimeException("해당 상태의 클럽 멤버가 존재하지 않습니다.");
            }
            clubMemberRepository.save(clubMember);
        });
    }

    public void rejectClub(Long clubId, String refusalReason) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(()-> new RuntimeException("해당하는 동아리가 존재하지 않습니다."));

        ClubMember clubMember = clubMemberRepository.findByClubAndStatus(club, Status.GO_OVER);
        clubMember.reject(refusalReason);
        clubMemberRepository.save(clubMember);
    }

}
