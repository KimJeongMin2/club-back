package com.clubcommunity.service;

import com.clubcommunity.domain.Post;
import com.clubcommunity.dto.PostDTO;
import com.clubcommunity.repository.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class PostService {
    private final PostRepository postRepository;
    private final MemberService memberService;
    public PostService(PostRepository postRepository, MemberService memberService){
        this.postRepository = postRepository;
        this.memberService = memberService;
    }

    public Post createPost(PostDTO postDTO, MultipartFile files) {
        Post post = new Post();
        post.setTitle(postDTO.getTitle());
        post.setMember(memberService.convertMemberDTOToMember(postDTO.getMember()));
        post.setContent(postDTO.getContent());
        post.setCategory(postDTO.getCategory());
        post.setNoticeVisibilityType(postDTO.getNoticeVisibilityType());
        try {
            post.setPhoto(files.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return postRepository.save(post);
    }
    public List<PostDTO> getAllPosts() {
        List<Post> posts = postRepository.findAll();
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

//    public Post updatePost(Long id, PostDTO postDto) {
//        Post post = getPostById(id);
//        post.setTitle(postDto.getTitle());
//        post.setContent(postDto.getContent());
//        post.setCategory(postDto.getCategory());
//        post.setPhoto(postDto.getPhoto());
//        post.setMember(memberService.convertMemberDTOToMember(postDto.getMember()));
//        return postRepository.save(post);
//    }
    public void deletePost(Long id) {
        Post post = getPostById(id);
        postRepository.delete(post);
    }
}
