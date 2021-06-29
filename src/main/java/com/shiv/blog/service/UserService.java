package com.shiv.blog.service;
import com.shiv.blog.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    void addUser(User user);
    User getUser(Long userId);

    User getUserByEmail(String username);

    List<User> getAllUsers();
}
