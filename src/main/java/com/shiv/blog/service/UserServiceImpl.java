package com.shiv.blog.service;

import com.shiv.blog.entity.Post;
import com.shiv.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.shiv.blog.entity.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private PostService postService;

    @Override
    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public User getUser(Long userId) {
        User user = userRepository.getById(userId);
        return user;
    }

    @Override
    public User getUserByEmail(String username) {
        return userRepository.getUserByUsername(username);
    }

    @Override
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    @Override
    public void updateUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        this.userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        List<Post> posts = this.postService.getPostsOfAuthor(id);
        for(Post post : posts) {
            this.postService.deletePost(post.getId());
        }
        this.userRepository.deleteById(id);
    }
}
