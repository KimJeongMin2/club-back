package com.clubcommunity.service;

import com.clubcommunity.domain.Category;
import com.clubcommunity.domain.Post;
import com.clubcommunity.dto.PostDTO;
import com.clubcommunity.dto.VideoDTO;
import com.clubcommunity.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final MemberService memberService;
    public PostService(PostRepository postRepository, MemberService memberService){
        this.postRepository = postRepository;
        this.memberService = memberService;
    }

    public Post createPost(PostDTO postDTO, MultipartFile files) {
        Post.PostBuilder postBuilder = Post.builder()
                .title(postDTO.getTitle())
                .member(memberService.convertMemberDTOToMember(postDTO.getMember()))
                .content(postDTO.getContent())
                .category(postDTO.getCategory())
                .noticeVisibilityType(postDTO.getNoticeVisibilityType());

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
            postDTOs.add(postDTO);
        }
        return postDTOs;
    }

//    public List<Post> getAllPosts(){
//        return postRepository.findAll();
//    }
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

    public void deletePost(Long noticeId) {
        Post post = getPostById(noticeId);
        postRepository.delete(post);
    }

    public Post createMemberRecruitment(PostDTO postDTO, MultipartFile photo, MultipartFile file) {
        Post.PostBuilder postBuilder = Post.builder()
                .title(postDTO.getTitle())
                .member(memberService.convertMemberDTOToMember(postDTO.getMember()))
                .content(postDTO.getContent())
                .category(postDTO.getCategory());

        try {
            postBuilder.photo(photo.getBytes());
            postBuilder.file(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Post post = postBuilder.build();
        return postRepository.save(post);
    }



    //    public Post createMemberRecruitment(PostDTO postDTO, MultipartFile files) {
//        Post.PostBuilder postBuilder = Post.builder()
//                .title(postDTO.getTitle())
//                .member(memberService.convertMemberDTOToMember(postDTO.getMember()))
//                .content(postDTO.getContent())
//                .category(postDTO.getCategory());
//
//        try {
//            postBuilder.photo(files.getBytes());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        Post post = postBuilder.build();
//        return postRepository.save(post);
//    }
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
            postDTO.setMember(memberService.convertMemberToMemberDTO(post.getMember())); // Member 엔티티를 MemberDTO로 변환
            postDTO.setPhoto(post.getPhoto());
            postDTO.setFile(post.getFile());
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

        if (!files.isEmpty()) {
            try {
                post.setPhoto(files.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return postRepository.save(post);
    }


    public Post makeVideo(VideoDTO videoDTO) {
        Post post = Post.builder()
                .title(videoDTO.getTitle())
                .content(videoDTO.getContent())
                .category(Category.VIDEO)
                .member(memberService.convertMemberDTOToMember(videoDTO.getMember()))
                .build();

        Post savedPost = postRepository.save(post);
        log.info("savedPost: "  + savedPost.getPostId());

        return savedPost;
    }

    public List<VideoDTO> get4VideoList() {
        // Category가 VIDEO인 포스트 4개를 최신순으로 가져옴
        List<Post> videos = postRepository.findTop4ByCategoryOrderByCreateAtDesc(Category.VIDEO);
        List<VideoDTO> videoDTOS = new ArrayList<>();

        for (Post post : videos) {
            VideoDTO videoDTO = new VideoDTO();
            videoDTO.setTitle(post.getTitle());
            videoDTO.setContent(post.getContent());
            videoDTO.setMember(memberService.convertMemberToMemberDTO(post.getMember()));
            log.info("제목:"+videoDTO.getTitle());
            videoDTOS.add(videoDTO);
        }

        log.info("post개수:"+videoDTOS.size());


        return videoDTOS;
    }

    public List<VideoDTO> getVideoList() {
        // Category가 VIDEO인 포스트 최신순으로 가져옴
        List<Post> videos = postRepository.findByCategoryOrderByCreateAtDesc(Category.VIDEO);
        List<VideoDTO> videoDTOS = new ArrayList<>();

        for (Post post : videos) {
            VideoDTO videoDTO = new VideoDTO();
            videoDTO.setTitle(post.getTitle());
            videoDTO.setContent(post.getContent());
            videoDTO.setMember(memberService.convertMemberToMemberDTO(post.getMember()));
            videoDTOS.add(videoDTO);
        }

        log.info("post 개수:"+videoDTOS.size());


        return videoDTOS;
    }
}
