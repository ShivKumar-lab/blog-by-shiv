package com.shiv.blog.repository;

import com.shiv.blog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    void deleteById(Long postId);

    List<Post> findByTitleContaining(String searchString);

    List<Post> findByContentContaining(String searchString);

    List<Post> findByExcerptContaining(String searchString);

    @Query(value = "SELECT * FROM posts p Where p.author=:filterQuery", nativeQuery = true)
    List<Post> getPostByAuthor(@Param("filterQuery") String filterQuery);

    @Query(value = "SELECT * FROM posts p Where p.user_id=:id", nativeQuery = true)
    List<Post> getByUserId(@Param("id") Long id);
}
