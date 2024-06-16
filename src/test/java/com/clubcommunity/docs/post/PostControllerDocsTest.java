package com.clubcommunity.docs.post;
import com.clubcommunity.controller.PostController;
import com.clubcommunity.docs.RestDocsSupport;
import com.clubcommunity.domain.*;
import com.clubcommunity.dto.*;
import com.clubcommunity.service.ImageService;
import com.clubcommunity.service.MemberService;
import com.clubcommunity.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(PostController.class)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class PostControllerDocsTest extends RestDocsSupport {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostService postService;

    @MockBean
    private ImageService imageService;

    @MockBean
    private MemberService memberService;

    @Override
    protected Object initController() {
        return new PostController(postService, imageService, memberService);
    }

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(document("{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .build();
    }

    @Test
    public void deletePost() throws Exception {
        Long postId = 1L;

        // Mocking the service method
        doNothing().when(postService).deletePost(postId);

        mockMvc.perform(delete("/api/posts/{postId}", postId))
                .andExpect(status().isNoContent())
                .andDo(document("delete-post",
                        pathParameters(
                                parameterWithName("postId").description("삭제할 게시물의 ID")
                        )
                ));
    }

    @Test
    void getRecentNoticePosts() throws Exception {
        // Given
        PostDTO postDTO = new PostDTO();
        postDTO.setPostId(1L);
        postDTO.setTitle("Test Title");
        postDTO.setContent("Test Content");
        postDTO.setCategory(Category.NOTICE);
        postDTO.setNoticeVisibilityType(NoticeVisibilityType.ENTIRE);
        postDTO.setCreatedAt(LocalDateTime.now());
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setUid("1");
        memberDTO.setPw("1");
        memberDTO.setStudentId(123L);
        memberDTO.setName("John Doe");
        memberDTO.setBirth(20000101L);
        memberDTO.setGender(Gender.MALE);
        memberDTO.setDepartment("Computer Science");
        memberDTO.setPhoneNum("123-456-7890");
        memberDTO.setEmail("john.doe@example.com");
        memberDTO.setRoleType(RoleType.MEMBER);
        postDTO.setMember(memberDTO);

        given(postService.getRecentNoticePosts()).willReturn(Arrays.asList(postDTO));

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/api/posts/recent/notices")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("get-recent-notice-posts",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].postId").description("게시물 ID"),
                                fieldWithPath("[].title").description("게시물 제목"),
                                fieldWithPath("[].content").description("게시물 내용"),
                                fieldWithPath("[].category").description("게시물 카테고리"),
                                fieldWithPath("[].noticeVisibilityType").description("공지사항 공개 유형"),
                                fieldWithPath("[].createdAt").description("게시물 생성 시간"),
                                fieldWithPath("[].member.uid").description("작성자 아이디"),
                                fieldWithPath("[].member.pw").description("작성자 비밀번호"),
                                fieldWithPath("[].member.studentId").description("작성자 학번"),
                                fieldWithPath("[].member.name").description("작성자 이름"),
                                fieldWithPath("[].member.birth").description("작성자 생년월일"),
                                fieldWithPath("[].member.gender").description("작성자 성별"),
                                fieldWithPath("[].member.department").description("작성자 학과"),
                                fieldWithPath("[].member.phoneNum").description("작성자 전화번호"),
                                fieldWithPath("[].member.email").description("작성자 이메일"),
                                fieldWithPath("[].member.roleType").description("작성자 역할 유형"),
                                fieldWithPath("[].club").description("동아리 정보 (nullable)").optional(),
                                fieldWithPath("[].clubSummaryDTO").description("동아리 요약 정보 (nullable)").optional(),
                                fieldWithPath("[].photo").description("게시물 사진 (nullable)").optional(),
                                fieldWithPath("[].uploadFileName").description("업로드된 파일 이름 (nullable)").optional(),
                                fieldWithPath("[].file").description("업로드된 파일 (nullable)").optional()
                        )));
    }

    @Test
    void getAllPosts() throws Exception {
        // Given
        PostDTO postDTO = new PostDTO();
        postDTO.setPostId(1L);
        postDTO.setTitle("Test Title");
        postDTO.setContent("Test Content");
        postDTO.setCategory(Category.NOTICE);
        postDTO.setNoticeVisibilityType(NoticeVisibilityType.ENTIRE);
        postDTO.setCreatedAt(LocalDateTime.now());

        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setUid("1");
        memberDTO.setPw("1");
        memberDTO.setStudentId(123L);
        memberDTO.setName("John Doe");
        memberDTO.setBirth(20000101L);
        memberDTO.setGender(Gender.MALE);
        memberDTO.setDepartment("Computer Science");
        memberDTO.setPhoneNum("123-456-7890");
        memberDTO.setEmail("john.doe@example.com");
        memberDTO.setRoleType(RoleType.MEMBER);
        postDTO.setMember(memberDTO);

        ClubSummaryDTO clubSummaryDTO = new ClubSummaryDTO();
        clubSummaryDTO.setClubId(1L);
        ClubJoinMemberDTO clubJoinMemberDTO = new ClubJoinMemberDTO();
        clubJoinMemberDTO.setMember(memberDTO);
        clubJoinMemberDTO.setMemberStatus(MemberStatus.ACTIVITY);
        clubSummaryDTO.setMembers(Arrays.asList(clubJoinMemberDTO));
        postDTO.setClubSummaryDTO(clubSummaryDTO);

        given(postService.getAllPosts()).willReturn(Arrays.asList(postDTO));

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("get-all-posts",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].postId").description("게시물 ID"),
                                fieldWithPath("[].title").description("게시물 제목"),
                                fieldWithPath("[].content").description("게시물 내용"),
                                fieldWithPath("[].category").description("게시물 카테고리"),
                                fieldWithPath("[].noticeVisibilityType").description("공지사항 가시성 유형"),
                                fieldWithPath("[].createdAt").description("게시물 생성 날짜"),
                                fieldWithPath("[].member.uid").description("작성자 ID"),
                                fieldWithPath("[].member.pw").description("작성자 PW"),
                                fieldWithPath("[].member.studentId").description("작성자 학생 ID"),
                                fieldWithPath("[].member.name").description("작성자 이름"),
                                fieldWithPath("[].member.birth").description("작성자 생년월일"),
                                fieldWithPath("[].member.gender").description("작성자 성별"),
                                fieldWithPath("[].member.department").description("작성자 학과"),
                                fieldWithPath("[].member.phoneNum").description("작성자 전화번호"),
                                fieldWithPath("[].member.email").description("작성자 이메일"),
                                fieldWithPath("[].member.roleType").description("작성자 역할 유형").optional(),
                                fieldWithPath("[].clubSummaryDTO").description("클럽 요약 정보").optional(),
                                fieldWithPath("[].clubSummaryDTO.clubId").description("클럽 ID"),
                                fieldWithPath("[].club").description("클럽 정보"),
                                subsectionWithPath("[].clubSummaryDTO.members").description("클럽 멤버 목록"),
                                fieldWithPath("[].clubSummaryDTO.members[].clubJoinId").description("클럽 가입 ID"),
                                fieldWithPath("[].clubSummaryDTO.members[].club").description("클럽 정보"),
                                fieldWithPath("[].clubSummaryDTO.members[].status").description("멤버 상태"),
                                fieldWithPath("[].clubSummaryDTO.members").description("클럽 멤버 목록"),
                                fieldWithPath("[].clubSummaryDTO.members[].member.studentId").description("클럽 멤버의 학생 ID"),
                                fieldWithPath("[].clubSummaryDTO.members[].member.name").description("클럽 멤버의 이름"),
                                fieldWithPath("[].clubSummaryDTO.members[].member.birth").description("클럽 멤버의 생년월일"),
                                fieldWithPath("[].clubSummaryDTO.members[].member.gender").description("클럽 멤버의 성별"),
                                fieldWithPath("[].clubSummaryDTO.members[].member.department").description("클럽 멤버의 학과"),
                                fieldWithPath("[].clubSummaryDTO.members[].member.phoneNum").description("클럽 멤버의 전화번호"),
                                fieldWithPath("[].clubSummaryDTO.members[].member.email").description("클럽 멤버의 이메일"),
                                fieldWithPath("[].clubSummaryDTO.members[].member.roleType").description("클럽 멤버의 역할 유형").optional(),
                                fieldWithPath("[].clubSummaryDTO.members[].memberStatus").description("클럽 멤버의 상태"),
                                fieldWithPath("[].photo").description("게시물 사진"),
                                fieldWithPath("[].uploadFileName").description("업로드된 파일 이름").optional(),
                                fieldWithPath("[].file").description("파일").optional()
                        )
                ));
    }

    //활동 영상 등록
    @Test
    public void makeVideo() throws Exception {
        VideoDTO.Request videoDTO = new VideoDTO.Request(
                1L,
                "비디오 제목",
                "http://example.com/video1",
                new MemberDTO(
                        "id", // uid
                        "pw", // pw
                        20211234L, // studentId
                        "김회원", // name
                        2000010L, // birth
                        Gender.MALE, // gender
                        "컴퓨터 소프트웨어공학과", // department
                        "010-1234-5678", // phoneNum
                        "kim@gmail.com", // email
                        RoleType.MEMBER // roleType
                ),
                LocalDateTime.now(),
                Category.VIDEO,
                LocalDateTime.now()
        );

        Post post = new Post();
        post.setPostId(1L);
        post.setTitle(videoDTO.getTitle());
        post.setContent(videoDTO.getContent());

        Mockito.when(postService.makeVideo(any(VideoDTO.Request.class))).thenReturn(post);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/posts/video")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(videoDTO)))
                .andExpect(status().isCreated())
                .andDo(document("make-video",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("postId").description("비디오 게시물의 ID").type(JsonFieldType.NUMBER),
                                fieldWithPath("title").description("비디오 제목").type(JsonFieldType.STRING),
                                fieldWithPath("content").description("비디오 URL").type(JsonFieldType.STRING),
                                fieldWithPath("member").description("비디오를 게시한 회원").type(JsonFieldType.OBJECT),
                                fieldWithPath("member.uid").description("회원의 UID").type(JsonFieldType.STRING),
                                fieldWithPath("member.pw").description("회원의 비밀번호").type(JsonFieldType.STRING),
                                fieldWithPath("member.studentId").description("회원의 학번").type(JsonFieldType.NUMBER),
                                fieldWithPath("member.name").description("회원의 이름").type(JsonFieldType.STRING),
                                fieldWithPath("member.birth").description("회원의 생년월일").type(JsonFieldType.NUMBER),
                                fieldWithPath("member.gender").description("회원의 성별").type(JsonFieldType.STRING),
                                fieldWithPath("member.department").description("회원의 학과").type(JsonFieldType.STRING),
                                fieldWithPath("member.phoneNum").description("회원의 전화번호").type(JsonFieldType.STRING),
                                fieldWithPath("member.email").description("회원의 이메일").type(JsonFieldType.STRING),
                                fieldWithPath("member.roleType").description("회원의 역할 유형").type(JsonFieldType.STRING),
                                fieldWithPath("createdAt").description("게시물 생성 날짜").type(JsonFieldType.STRING),
                                fieldWithPath("updateAt").description("게시물 마지막 수정 날짜").type(JsonFieldType.STRING),
                                fieldWithPath("category").description("게시물 카테고리").type(JsonFieldType.STRING).optional()
                        ),
                        responseFields(
                                fieldWithPath("postId").description("비디오 게시물의 ID").type(JsonFieldType.NUMBER),
                                fieldWithPath("title").description("비디오 제목").type(JsonFieldType.STRING),
                                fieldWithPath("content").description("비디오 URL").type(JsonFieldType.STRING),
                                fieldWithPath("member").description("비디오를 게시한 회원").type(JsonFieldType.OBJECT).optional(),
                                fieldWithPath("club").description("클럽 정보 (nullable)").type(JsonFieldType.OBJECT).optional(),
                                fieldWithPath("uploadFileName").description("업로드된 파일 이름 (nullable)").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("category").description("게시물 카테고리 (nullable)").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("photo").description("게시물 사진 (nullable)").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("file").description("업로드된 파일 (nullable)").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("noticeVisibilityType").description("공지사항 공개 유형 (nullable)").type(JsonFieldType.STRING).optional(),
                                fieldWithPath("createAt").description("게시물 생성 날짜").type(JsonFieldType.STRING),
                                fieldWithPath("updateAt").description("게시물 마지막 수정 날짜").type(JsonFieldType.STRING)
                        )));
    }


    //활동 영상 4개 최신순으로 조회
    @Test
    public void get4VideoList() throws Exception {
        VideoDTO.Request video = new VideoDTO.Request(
                1L,
                "샘플 비디오",
                "https://youtu.be/IMRZqfHDmII?si=jzNE4IL3oblyPErm",
                new MemberDTO(
                        "id", // uid
                        "pw", // pw
                        20211234L, // studentId
                        "김회원", // name
                        2000010L, // birth
                        Gender.MALE, // gender
                        "컴퓨터 소프트웨어공학과", // department
                        "010-1234-5678", // phoneNum
                        "kim@gmail.com", // email
                        RoleType.MEMBER // roleType
                ),
                LocalDateTime.now(),
                Category.VIDEO, // category
                LocalDateTime.now()
        );


        Mockito.when(postService.get4VideoList()).thenReturn(Arrays.asList(video, video, video, video));

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/posts/main-video")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("get-4-video-list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].postId").description("비디오 게시물의 ID"),
                                fieldWithPath("[].title").description("비디오 제목"),
                                fieldWithPath("[].content").description("비디오 URL"),
                                fieldWithPath("[].member").description("비디오를 게시한 회원"),
                                fieldWithPath("[].member.uid").description("회원의 UID").optional(),
                                fieldWithPath("[].member.pw").description("회원의 비밀번호").optional(),
                                fieldWithPath("[].member.studentId").description("회원의 학번"),
                                fieldWithPath("[].member.name").description("회원의 이름"),
                                fieldWithPath("[].member.birth").description("회원의 생년월일"),
                                fieldWithPath("[].member.gender").description("회원의 성별"),
                                fieldWithPath("[].member.department").description("회원의 학과"),
                                fieldWithPath("[].member.phoneNum").description("회원의 전화번호"),
                                fieldWithPath("[].member.email").description("회원의 이메일"),
                                fieldWithPath("[].member.roleType").description("회원의 역할 유형"),
                                fieldWithPath("[].createdAt").description("게시물 생성 날짜"),
                                fieldWithPath("[].updateAt").description("게시물 마지막 수정 날짜"),
                                fieldWithPath("[].category").description("게시물 카테고리")
                        )));
    }

    // 활동 영상 리스트 조회
    @Test
    public void getVideoList() throws Exception {
        VideoDTO.Request video = VideoDTO.Request.builder()
                .postId(1L)
                .title("샘플 비디오")
                .content("http://example.com/video1")
                .member(new MemberDTO(
                        "id", // uid
                        "pw", // pw
                        20211234L, // studentId
                        "김회원", // name
                        2000010L, // birth
                        Gender.MALE, // gender
                        "컴퓨터 소프트웨어공학과", // department
                        "010-1234-5678", // phoneNum
                        "kim@gmail.com", // email
                        RoleType.MEMBER // roleType
                ))                     .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .category(null)
                .build();

        Mockito.when(postService.getVideoList()).thenReturn(Collections.singletonList(video));

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/posts/video")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("get-video-list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].postId").description("비디오 게시물의 ID"),
                                fieldWithPath("[].title").description("비디오 제목"),
                                fieldWithPath("[].content").description("비디오 URL"),
                                fieldWithPath("[].member").description("비디오를 게시한 회원"),
                                fieldWithPath("[].member.uid").description("회원의 UID").optional(),
                                fieldWithPath("[].member.pw").description("회원의 비밀번호").optional(),
                                fieldWithPath("[].member.studentId").description("회원의 학번"),
                                fieldWithPath("[].member.name").description("회원의 이름"),
                                fieldWithPath("[].member.birth").description("회원의 생년월일"),
                                fieldWithPath("[].member.gender").description("회원의 성별"),
                                fieldWithPath("[].member.department").description("회원의 학과"),
                                fieldWithPath("[].member.phoneNum").description("회원의 전화번호"),
                                fieldWithPath("[].member.email").description("회원의 이메일"),
                                fieldWithPath("[].member.roleType").description("회원의 역할 유형"),
                                fieldWithPath("[].createdAt").description("게시물 생성 날짜"),
                                fieldWithPath("[].updateAt").description("게시물 마지막 수정 날짜"),
                                fieldWithPath("[].category").description("게시물 카테고리")
                        )));
    }

    // 활동 영상 조회
    @Test
    public void getVideo() throws Exception {
        VideoDTO.Request video = VideoDTO.Request.builder()
                .postId(1L)
                .title("샘플 비디오")
                .content("http://example.com/video1")
                .member(new MemberDTO(
                        "id", // uid
                        "pw", // pw
                        20211234L, // studentId
                        "김회원", // name
                        2000010L, // birth
                        Gender.MALE, // gender
                        "컴퓨터 소프트웨어공학과", // department
                        "010-1234-5678", // phoneNum
                        "kim@gmail.com", // email
                        RoleType.MEMBER // roleType
                ))                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .category(null)
                .build();

        Mockito.when(postService.getVideo(1L)).thenReturn(video);
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/posts/video/{videoId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("get-video",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("postId").description("비디오 게시물의 ID"),
                                fieldWithPath("title").description("비디오 제목"),
                                fieldWithPath("content").description("비디오 URL"),
                                fieldWithPath("member").description("비디오를 게시한 회원"),
                                fieldWithPath("member.uid").description("회원의 UID").optional(),
                                fieldWithPath("member.pw").description("회원의 비밀번호").optional(),
                                fieldWithPath("member.studentId").description("회원의 학번"),
                                fieldWithPath("member.name").description("회원의 이름"),
                                fieldWithPath("member.birth").description("회원의 생년월일"),
                                fieldWithPath("member.gender").description("회원의 성별"),
                                fieldWithPath("member.department").description("회원의 학과"),
                                fieldWithPath("member.phoneNum").description("회원의 전화번호"),
                                fieldWithPath("member.email").description("회원의 이메일"),
                                fieldWithPath("member.roleType").description("회원의 역할 유형"),
                                fieldWithPath("createdAt").description("게시물 생성 날짜"),
                                fieldWithPath("updateAt").description("게시물 마지막 수정 날짜"),
                                fieldWithPath("category").description("게시물 카테고리")
                        )));
    }

    // 활동 영상 수정
    @Test
    public void updateVideo() throws Exception {
        VideoDTO.UpdateRequest updateRequest = VideoDTO.UpdateRequest.builder()
                .title("업데이트된 비디오")
                .content("http://example.com/video1-updated")
                .build();

        Mockito.when(postService.updateVideo(any(Long.class), any(VideoDTO.UpdateRequest.class)))
                .thenReturn(null); // 테스트를 위해 필요한 값 반환

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/posts/video/{videoId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andDo(document("update-video",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("title").description("비디오 제목"),
                                fieldWithPath("content").description("비디오 URL")
                        )));
    }

}
