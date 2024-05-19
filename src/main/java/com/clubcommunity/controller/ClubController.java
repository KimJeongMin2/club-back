package com.clubcommunity.controller;

import com.clubcommunity.domain.Club;
import com.clubcommunity.dto.ClubDTO;
import com.clubcommunity.service.ClubService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/club")
public class ClubController {

    private final ClubService clubService;

    // 동아리 등록
    @PostMapping("")
    public ResponseEntity makeClub(Long memberPk, @RequestBody ClubDTO clubDTO) throws RuntimeException {

        Club club = clubService.makeClub(memberPk, clubDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(club);
    }
}
