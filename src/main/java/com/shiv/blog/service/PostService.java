package com.shiv.blog.service;

import com.shiv.blog.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface PostService {
    Post getPost(Long postId);

    Long addPost(Post post);

    void deletePost(Long postId);

    Page<Post> getPaginatedPosts(int pageNo, int pageSize, String sortField, String sortDirection);

    List<Post> getPosts(String searchString);

    List<Post> getAllPosts();

    List<Post> getPostsOfAuthor(Long id);
}
