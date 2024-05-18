package com.clubcommunity.repository;

import com.clubcommunity.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    // Create (Save)
    Post save(Post post);

    // Read (Find)
    Optional<Post> findById(Long id);
    List<Post> findAll();

    // Delete
    void deleteById(Long id);
    void delete(Post post);
}
