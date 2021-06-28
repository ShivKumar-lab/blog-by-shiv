package com.shiv.blog.repository;

import com.shiv.blog.entity.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface PostTagRepository extends JpaRepository<PostTag, Long> {
    List<PostTag> findByPostId(Long postId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM post_tags WHERE post_id=:id", nativeQuery = true)
    void deleteByPostId(@Param("id") Long postId);

    @Query(value = "SELECT post_id FROM post_tags WHERE tag_id=:tagId", nativeQuery = true)
    List<Long> findByTagId(@Param("tagId") Long tagId);

    @Query(value = "SELECT tag_id FROM post_tags Where post_id=:postId", nativeQuery = true)
    List<Long> findTagIdsByPostId(@Param("postId") Long postId);
}
