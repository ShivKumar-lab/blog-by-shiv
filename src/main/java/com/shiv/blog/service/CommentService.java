package com.shiv.blog.service;

import com.shiv.blog.entity.Comment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentService {
    void addComment(Comment comment);

    List<Comment> getAllComments(Long postId);

    void deleteCommentsByPostId(Long postId);

    void deleteCommentById(Long id);

    Long getPostIdByCommentId(Long id);

    Comment getCommentById(Long id);
}
