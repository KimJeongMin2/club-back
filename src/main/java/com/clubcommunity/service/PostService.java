package com.clubcommunity.service;

import com.clubcommunity.domain.*;
import com.clubcommunity.dto.*;
import com.clubcommunity.repository.ClubJoinMemberRepository;
import com.clubcommunity.repository.ClubJoinRepository;
import com.clubcommunity.repository.MemberRepository;
import com.clubcommunity.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final MemberService memberService;

    private final MemberRepository memberRepository;
    private final ClubService clubService;

    private final ClubJoinRepository clubJoinRepository;
    private final ClubJoinMemberRepository clubJoinMemberRepository;
//    public PostService(PostRepository postRepository, MemberService memberService, ClubService clubService){
//        this.postRepository = postRepository;
//        this.memberService = memberService;
//        this.clubService = clubService;
//    }

    public Post createPost(PostDTO postDTO, MultipartFile files) {
        Post.PostBuilder postBuilder = Post.builder()
                .title(postDTO.getTitle())
                .member(memberService.convertMemberDTOToMember(postDTO.getMember()))
                .content(postDTO.getContent())
                .category(postDTO.getCategory())
                .noticeVisibilityType(postDTO.getNoticeVisibilityType())
                .club(clubService.convertClubDTOToClub(postDTO.getClub()));
        try {
            postBuilder.photo(files.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Post post = postBuilder.build();
        return postRepository.save(post);
    }

    public List<PostDTO> getAllPosts() {
        List<Post> posts = postRepository.findByCategory(Category.NOTICE);
        List<PostDTO> postDTOs = new ArrayList<>();
        for (Post post : posts) {
            PostDTO postDTO = new PostDTO();
            postDTO.setPostId(post.getPostId());
            postDTO.setTitle(post.getTitle());
            postDTO.setContent(post.getContent());
            postDTO.setCategory(post.getCategory());
            postDTO.setNoticeVisibilityType(post.getNoticeVisibilityType());
            postDTO.setCreatedAt(post.getCreateAt());
            postDTO.setMember(memberService.convertMemberToMemberDTO(post.getMember())); // Member 엔티티를 MemberDTO로 변환
            postDTO.setPhoto(post.getPhoto());

            Club club = post.getClub();
            ClubSummaryDTO clubSummaryDTO = new ClubSummaryDTO();
            clubSummaryDTO.setClubId(club.getClubId());

            // 클럽에 소속된 멤버 추가
            List<ClubJoinMemberDTO> memberDTOs = new ArrayList<>();
            List<ClubJoin> clubJoins = clubJoinRepository.findByClub(club);

            for (ClubJoin clubJoin : clubJoins) {
                List<ClubJoinMember> clubJoinMembers = clubJoinMemberRepository.findByClubJoinAndMemberStatus(clubJoin, MemberStatus.ACTIVITY);

                for (ClubJoinMember clubJoinMember : clubJoinMembers) {
                    if (clubJoinMember.getStatus() == Status.APPROVAL) {
                        ClubJoinMemberDTO memberDTO = new ClubJoinMemberDTO();
                        memberDTO.setMember(memberService.convertMemberToMemberDTO(clubJoinMember.getMember()));
                        memberDTO.setMemberStatus(clubJoinMember.getMemberStatus());
                        memberDTOs.add(memberDTO);
                    }
                }
            }
            clubSummaryDTO.setMembers(memberDTOs); // 클럽에 소속된 멤버들 추가
            postDTO.setClubSummaryDTO(clubSummaryDTO); // 필요한 필드만 설정한 ClubSummaryDTO 설정
            postDTOs.add(postDTO);
        }
        return postDTOs;
    }


//    public List<PostDTO> getAllPosts() {
//        List<Post> posts = postRepository.findByCategory(Category.NOTICE);
//        List<PostDTO> postDTOs = new ArrayList<>();
//        for (Post post : posts) {
//            PostDTO postDTO = new PostDTO();
//            postDTO.setPostId(post.getPostId());
//            postDTO.setTitle(post.getTitle());
//            postDTO.setContent(post.getContent());
//            postDTO.setCategory(post.getCategory());
//            postDTO.setNoticeVisibilityType(post.getNoticeVisibilityType());
//            postDTO.setCreatedAt(post.getCreateAt());
//            postDTO.setMember(memberService.convertMemberToMemberDTO(post.getMember())); // Member 엔티티를 MemberDTO로 변환
//            postDTO.setPhoto(post.getPhoto());
//            postDTO.setClub(clubService.convertClubToClubDTO(post.getClub()));
//
//            Club club = post.getClub();
//            ClubDTO clubDTO = clubService.convertClubToClubDTO(club);
//
//            // 클럽에 소속된 멤버 추가
//            List<ClubJoinMemberDTO> memberDTOs = new ArrayList<>();
//            List<ClubJoin> clubJoins = clubJoinRepository.findByClub(club);
//
//            for (ClubJoin clubJoin : clubJoins) {
//                List<ClubJoinMember> clubJoinMembers = clubJoinMemberRepository.findByClubJoinAndMemberStatus(clubJoin, MemberStatus.ACTIVITY);
//
//                for (ClubJoinMember clubJoinMember : clubJoinMembers) {
//                    if (clubJoinMember.getStatus() == Status.APPROVAL) {
//                        ClubJoinMemberDTO memberDTO = new ClubJoinMemberDTO();
//                        memberDTO.setMember(memberService.convertMemberToMemberDTO(clubJoinMember.getMember()));
//                        memberDTO.setMemberStatus(clubJoinMember.getMemberStatus());
//                        memberDTOs.add(memberDTO);
//                    }
//                }
//            }
//            clubDTO.setMembers(memberDTOs); // 클럽에 소속된 멤버들 추가
//            postDTO.setClub(clubDTO); //
//            postDTOs.add(postDTO);
//        }
//        return postDTOs;
//    }

    public List<PostDTO> getRecentNoticePosts() {
        Pageable topFive = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createAt"));
        Page<Post> page = postRepository.findByCategoryOrderByCreateAtDesc(Category.NOTICE, topFive);
        List<Post> posts = page.getContent();

        List<PostDTO> postDTOs = new ArrayList<>();
        for (Post post : posts) {
            PostDTO postDTO = new PostDTO();
            postDTO.setPostId(post.getPostId());
            postDTO.setTitle(post.getTitle());
            postDTO.setContent(post.getContent());
            postDTO.setCategory(post.getCategory());
            postDTO.setNoticeVisibilityType(post.getNoticeVisibilityType());
            postDTO.setCreatedAt(post.getCreateAt());
            postDTO.setMember(memberService.convertMemberToMemberDTO(post.getMember()));
            postDTO.setPhoto(post.getPhoto());
            postDTOs.add(postDTO);
        }
        return postDTOs;
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found with id: " + id));
    }

    public Post updatePost(Long postId, PostDTO postDTO, MultipartFile files) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));

        post.setTitle(postDTO.getTitle());
        post.setMember(memberService.convertMemberDTOToMember(postDTO.getMember()));
        post.setContent(postDTO.getContent());
        post.setCategory(postDTO.getCategory());
        post.setNoticeVisibilityType(postDTO.getNoticeVisibilityType());

        if (!files.isEmpty()) {
            try {
                post.setPhoto(files.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return postRepository.save(post);
    }

    public void deletePost(Long postId) {
        Post post = getPostById(postId);
        postRepository.delete(post);
    }


    public Post createMemberRecruitment(PostDTO postDTO, MultipartFile photo, MultipartFile file) {
        System.out.println(postDTO.toString());

        String memberUid = postDTO.getMember().getUid();
        System.out.println("memberUid = " + memberUid);
        Member member = memberService.findByUid(memberUid);
        if (member == null) {
            throw new IllegalArgumentException("Member with UID " + memberUid + " not found");
        }

        Post.PostBuilder postBuilder = Post.builder()
                .title(postDTO.getTitle())
                .member(memberService.convertMemberDTOToMember(postDTO.getMember()))
                .content(postDTO.getContent())
                .category(postDTO.getCategory())
                .club(clubService.convertClubDTOToClub(postDTO.getClub()));

        try {
            postBuilder.photo(photo.getBytes());
            postBuilder.file(file.getBytes());
            postBuilder.uploadFileName(file.getOriginalFilename());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Post post = postBuilder.build();

        return postRepository.save(post);
    }

    public List<PostDTO> getAllPostsRecruitment() {
        List<Post> posts = postRepository.findByCategory(Category.RECRUIT);
        List<PostDTO> postDTOs = new ArrayList<>();
        for (Post post : posts) {
            PostDTO postDTO = new PostDTO();
            postDTO.setPostId(post.getPostId());
            postDTO.setTitle(post.getTitle());
            postDTO.setContent(post.getContent());
            postDTO.setCategory(post.getCategory());
            postDTO.setCreatedAt(post.getCreateAt());
            postDTO.setUploadFileName(post.getUploadFileName());
            postDTO.setMember(memberService.convertMemberToMemberDTO(post.getMember()));
            postDTO.setPhoto(post.getPhoto());
            postDTO.setFile(post.getFile());
            postDTO.setClub(clubService.convertClubToClubDTO(post.getClub()));
            postDTOs.add(postDTO);
        }
        return postDTOs;
    }

//    public List<PostDTO> getAllPostsRecruitment() {
//        List<Post> posts = postRepository.findByCategory(Category.RECRUIT);
//        List<PostDTO> postDTOs = new ArrayList<>();
//        for (Post post : posts) {
//            PostDTO postDTO = new PostDTO();
//            postDTO.setPostId(post.getPostId());
//            postDTO.setTitle(post.getTitle());
//            postDTO.setContent(post.getContent());
//            postDTO.setCategory(post.getCategory());
//            postDTO.setCreatedAt(post.getCreateAt());
//            //System.out.println("post.getStoredFileName() = " + post.getStoredFileName());
//            postDTO.setUploadFileName(post.getUploadFileName());
//            postDTO.setMember(memberService.convertMemberToMemberDTO(post.getMember())); // Member 엔티티를 MemberDTO로 변환
//            postDTO.setPhoto(post.getPhoto());
//            postDTO.setFile(post.getFile());
//            postDTO.setClub(clubService.convertClubToClubDTO(post.getClub()));
//            postDTOs.add(postDTO);
//        }
//        return postDTOs;
//    }

    public List<PostDTO> getRecentRecruitPosts() {
        Pageable topFive = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createAt"));
        Page<Post> page = postRepository.findByCategoryOrderByCreateAtDesc(Category.RECRUIT, topFive);
        List<Post> posts = page.getContent();

        List<PostDTO> postDTOs = new ArrayList<>();
        for (Post post : posts) {
            PostDTO postDTO = new PostDTO();
            postDTO.setPostId(post.getPostId());
            postDTO.setTitle(post.getTitle());
            postDTO.setContent(post.getContent());
            postDTO.setCategory(post.getCategory());
            postDTO.setNoticeVisibilityType(post.getNoticeVisibilityType());
            postDTO.setCreatedAt(post.getCreateAt());
            postDTO.setMember(memberService.convertMemberToMemberDTO(post.getMember()));
            postDTO.setPhoto(post.getPhoto());
            postDTOs.add(postDTO);
        }
        return postDTOs;
    }

    public Post updateRecruitment(Long postId, PostDTO postDTO, MultipartFile files) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));

        post.setTitle(postDTO.getTitle());
        post.setMember(memberService.convertMemberDTOToMember(postDTO.getMember()));
        post.setContent(postDTO.getContent());
        post.setCategory(postDTO.getCategory());
        post.setUploadFileName(postDTO.getUploadFileName());
        if (!files.isEmpty()) {
            try {
                post.setPhoto(files.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return postRepository.save(post);
    }


    public Post makeVideo(VideoDTO.Request videoDTO) {
        String memberUid = videoDTO.getMember().getUid();
        log.info("memberUid = {}", memberUid);

        Member member = memberService.findByUid(memberUid);
        if (member == null) {
            throw new RuntimeException("해당하는 회원이 존재하지 않습니다.");
        }
        Post post = Post.builder()
                .title(videoDTO.getTitle())
                .content(videoDTO.getContent())
                .category(Category.VIDEO)
//                .member(memberService.convertMemberDTOToMember(videoDTO.getMember()))
                .member(member)
                .build();

        Post savedPost = postRepository.save(post);
        log.info("savedPost: "  + savedPost.getPostId());

        return savedPost;
    }

    public List<VideoDTO.Request> get4VideoList() {
        // Category가 VIDEO인 포스트 4개를 최신순으로 가져옴
        List<Post> videos = postRepository.findTop4ByCategoryOrderByCreateAtDesc(Category.VIDEO);
        List<VideoDTO.Request> videoDTOS = new ArrayList<>();

        for (Post post : videos) {
            VideoDTO.Request videoDTO = VideoDTO.Request.builder()
                    .postId(post.getPostId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .member(memberService.convertMemberToMemberDTO(post.getMember()))
                    .createdAt(post.getCreateAt())
                    .updateAt(post.getUpdateAt())
                    .category(post.getCategory())
                    .build();
            videoDTOS.add(videoDTO);
        }

        log.info("post개수:" + videoDTOS.size());

        return videoDTOS;
    }


    public List<VideoDTO.Request> getVideoList() {
        // Category가 VIDEO인 포스트 최신순으로 가져옴
        List<Post> videos = postRepository.findByCategoryOrderByCreateAtDesc(Category.VIDEO);
        List<VideoDTO.Request> videoDTOS = new ArrayList<>();

        for (Post post : videos) {
            VideoDTO.Request videoDTO = VideoDTO.Request.builder()
                    .postId(post.getPostId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .member(memberService.convertMemberToMemberDTO(post.getMember()))
                    .createdAt(post.getCreateAt())
                    .updateAt(post.getUpdateAt())
                    .category(post.getCategory())
                    .build();
            videoDTOS.add(videoDTO);
        }

        log.info("post 개수:"+videoDTOS.size());

        return videoDTOS;
    }

    public Post updateVideo(Long postId, VideoDTO.UpdateRequest videoDTO) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("해당하는 Post를 찾을 수 없습니다."));

        post.updateVideo(videoDTO.getTitle(), videoDTO.getContent());

        postRepository.save(post);

        return post;
    }

    public VideoDTO.Request getVideo(Long videoId) {
        Post post = postRepository.findById(videoId)
                .orElseThrow(() -> new RuntimeException("해당하는 Post를 찾을 수 없습니다."));

        VideoDTO.Request videoDTO = VideoDTO.Request.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .member(memberService.convertMemberToMemberDTO(post.getMember()))
                .createdAt(post.getCreateAt())
                .updateAt(post.getUpdateAt())
                .category(post.getCategory())
                .build();

        return videoDTO;

    }
//    public List<PictureDTO.Request> getPictureList() {
//        List<Post> picture = postRepository.findByCategoryOrderByCreateAtDesc(Category.PHOTO);
//        List<PictureDTO.Request> pictureDTOS = new ArrayList<>();
//
//        for (Post post : picture) {
//            PictureDTO.Request pictureDTO = PictureDTO.Request.builder()
//                    .postId(post.getPostId())
//                    .title(post.getTitle())
//                    .content(post.getContent())
//                    .member(memberService.convertMemberToMemberDTO(post.getMember()))
//                    .createdAt(post.getCreateAt())
//                    .updateAt(post.getUpdateAt())
//                    .category(post.getCategory())
//                    .photo(post.getPhoto())
//                    .build();
//            pictureDTOS.add(pictureDTO);
//        }
//
//        log.info("post 개수:"+pictureDTOS.size());
//
//        return pictureDTOS;
//    }
    public List<PictureDTO.Request> getPictureList() {
        List<Post> picture = postRepository.findByCategoryOrderByCreateAtDesc(Category.PHOTO);
        List<PictureDTO.Request> pictureDTOS = new ArrayList<>();
        for (Post post : picture) {
            PictureDTO.Request pictureDTO = PictureDTO.Request.builder()
                    .postId(post.getPostId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .member(memberService.convertMemberToMemberDTO(post.getMember()))
                    .createdAt(post.getCreateAt())
                    .updateAt(post.getUpdateAt())
                    .category(post.getCategory())
                    .photoBase64(Base64.getEncoder().encodeToString(post.getPhoto())) // Base64 인코딩
                    .build();
            pictureDTOS.add(pictureDTO);
        }
        log.info("post 개수:" + pictureDTOS.size());
        return pictureDTOS;
    }
    public Post updatePicture(Long postId, PictureDTO.UpdateRequest pictureDTO) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("해당하는 Post를 찾을 수 없습니다."));

        post.updateVideo(pictureDTO.getTitle(), pictureDTO.getContent());

        return postRepository.save(post);
    }
    public List<PictureDTO.Request> get4PictureList() {
        List<Post> pictures = postRepository.findTop4ByCategoryOrderByCreateAtDesc(Category.PHOTO);
        List<PictureDTO.Request> pictureDTOS = new ArrayList<>();

        for (Post post : pictures) {
            PictureDTO.Request pictureDTO = PictureDTO.Request.builder()
                    .postId(post.getPostId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .member(memberService.convertMemberToMemberDTO(post.getMember()))
                    .createdAt(post.getCreateAt())
                    .updateAt(post.getUpdateAt())
                    .category(post.getCategory())
                    .photoBase64(Base64.getEncoder().encodeToString(post.getPhoto()))
                    .build();
            pictureDTOS.add(pictureDTO);
        }

        log.info("post개수:" + pictureDTOS.size());

        return pictureDTOS;
    }
    public PictureDTO.Request getPicture(Long photoId) {
        Post post = postRepository.findById(photoId)
                .orElseThrow(() -> new RuntimeException("해당하는 Post를 찾을 수 없습니다."));
        PictureDTO.Request pictureDTO = PictureDTO.Request.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .member(memberService.convertMemberToMemberDTO(post.getMember()))
                .createdAt(post.getCreateAt())
                .updateAt(post.getUpdateAt())
                .category(post.getCategory())
                .photoBase64(Base64.getEncoder().encodeToString(post.getPhoto()))
                .build();
        return pictureDTO;
    }
//    public PictureDTO.Request getPicture(Long photoId) {
//        Post post = postRepository.findById(photoId)
//                .orElseThrow(() -> new RuntimeException("해당하는 Post를 찾을 수 없습니다."));
//
//        PictureDTO.Request pictureDTO = PictureDTO.Request.builder()
//                .postId(post.getPostId())
//                .title(post.getTitle())
//                .content(post.getContent())
//                .member(memberService.convertMemberToMemberDTO(post.getMember()))
//                .createdAt(post.getCreateAt())
//                .updateAt(post.getUpdateAt())
//                .category(post.getCategory())
//                .build();
//
//        return pictureDTO;
//
//    }
}
