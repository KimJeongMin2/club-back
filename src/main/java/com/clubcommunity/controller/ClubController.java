package com.clubcommunity.controller;

import com.clubcommunity.domain.Club;
import com.clubcommunity.dto.*;
import com.clubcommunity.service.ClubService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/club")
public class ClubController {

    private final ClubService clubService;

    // 동아리 등록
    @PostMapping("")
    public ResponseEntity makeClub(@RequestBody ClubDTO clubDTO) throws RuntimeException {

        Club club = clubService.makeClub(clubDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(club);
    }
    
    @GetMapping
    public ResponseEntity<List<ClubDTO>> getAllClubs() {
        List<ClubDTO> clubs = clubService.getAllClub();
        return ResponseEntity.ok(clubs);
    }

    // 관리자의 내 동아리 조회
    @GetMapping("/my/{studentId}")
    public ResponseEntity<List<ClubDetailDTO>> getMyClubs(@PathVariable("studentId") Long studentId) {
        List<ClubDetailDTO> clubDTOS = clubService.getMyClubs(studentId);
        return ResponseEntity.ok(clubDTOS);
    }

//    @GetMapping("/detail/{clubId}")
//    public ResponseEntity<List<ClubDetailDTO>> getClubDetail(@PathVariable Long clubId) {
//        List<ClubDetailDTO> clubDTOS = clubService.getClubDetail(clubId);
//        return ResponseEntity.ok(clubDTOS);
//    }

    @PostMapping(value = "/detail/{clubId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Club> makeClubBaseInfo(
            @PathVariable("clubId") Long clubId,
            @RequestPart(value = "dto", required = false) ClubDetailDTO clubDetailDTO,
            @RequestPart(value = "registration", required = false) MultipartFile registration,
            @RequestPart(value = "photo", required = false) MultipartFile photo,
            @RequestPart(value = "staffList", required = false) MultipartFile staffList
    ) throws IOException {
        System.out.println("Name: " + clubDetailDTO.getClubName());
        Club savedClub = clubService.makeClubBaseInfo(clubId, clubDetailDTO, registration, photo, staffList);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedClub);
    }

    @GetMapping( "/detail/{clubId}")
    public ResponseEntity<Club> getClubBaseInfo(
            @PathVariable("clubId") Long clubId
    ) throws IOException {
        Club club = clubService.getClubBaseInfo(clubId);
        return ResponseEntity.ok(club);
    }

    @GetMapping("/download/{type}/{clubId}")
    public ResponseEntity<byte[]> downloadFile(
            @PathVariable String type,
            @PathVariable Long clubId) throws IOException {
        Club club = clubService.getClubBaseInfo(clubId);
        byte[] fileContent;
        String fileName;

        switch (type) {
            case "registration":
                fileContent = club.getRegistration();
                fileName = "동아리 가입신청서.hwp";
                break;
            case "staffList":
                fileContent = club.getStaffList();
                fileName = "동아리 임원 명단.hwp";
                break;
            default:
                return ResponseEntity.notFound().build();
        }

        if (fileContent == null) {
            return ResponseEntity.notFound().build();
        }

        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.builder("attachment").filename(encodedFileName).build());

        return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
    }

    @GetMapping("/applicationClubList")
    public ResponseEntity<List<ClubApplicationDTO>> getAllApplicationClubList() {
        List<ClubApplicationDTO> clubs = clubService.getAllApplicationClubList();
        return ResponseEntity.ok(clubs);
    }

    @PutMapping("/{clubId}/approve")
    public ResponseEntity<Club> approveClub(@PathVariable Long clubId) {
        clubService.approveClub(clubId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/approve")
    public ResponseEntity<List<Club>> approveMultipleClubJoins(@RequestBody List<Long> clubIds) {
        clubService.approveAllClubs(clubIds);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{clubId}/reject")
    public ResponseEntity<Club> rejectClub(@PathVariable Long clubId, @RequestBody String refusalReason) {
        clubService.rejectClub(clubId, refusalReason);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


}
