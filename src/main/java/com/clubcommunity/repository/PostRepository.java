package com.clubcommunity.repository;

import com.clubcommunity.domain.Category;
import com.clubcommunity.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    // Create (Save)
    Post save(Post post);

    // Read (Find)
    Optional<Post> findById(Long id);
    List<Post> findAll();

    Page<Post> findByCategoryOrderByCreateAtDesc(Category category, Pageable pageable);

    List<Post> findByCategory(Category category);

    // Delete
    void deleteById(Long id);
    void delete(Post post);

    // 최신순으로 Category가 해당 카테고리인 포스트 3개를 가져오는 쿼리
    List<Post> findTop4ByCategoryOrderByCreateAtDesc(Category category);

    List<Post> findByCategoryOrderByCreateAtDesc(Category category);
}
