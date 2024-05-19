package com.clubcommunity.controller;

import com.clubcommunity.domain.Category;
import com.clubcommunity.domain.Member;
import com.clubcommunity.domain.Post;
import com.clubcommunity.dto.MemberDTO;
import com.clubcommunity.dto.PostDTO;
import com.clubcommunity.service.ImageService;
import com.clubcommunity.service.MemberService;
import com.clubcommunity.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;
    private final ImageService imageService;
    private final MemberService memberService;

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

        System.out.println("Title: " + postDTO.getTitle());
        System.out.println("Content: " + postDTO.getContent());
        System.out.println("Category: " + postDTO.getCategory());
        System.out.println("Member: " + postDTO.getMember());
        System.out.println("NoticeVisibility: " + postDTO.getNoticeVisibilityType());
        System.out.println("files: " + files);
        Post savedPost = postService.createPost(postDTO, files);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPost);
    }


    @GetMapping
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        List<PostDTO> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id){
        Post post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody PostDTO postDTO){
//        Post updatedPost = postService.updatePost(id, postDTO);
//        return ResponseEntity.ok(updatedPost);
//    }

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


    @DeleteMapping("/{noticeId}")
    public ResponseEntity<Void> deletePost(@PathVariable("noticeId") Long noticeId){
        postService.deletePost(noticeId);
        return ResponseEntity.noContent().build();
    }


}
