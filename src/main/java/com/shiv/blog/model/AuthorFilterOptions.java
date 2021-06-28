package com.shiv.blog.model;

import java.util.*;
import com.shiv.blog.entity.User;

public class AuthorFilterOptions {
    List<User> options = new ArrayList<>();

    public AuthorFilterOptions(List<User> options) {
        this.options = options;
    }

    public AuthorFilterOptions() {

    }

    public List<User> getOptions() {
        return options;
    }

    public void setOptions(List<User> options) {
        this.options = options;
    }
}
