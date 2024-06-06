package com.clubcommunity.service;

import com.clubcommunity.domain.ClubJoinMember;
import com.clubcommunity.domain.MemberStatus;
import com.clubcommunity.domain.Status;
import com.clubcommunity.dto.ClubJoinMemberDTO;
import com.clubcommunity.dto.ClubJoinRejectDTO;
import com.clubcommunity.repository.ClubJoinMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ClubJoinMemberService {
    private final ClubJoinMemberRepository clubJoinMemberRepository;

    public ClubJoinMemberService(ClubJoinMemberRepository clubJoinMemberRepository){
        this.clubJoinMemberRepository = clubJoinMemberRepository;
    }
    public ClubJoinMember approveClubJoin(Long clubJoinId) {
        ClubJoinMember clubJoin = clubJoinMemberRepository.findById(clubJoinId)
                .orElseThrow(() -> new RuntimeException("ClubJoin not found with id: " + clubJoinId));
        clubJoin.setStatus(Status.APPROVAL);
        return clubJoinMemberRepository.save(clubJoin);
    }
        public List<ClubJoinMember> approveMultipleClubJoins(List<Long> clubJoinIds) {
        List<ClubJoinMember> clubJoins = clubJoinMemberRepository.findAllById(clubJoinIds);
        clubJoins.forEach(clubJoin -> clubJoin.setStatus(Status.APPROVAL));
        return clubJoinMemberRepository.saveAll(clubJoins);
    }
    public ClubJoinMember rejectClubJoin(Long clubJoinId, String refusalReason) {
        ClubJoinMember clubJoin = clubJoinMemberRepository.findById(clubJoinId)
                .orElseThrow(() -> new RuntimeException("ClubJoin not found with id: " + clubJoinId));
        clubJoin.setStatus(Status.REFUSE);
        clubJoin.setRefusalReason(refusalReason);
        return clubJoinMemberRepository.save(clubJoin);
    }

    public List<ClubJoinMember> rejectMultipleClubJoins(List<ClubJoinRejectDTO> rejectionList) {
        List<Long> clubJoinIds = rejectionList.stream().map(ClubJoinRejectDTO::getClubJoinId).collect(Collectors.toList());
        List<ClubJoinMember> clubJoins = clubJoinMemberRepository.findAllById(clubJoinIds);
        clubJoins.forEach(clubJoin -> {
            String reason = rejectionList.stream()
                    .filter(rejection -> rejection.getClubJoinId().equals(clubJoin.getClubJoinMemberId()))
                    .findFirst()
                    .map(ClubJoinRejectDTO::getRefusalReason)
                    .orElse("No reason provided");
            clubJoin.setStatus(Status.REFUSE);
            clubJoin.setRefusalReason(reason);
        });
        return clubJoinMemberRepository.saveAll(clubJoins);
    }
    public ClubJoinMember withdrawClubJoinMember(Long clubJoinId) {
        ClubJoinMember clubJoinMember = clubJoinMemberRepository.findById(clubJoinId)
                .orElseThrow(() -> new RuntimeException("ClubJoinMember not found with id: " + clubJoinId));
        clubJoinMember.setMemberStatus(MemberStatus.WITHDRAW);
        clubJoinMember.setStatus(Status.REFUSE);
        return clubJoinMemberRepository.save(clubJoinMember);
    }

    public List<ClubJoinMember> withdrawMultipleClubJoinMembers(List<Long> clubJoinMemberIds) {
        List<ClubJoinMember> clubJoinMembers = clubJoinMemberRepository.findAllById(clubJoinMemberIds);
        clubJoinMembers.forEach(clubJoinMember -> {
            clubJoinMember.setMemberStatus(MemberStatus.WITHDRAW);
            clubJoinMember.setStatus(Status.REFUSE);
        });
        return clubJoinMemberRepository.saveAll(clubJoinMembers);
    }

}
