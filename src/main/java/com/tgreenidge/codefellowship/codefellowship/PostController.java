package com.tgreenidge.codefellowship.codefellowship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.List;

@Controller
public class PostController {
    @Autowired
    PostRepository postRepository;
    @Autowired
    AppUserRepository appUserRepository;

    @GetMapping("/posts/add")
    public String getPostCreator() {
        return "createPost";
    }

    @PostMapping("/posts")
    public RedirectView addPost(String body, Principal p) {
        Post newPost = new Post(body);
        AppUser me = appUserRepository.findByUsername(p.getName());
        newPost.creator = me;
        postRepository.save(newPost);
        return new RedirectView("/myprofile");
    }
}

