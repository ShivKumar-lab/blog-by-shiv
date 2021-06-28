package com.shiv.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.shiv.blog.entity.Comment;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM comments WHERE post_id=:id", nativeQuery = true)
    void deleteByPostId(@Param("id") Long postId);
}
