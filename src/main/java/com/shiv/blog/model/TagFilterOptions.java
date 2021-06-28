package com.shiv.blog.model;

import java.util.*;
import com.shiv.blog.entity.Tag;

public class TagFilterOptions {
    List<Tag> options = new ArrayList<>();

    public TagFilterOptions(List<Tag> options) {
        this.options = options;
    }

    public TagFilterOptions() {

    }

    public List<Tag> getOptions() {
        return options;
    }

    public void setOptions(List<Tag> options) {
        this.options = options;
    }
}
