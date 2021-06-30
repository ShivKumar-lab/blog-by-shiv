package com.shiv.blog.service;

import com.shiv.blog.entity.Comment;
import com.shiv.blog.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public void addComment(Comment comment) {
        this.commentRepository.save(comment);
    }

    @Override
    public List<Comment> getAllComments(Long postId) {
        return this.commentRepository.findByPostId(postId);
    }

    @Override
    public void deleteCommentsByPostId(Long postId) {
        this.commentRepository.deleteByPostId(postId);
    }

    @Override
    public void deleteCommentById(Long id) {
        this.commentRepository.deleteById(id);
    }

    @Override
    public Long getPostIdByCommentId(Long id) {
        Comment comment = this.commentRepository.getById(id);
        Long postId = comment.getPostId();
        return postId;
    }

    @Override
    public Comment getCommentById(Long id) {
        return this.commentRepository.getById(id);
    }

    @Override
    public void updateCommentById(Long id, Comment comment) {
        comment.setId(id);
        this.commentRepository.save(comment);
    }
}
