package com.shiv.blog.service;

import com.shiv.blog.entity.Post;
import com.shiv.blog.entity.Tag;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface TagService {
    List<Long> submitTags(String bundledTags);

    List<String> getTagNamesById(List<Long> tagIds);

    List<Post> getSearchedPosts(String searchString);

    List<Tag> getAllTags();

    Tag getTag(Long id);
}
