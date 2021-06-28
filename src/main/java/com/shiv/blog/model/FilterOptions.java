package com.shiv.blog.model;

import java.util.*;
import com.shiv.blog.entity.Tag;
import com.shiv.blog.entity.User;

public class FilterOptions {
    List<Tag> tagOptions = new ArrayList<>();
    List<User> userOptions = new ArrayList<>();

    public FilterOptions(List<Tag> tagOptions, List<User> userOptions) {
        this.tagOptions = tagOptions;
        this.userOptions = userOptions;
    }

    public FilterOptions() {

    }

    public List<Tag> getTagOptions() {
        return tagOptions;
    }

    public List<User> getUserOptions() { return userOptions; }

    public void setTagOptions(List<Tag> tagOptions) {
        this.tagOptions = tagOptions;
    }

    public void setUserOptions(List<User> userOptions) { this.userOptions = userOptions; }
}
