package com.shiv.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.shiv.blog.entity.Tag;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByName(String name);

    List<Tag> findByNameContaining(String searchString);

    void deleteById(Long tagId);
}

