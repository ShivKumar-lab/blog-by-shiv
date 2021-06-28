package com.shiv.blog.service;

import com.shiv.blog.entity.Post;
import com.shiv.blog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentService commentService;
    @Autowired
    private PostTagService postTagService;
    @Autowired
    private TagService tagService;

    @Override
    public Post getPost(Long postId) {
        Post post = this.postRepository.getById(postId);
        return post;
    }

    @Override
    public Long addPost(Post post) {
        Post tempPost = this.postRepository.save(post);
        return tempPost.getId();
    }

    @Override
    public void deletePost(Long postId) {
        postTagService.deletePostTagsByPostId(postId);
        commentService.deleteCommentsByPostId(postId);
        this.postRepository.deleteById(postId);
    }

    @Override
    public Page<Post> getPaginatedPosts(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.postRepository.findAll(pageable);
    }

    @Override
    public List<Post> getPosts(String searchString) {
        List<Post> postsFromTitle = this.postRepository.findByTitleContaining(searchString);
        List<Post> postsFromExcerpt = this.postRepository.findByExcerptContaining(searchString);
        List<Post> postsFromContent = this.postRepository.findByContentContaining(searchString);
        postsFromContent.addAll(postsFromExcerpt);
        postsFromContent.addAll(postsFromTitle);
        return postsFromContent;
    }

    @Override
    public List<Post> getAllPosts() {
        return this.postRepository.findAll();
    }
}
