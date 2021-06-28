package com.shiv.blog.controller;

import com.shiv.blog.service.CommentService;
import com.shiv.blog.service.PostService;
import com.shiv.blog.service.PostTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.shiv.blog.entity.Comment;
import com.shiv.blog.entity.Post;
import com.shiv.blog.entity.User;
import com.shiv.blog.service.UserService;
import com.shiv.blog.controller.PostController;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;
    @Autowired
    private PostTagService postTagService;

    @PostMapping("/addComment")
    public String addComment(@ModelAttribute Comment tempComment, Long postId) {
        Post post = postService.getPost(postId);

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        User user = userService.getUserByEmail(username);
        if(user == null) {
            tempComment.setName("anonymousUser");
            tempComment.setEmail("");
            tempComment.setUserId(0L);
        } else {
            tempComment.setName(user.getName());
            tempComment.setEmail(user.getEmail());
            tempComment.setUserId(user.getId());
        }
        tempComment.setPostId(post.getId());
        tempComment.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        commentService.addComment(tempComment);
        return "redirect:/blogPost/" + postId;
    }

    @GetMapping("/deleteComment/{id}")
    public String deleteComment(@PathVariable(value = "id") Long id) {
        Long postId = this.commentService.getPostIdByCommentId(id);
        this.commentService.deleteCommentById(id);
        return "redirect:/blogPost/" + postId;
    }

    @GetMapping("/updateComment/{id}")
    public String updateComment(@PathVariable(value = "id") Long id, Model model) {
        Comment comment = this.commentService.getCommentById(id);
        Long postId = comment.getPostId();
        Post post = this.postService.getPost(postId);
        model.addAttribute("tempComment", comment);
        model.addAttribute("tempPost", post);
        this.commentService.deleteCommentById(id);
        List<String> tagNames = postTagService.getTagNamesByPost(postId);
        List<Comment> comments = commentService.getAllComments(post.getId());
        model.addAttribute("tagNames", tagNames);
        model.addAttribute("comments", comments);
        return "show_post.html";
    }
}
