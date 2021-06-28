package com.shiv.blog.service;

import com.shiv.blog.repository.PostTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.shiv.blog.entity.PostTag;

@Service
public class PostTagServiceImpl implements PostTagService {

    @Autowired
    private PostTagRepository postTagRepository;
    @Autowired
    private TagService tagService;

    @Override
    public void addPostTags(Long postId, List<Long> listOfTagId) {
        for (Long tagId : listOfTagId) {
            PostTag postTag = new PostTag();
            postTag.setPostId(postId);
            postTag.setTagId(tagId);
            postTag.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            postTagRepository.save(postTag);
        }
    }

    @Override
    public List<String> getTagNamesByPost(Long postId) {
        List<PostTag> postTags = this.postTagRepository.findByPostId(postId);
        List<Long> tagIds = new ArrayList<>();
        for (PostTag postTag : postTags) {
            tagIds.add(postTag.getTagId());
        }
        return tagService.getTagNamesById(tagIds);
    }

    @Override
    public void deletePostTagsByPostId(Long postId) {
        this.postTagRepository.deleteByPostId(postId);
    }

    @Override
    public List<Long> getPostIdByTagId(Long tagId) {
        List<Long> postIds = this.postTagRepository.findByTagId(tagId);
        if (!postIds.isEmpty()) {
            for (Long postId : postIds) {
                System.out.println("postId : " + postId);
            }
        }
        return postIds;
    }
}
