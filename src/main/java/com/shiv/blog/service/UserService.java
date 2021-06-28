package com.shiv.blog.service;
import com.shiv.blog.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    void addUser(User user);
    User getUser(Long userId);

    User getUserByEmail(String username);
}
