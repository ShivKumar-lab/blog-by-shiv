package com.shiv.blog.controller;

import com.shiv.blog.entity.Comment;
import com.shiv.blog.entity.Post;
import com.shiv.blog.entity.Tag;
import com.shiv.blog.entity.User;
import com.shiv.blog.service.CommentService;
import com.shiv.blog.service.PostService;
import com.shiv.blog.service.TagService;
import com.shiv.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RestApiController {

    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private TagService tagService;
    @Autowired
    private UserService userService;

    @GetMapping("/api/posts")
    public List<Post> getAllPosts() {
        return this.postService.getAllPosts();
    }

    @GetMapping("/api/posts/{pageNo}")
    public List<Post> getPageOfPosts(@PathVariable(value = "pageNo") int pageNo) {
        Page<Post> page = postService.getPaginatedPosts(pageNo, 10, "createdAt", "desc");
        return page.getContent();
    }

    @GetMapping("/api/post/{id}")
    public Post getPost(@PathVariable(value = "id") Long id) {
        return this.postService.getPost(id);
    }

    @GetMapping("/api/comments/{postId}")
    public List<Comment> getAllCommentsOfPost(@PathVariable(value = "postId") Long postId) {
        return this.commentService.getAllComments(postId);
    }

    @GetMapping("api/comment/{id}")
    public Comment getComment(@PathVariable(value = "id") Long id) {
        return this.commentService.getCommentById(id);
    }

    @GetMapping("api/tags")
    public List<Tag> getAllTags() {
        return this.tagService.getAllTags();
    }

    @PostMapping("api/post")
    public Long addPost(@RequestBody Post post) {
        return this.postService.addPost(post);
    }

    @PostMapping("api/comment/{postId}")
    public String addComment(@PathVariable Long postId, @RequestBody Comment comment) {
        this.commentService.addComment(comment);
        return "Comment added";
    }

    @GetMapping("/api/users")
    public List<User> getUsers() {
        return this.userService.getAllUsers();
    }

    @GetMapping("api/user/{id}")
    public User getUser(@PathVariable Long id) {
        return this.userService.getUser(id);
    }
}
