package com.clubcommunity.controller;

import com.clubcommunity.domain.Member;
import com.clubcommunity.domain.Post;
import com.clubcommunity.dto.PictureDTO;
import com.clubcommunity.dto.PostDTO;
import com.clubcommunity.dto.VideoDTO;
import com.clubcommunity.repository.MemberRepository;
import com.clubcommunity.repository.PostRepository;
import com.clubcommunity.service.ImageService;
import com.clubcommunity.service.MemberService;
import com.clubcommunity.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.clubcommunity.domain.Category.PHOTO;
import static com.clubcommunity.domain.NoticeVisibilityType.ENTIRE;

@RestController
@RequestMapping("/api/posts")

public class PostController {
    private final PostService postService;
    private final ImageService imageService;
    private final MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    public PostController(PostService postService, ImageService imageService, MemberService memberService) {
        this.postService = postService;
        this.imageService = imageService;
        this.memberService = memberService;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Post> createPost(
            @RequestPart(value = "dto", required = false) PostDTO postDTO,
            @RequestPart(value = "photo", required = false) MultipartFile files
    ) {
        Post savedPost = postService.createPost(postDTO, files);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPost);
    }


    @GetMapping
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        List<PostDTO> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }
    @GetMapping("/recent/notices")
    public ResponseEntity<List<PostDTO>> getRecentNoticePosts() {
        List<PostDTO> posts = postService.getRecentNoticePosts();
        return ResponseEntity.ok(posts);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id){
        Post post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }
    @PutMapping(value = "/{noticeId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Post> updatePost(
            @PathVariable("noticeId") Long noticeId,
            @RequestPart(value = "dto", required = false) PostDTO postDTO,
            @RequestPart(value = "photo", required = false) MultipartFile files
    ) {
        System.out.println("Updating post with ID: " + noticeId);
        System.out.println("Title: " + postDTO.getTitle());
        System.out.println("Content: " + postDTO.getContent());
        System.out.println("Category: " + postDTO.getCategory());
        System.out.println("Member: " + postDTO.getMember());
        System.out.println("NoticeVisibility: " + postDTO.getNoticeVisibilityType());
        System.out.println("files: " + files);

        Post updatedPost = postService.updatePost(noticeId, postDTO, files);
        return ResponseEntity.ok(updatedPost);
    }


    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable("postId") Long postId){
        System.out.println("postId:"+postId);
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }


    @PostMapping(value = "/new-recruitment-posts", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Post> CreateMemberRecruitment(
            @RequestPart(value = "dto", required = false) PostDTO postDTO,
            @RequestPart(value = "photo", required = false) MultipartFile photo,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        System.out.println(postDTO.getClub());
        Post savedPost = postService.createMemberRecruitment(postDTO, photo, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPost);
    }
    
    @GetMapping("/recruitment")
    public ResponseEntity<List<PostDTO>> getAllPostsRecruitment() {
        List<PostDTO> posts = postService.getAllPostsRecruitment();
        return ResponseEntity.ok(posts);
    }
    @GetMapping("/recent/recruit")
    public ResponseEntity<List<PostDTO>> getRecentRecruitPosts() {
        List<PostDTO> posts = postService.getRecentRecruitPosts();
        return ResponseEntity.ok(posts);
    }

    @PutMapping(value = "/recruitment/{noticeId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Post> updateRecruitment(
            @PathVariable("noticeId") Long noticeId,
            @RequestPart(value = "dto", required = false) PostDTO postDTO,
            @RequestPart(value = "photo", required = false) MultipartFile files
    ) {
        System.out.println("Updating post with ID: " + noticeId);
        System.out.println("Title: " + postDTO.getTitle());
        System.out.println("Content: " + postDTO.getContent());
        System.out.println("Category: " + postDTO.getCategory());
        System.out.println("Member: " + postDTO.getMember());
        System.out.println("files: " + files);

        Post updatedPost = postService.updateRecruitment(noticeId, postDTO, files);
        return ResponseEntity.ok(updatedPost);
    }


    @DeleteMapping("/recruitment/{noticeId}")
    public ResponseEntity<Void> deleteRecruitment(@PathVariable("noticeId") Long noticeId){
        postService.deletePost(noticeId);
        return ResponseEntity.noContent().build();
    }


    //활동 영상 등록
    @PostMapping("/video")
    public ResponseEntity makeVideo(@RequestBody VideoDTO.Request videoDTO) throws RuntimeException {
        System.out.println("여기?");
        Post post = postService.makeVideo(videoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    //활동 영상 4개 최신순으로 조회
    @GetMapping("/main-video")
    public ResponseEntity<List<VideoDTO.Request>> get4VideoList() throws RuntimeException {
        List<VideoDTO.Request> videoList = postService.get4VideoList();
        return ResponseEntity.ok(videoList);
    }

    //활동 영상 리스트 조회
    @GetMapping("/video")
    public ResponseEntity<List<VideoDTO.Request>> getVideoList() throws RuntimeException {
        List<VideoDTO.Request> videoList = postService.getVideoList();
        return ResponseEntity.ok(videoList);
    }
    //활동 영상 조회
    @GetMapping("/video/{videoId}")
    public ResponseEntity<VideoDTO.Request> getVideo(@PathVariable("videoId") Long videoId) throws RuntimeException {
        VideoDTO.Request video = postService.getVideo(videoId);
        System.out.println("videoId" + videoId);
        return ResponseEntity.ok(video);
    }

    //활동 영상 수정
    @PutMapping("/video/{videoId}")
    public ResponseEntity<Post> updateVideo(
            @PathVariable("videoId") Long videoId,
            @RequestBody(required = false) VideoDTO.UpdateRequest videoDTO
    ) {
        System.out.println("Updating post with ID: " + videoId);
        System.out.println("Title: " + videoDTO.getTitle());
        System.out.println("Content: " + videoDTO.getContent());

        Post updatedPost = postService.updateVideo(videoId, videoDTO);
        return ResponseEntity.ok(updatedPost);
    }
    @GetMapping("/images")
    public ResponseEntity<List<PictureDTO.Request>> getPostImages() throws RuntimeException {
        List<PictureDTO.Request> postImages = postService.getPictureList();
        return ResponseEntity.ok(postImages);
    }
    @GetMapping("/main-picture")
    public ResponseEntity<List<PictureDTO.Request>> get4PictureList() throws RuntimeException {
        List<PictureDTO.Request> pictureList = postService.get4PictureList();
        return ResponseEntity.ok(pictureList);
    }
    @GetMapping("/picture")
    public ResponseEntity<List<PictureDTO.Request>> getPictureList() throws RuntimeException {
        List<PictureDTO.Request> pictureList = postService.getPictureList();
        return ResponseEntity.ok(pictureList);
    }
    @GetMapping("/picture/{pictureId}")
    public ResponseEntity<PictureDTO.Request> getPicture(@PathVariable("pictureId") Long pictureId) throws RuntimeException {
        PictureDTO.Request picture = postService.getPicture(pictureId);
        System.out.println("pictureId" + pictureId);
        return ResponseEntity.ok(picture);
    }
    @PutMapping("/picture/{pictureId}")
    public ResponseEntity<Post> updatePicture(
            @PathVariable("pictureId") Long pictureId,
            @RequestBody(required = false) PictureDTO.UpdateRequest pictureDTO
    ) {
        System.out.println("Updating post with ID: " + pictureId);
        System.out.println("Title: " + pictureDTO.getTitle());
        System.out.println("Content: " + pictureDTO.getContent());

        Post updatedPost = postService.updatePicture(pictureId, pictureDTO);
        return ResponseEntity.ok(updatedPost);
    }
    @PostMapping(value = "/activities", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Post> postActivity(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("userId") String userId,
            @RequestParam("imageFile") MultipartFile imageFile) throws IOException {

        // 작성자 Member 객체 찾기
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Invalid user"));;

        // Post 객체 생성 및 저장
        Post post = Post.builder()
                .member(member)
                .title(title)
                .content(content)
                .photo(imageFile.getBytes())
                .category(PHOTO)
                .noticeVisibilityType(ENTIRE)
                .build();
        post.setUploadFileName(imageFile.getOriginalFilename()); // 업로드된 파일 이름 설정
        Post savedPost = postRepository.save(post);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedPost);
    }

}
