package com.shiv.blog.controller;

import com.shiv.blog.entity.Comment;
import com.shiv.blog.entity.Post;
import com.shiv.blog.entity.Tag;
import com.shiv.blog.entity.User;
import com.shiv.blog.model.FilterOptions;
import com.shiv.blog.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    @Autowired
    private PostTagService postTagService;

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

    @PostMapping("api/post/filter")
    public List<Post> filterPosts(@RequestBody FilterOptions filterOptions) {
        List<Post> posts = new ArrayList<>();
        List<Long> postIds = new ArrayList<>();
        for(Tag tag : filterOptions.getTagOptions()) {
            postIds.addAll(this.postTagService.getPostIdByTagId(tag.getId()));
        }
        for(Long postId :postIds) {
            posts.add(this.postService.getPost(postId));
        }
        for(User user : filterOptions.getUserOptions()) {
            posts.addAll(this.postService.getPostsOfAuthor(user.getId()));
        }
        return posts;
    }

    @GetMapping("api/search/{searchString}")
    public List<Post> getSearchedPosts(@PathVariable String searchString) {
        return this.postService.getPosts(searchString);
    }

    @PostMapping("api/post")
    public Long addPost(@RequestBody Post post) {
        return this.postService.addPost(post);
    }

    @PutMapping("api/post")
    public String updatePost(@RequestBody Post post) {
        this.postService.addPost(post);
        return "Post updated";
    }

    @DeleteMapping("api/post/{id}")
    public String deletePost(@PathVariable Long id) {
        this.postService.deletePost(id);
        return "Post deleted";
    }



    @GetMapping("/api/comments/{postId}")
    public List<Comment> getAllCommentsOfPost(@PathVariable(value = "postId") Long postId) {
        return this.commentService.getAllComments(postId);
    }

    @GetMapping("api/comment/{id}")
    public Comment getComment(@PathVariable(value = "id") Long id) {
        return this.commentService.getCommentById(id);
    }

    @PostMapping("api/comment/{postId}")
    public String addComment(@PathVariable Long postId, @RequestBody Comment comment) {
        this.commentService.addComment(comment);
        return "Comment added";
    }

    @PutMapping("api/comment/{id}")
    public String updateComment(@PathVariable Long id, @RequestBody Comment comment) {
        this.commentService.updateCommentById(id, comment);
        return "Comment updated";
    }

    @DeleteMapping("api/comment/{id}")
    public String deleteComment(@PathVariable Long id) {
        this.commentService.deleteCommentById(id);
        return "Comment deleted";
    }



    @GetMapping("api/tags")
    public List<Tag> getAllTags() {
        return this.tagService.getAllTags();
    }

    @GetMapping("api/tag/{id}")
    public Tag getTag(@PathVariable(value = "id") Long id) {
        return this.tagService.getTag(id);
    }



    @GetMapping("/api/users")
    public List<User> getUsers() {
        return this.userService.getAllUsers();
    }

    @GetMapping("api/user/{id}")
    public User getUser(@PathVariable Long id) {
        return this.userService.getUser(id);
    }

    @PostMapping("api/user")
    public void addUser(@RequestBody User user) {
        this.userService.addUser(user);
    }

    @PutMapping("api/user")
    public void updateUser(@RequestBody User user) {
        this.userService.updateUser(user);
    }

    @DeleteMapping("api/user/{id}")
    public void deleteUser(@PathVariable Long id) {
        this.userService.deleteUser(id);
    }
}
