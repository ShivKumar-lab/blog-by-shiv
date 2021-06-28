package com.shiv.blog.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostTagService {
    void addPostTags(Long id, List<Long> listOfTagId);

    List<String> getTagNamesByPost(Long postId);

    void deletePostTagsByPostId(Long postId);

    List<Long> getPostIdByTagId(Long tagId);
}
