package com.shiv.blog.service;

import com.shiv.blog.entity.Post;
import com.shiv.blog.entity.Tag;
import com.shiv.blog.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private PostTagService postTagService;
    @Autowired
    private PostService postService;

    @Override
    public List<Long> submitTags(String bundledTags) {
        List<Tag> tags = new ArrayList<>();
        String[] arrOfTags = bundledTags.split(",");
        for (int i = 0; i < arrOfTags.length; i++) {
            arrOfTags[i] = arrOfTags[i].trim();
            if (!arrOfTags[i].isEmpty()) {
                arrOfTags[i]=arrOfTags[i].toLowerCase();
                Tag tag = new Tag();
                tag.setName(arrOfTags[i]);
                tag.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                tags.add(tag);
            }
        }

        for (Tag tag : tags) {
            Tag tempTag = getTagByName(tag.getName());
            if (tempTag == null) {
                tagRepository.save(tag);
            }
        }

        List<Long> listOfTagId = new ArrayList<>();
        for (Tag tag : tags) {
            Tag tempTag = getTagByName(tag.getName());
            listOfTagId.add(tempTag.getId());
        }
        return listOfTagId;
    }

    @Override
    public List<String> getTagNamesById(List<Long> tagIds) {
        List<String> tagNames = new ArrayList<>();
        for (Long tagId : tagIds) {
            Tag tag = this.tagRepository.getById(tagId);
            tagNames.add(tag.getName());
        }
        return tagNames;
    }

    @Override
    public List<Post> getSearchedPosts(String searchString) {
        List<Tag> tags = this.tagRepository.findByNameContaining(searchString);
        List<Post> posts = new ArrayList<>();
        for (Tag tag : tags) {
            List<Long> postIds = postTagService.getPostIdByTagId(tag.getId());
            for (Long postId : postIds) {
                posts.add(postService.getPost(postId));
            }
        }
        return posts;
    }

    @Override
    public List<Tag> getAllTags() {
        return this.tagRepository.findAll();
    }

    @Override
    public Tag getTag(Long id) {
        return this.tagRepository.getById(id);
    }

    private Tag getTagByName(String name) {
        return this.tagRepository.findByName(name);
    }
}
