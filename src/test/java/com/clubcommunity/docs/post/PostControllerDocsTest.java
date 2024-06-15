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
}
