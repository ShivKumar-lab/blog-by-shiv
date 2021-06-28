package com.shiv.blog.controller;

import com.shiv.blog.entity.Comment;
import com.shiv.blog.entity.Post;
import com.shiv.blog.model.AuthorFilterOptions;
import com.shiv.blog.model.FilterOptions;
import com.shiv.blog.model.TagFilterOptions;
import com.shiv.blog.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.shiv.blog.entity.User;
import com.shiv.blog.entity.Tag;

@Controller
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;
    @Autowired
    private TagService tagService;
    @Autowired
    private PostTagService postTagService;

    @GetMapping("/addPost")
    public String showAddPostForm(Model model) {
        model.addAttribute("post", new Post());
        return "add_post.html";
    }

    @PostMapping("/addPost")
    public String addPost(@ModelAttribute Post post, String bundledTags) {
        List<Long> listOfTagId = tagService.submitTags(bundledTags);

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        User user = userService.getUserByEmail(username);
        post.setUserId(user.getId());
        post.setAuthor(user.getName());
        post.setPublished(true);
        if (post.getContent().length() <= 50) {
            post.setExcerpt(post.getContent().substring(0, post.getContent().length() - 1));
        } else {
            post.setExcerpt(post.getContent().substring(0, 50));
        }
        post.setExcerpt(post.getExcerpt() + "..........");
        post.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        post.setPublishedAt(new Timestamp(System.currentTimeMillis()));
        Long postId = postService.addPost(post);
        postTagService.addPostTags(postId, listOfTagId);
        return "redirect:/blogPost/" + postId;
    }

    @GetMapping("/")
    public String viewPosts(Model model) {
        return showPaginatedPosts(1, "id", "desc", model);
    }


    @GetMapping("/{pageNo}")
    public String showPaginatedPosts(@PathVariable(value = "pageNo") int pageNo,
                                     @RequestParam("sortField") String sortField,
                                     @RequestParam("sortDir") String sortDir,
                                     Model model) {
        int pageSize = 10;
        Page<Post> page = postService.getPaginatedPosts(pageNo, pageSize, sortField, sortDir);
        List<Post> postList = page.getContent();

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        User user = userService.getUserByEmail(username);
        System.out.println(user);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalPosts", page.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("postList", postList);
        model.addAttribute("currentUser",user);

        return "show_posts.html";
    }

    @GetMapping("/blogPost/{postId}")
    public String showBlogPost(@PathVariable(value = "postId") Long postId, Model model) {
        Post post = postService.getPost(postId);
        User user = userService.getUser(post.getUserId());
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username, allowForPost = "no";
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        User currentUser = userService.getUserByEmail(username);
        if(!username.equals("anonymousUser")) {
            if (user.getEmail().equals(username) || currentUser.getUserRole().equals("ADMIN")) {
                allowForPost = "yes";
            } else {
                allowForPost = "no";
            }
        }

        System.out.println(currentUser);
        List<String> tagNames = postTagService.getTagNamesByPost(postId);
        List<Comment> comments = commentService.getAllComments(post.getId());
        model.addAttribute("allowAlteringOfPost", allowForPost);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("comments", comments);
        model.addAttribute("tempPost", post);
        model.addAttribute("tagNames", tagNames);
        model.addAttribute("tempComment", new Comment());
        return "show_post.html";
    }

    @GetMapping("/deletePost/{postId}")
    public String deletePost(@PathVariable(value = "postId") Long postId) {
        postService.deletePost(postId);
        return "redirect:/";
    }

    @GetMapping("/updatePost/{postId}")
    public String showUpdatePostForm(@PathVariable(value = "postId") Long postId, Model model) {
        Post post = postService.getPost(postId);
        List<String> tagNames = postTagService.getTagNamesByPost(postId);
        String bundledTags = "";
        for (String tagName : tagNames) {
            bundledTags += tagName + ", ";
        }
        model.addAttribute("post", post);
        model.addAttribute("bundledTags", bundledTags);
        return "update_post.html";
    }

    @PostMapping("/updatePost/{postId}")
    public String updatePost(@PathVariable(value = "postId") Long postId, @ModelAttribute Post post, String bundledTags) {
        List<Long> listOfTagId = tagService.submitTags(bundledTags);
        postTagService.deletePostTagsByPostId(post.getId());

        if (post.getContent().length() <= 50) {
            post.setExcerpt(post.getContent().substring(0, post.getContent().length() - 1));
        } else {
            post.setExcerpt(post.getContent().substring(0, 50));
        }
        post.setExcerpt(post.getExcerpt() + "..........");

        post.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        post.setPublished(true);
        Long tempPostId = postService.addPost(post);
        postTagService.addPostTags(tempPostId, listOfTagId);
        return "redirect:/blogPost/" + tempPostId;
    }

    @GetMapping("/searchPost")
    public String searchStringQuery(String searchString, Model model) {
        List<Post> posts = postService.getPosts(searchString);
        if (!posts.isEmpty()) {
            for (Post post : posts) {
                System.out.println(post.getCreatedAt());
            }
        }
        List<Post> postsFromSearchingTags = tagService.getSearchedPosts(searchString);
        posts.addAll(postsFromSearchingTags);
        if (posts.size() > 1) {
            Long id = posts.get(0).getId();
            for (int i = 1; i < posts.size(); i++) {
                if (id == posts.get(i).getId()) {
                    posts.remove(i);
                }
            }
        }
        model.addAttribute("posts", posts);
        return "show_searched_posts.html";
    }

    @GetMapping("/filterPost")
    public String applyFilters(Model model) {
        List<Tag> tags = tagService.getAllTags();
        List<User> users = userService.getAllUsers();
        List<Tag> selectableTags = new ArrayList<>();
        for (Tag tag : tags) {
            if (!tag.getName().trim().isEmpty()) {
                selectableTags.add(tag);
            }
        }
        model.addAttribute("users",users);
        model.addAttribute("filterOptions",new FilterOptions());
        model.addAttribute("selectableTags", selectableTags);
        return "apply_filters.html";
    }

    @PostMapping("/filterPost")
    public String processFilters(@ModelAttribute FilterOptions filterOptions,
                                 Model model, String dateFrom, String dateTo) {
        List<Post> posts = new ArrayList<>();
        List<Post> finalPosts = new ArrayList<>();
        List<Post> tempPostList = new ArrayList<>();
        for (Tag tag : filterOptions.getTagOptions()) {
            tempPostList.addAll(tagService.getSearchedPosts(tag.getName()));
        }
        //System.out.println(filterOptions.getUserOptions());
        for(User user : filterOptions.getUserOptions()) {
            tempPostList.addAll(postService.getPostsOfAuthor(user.getId()));
        }
        dateFrom += ":00.000Z";
        dateTo += ":00.000Z";
        Timestamp from = Timestamp.from(Instant.parse(dateFrom));
        Timestamp to = Timestamp.from(Instant.parse(dateTo));

        for (Post post : tempPostList) {
            if (post.getPublishedAt().compareTo(from) > 0 && post.getPublishedAt().compareTo(to) < 0) {
                posts.add(post);
            }
        }
        int flag;

        for(int i=0;i<posts.size()-1;i++) {
            flag = 0;
            for(int j=i+1;j<posts.size();j++) {
                if(posts.get(i).getId()==posts.get(j).getId()) {
                    flag = 1;
                }
            }
            if(flag==0) {
                finalPosts.add(posts.get(i));
            }
        }
        finalPosts.add(posts.get(posts.size()-1));


        model.addAttribute("posts", finalPosts);
        return "show_searched_posts.html";
    }
}
