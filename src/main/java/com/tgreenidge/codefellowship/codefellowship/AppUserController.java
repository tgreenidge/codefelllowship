package com.tgreenidge.codefellowship.codefellowship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;


@Controller
public class AppUserController {

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    PasswordEncoder bCryptPasswordEncoder;


    @PostMapping("/users/create")
    public RedirectView createUser(String username, String password, String firstName, String lastName, String bio, String dateOfBirthString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dateOfBirth;

        dateOfBirth = LocalDate.parse(dateOfBirthString, formatter);
        AppUser newUser = new AppUser(username, bCryptPasswordEncoder.encode(password), firstName, lastName, bio, dateOfBirth);
        appUserRepository.save(newUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(newUser, null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new RedirectView("/myprofile");
    }

    @GetMapping("/users")
    public String getUsers(Model m, Principal p) {
        AppUser loggedInUser = appUserRepository.findByUsername(p.getName());
        List<AppUser> users = appUserRepository.findAll();
        System.out.println(Arrays.asList(users).toString());
        Set<AppUser> friends = loggedInUser.getFriends();
        System.out.println(Arrays.asList(friends).toString());

        users.remove(loggedInUser);
        m.addAttribute("loggedInUser", loggedInUser);
        m.addAttribute("users", users);
        m.addAttribute("friends", friends);
        return "users";
    }

    @GetMapping("/users/{id}")
    public String getUserInfoPage(@PathVariable long id, Model m, Principal p) {
//        appUserRepository.findById()
        AppUser user = appUserRepository.findById(id);
        // check if that user belongs to the currently logged in user
        if (user.getUsername().equals(p.getName())) {
            m.addAttribute("user", user);
            return "userinfo";
        } else {
            // otherwise, tell them no
            throw new UserDoesNotBelongToYouException("That User  profile requested is not you.");
        }
    }

    @GetMapping("/signup")
    public String getSignUpPage() {
        return "signup";
    }


    @GetMapping("/login")
    public String getLoginPage(@RequestParam(required = false, defaultValue = "") String showMessage, Model m) {
        m.addAttribute("shouldShowExtraMessage", !showMessage.equals(""));
        return "login";
    }

    @GetMapping("/myprofile")
    public RedirectView getProfilePage(Principal p, Model m) {
        //System.out.println(p.getName());
        AppUser user = appUserRepository.findByUsername(p.getName());
        Long id = user.id;
        m.addAttribute("user", user);

        return new RedirectView("/users/" + id);
    }

    @PostMapping("/users/{id}/friends")
    public RedirectView addFriend(@PathVariable Long id, Long friend, Principal p, Model m) {
        AppUser currentUser = appUserRepository.findById(id).get();
        AppUser newFriend = appUserRepository.findById(friend).get();
        // use the principal: check both  belong to the currently logged in user
        if(newFriend.username.equals(currentUser.username)) {
            throw new UserDoesNotBelongToYouException("You cannot be friends with yourself.");
        }
        // make them friends
        currentUser.friends.add(newFriend);
        newFriend.friends.add(currentUser);

        appUserRepository.save(currentUser);
        appUserRepository.save(newFriend);

        // redirect back to the current user
        return new RedirectView("/users" );
    }

    @GetMapping("/feed")
    public String getFeed(Principal p, Model m) {
        //System.out.println(p.getName());
        AppUser user = appUserRepository.findByUsername(p.getName());
        m.addAttribute("user", user);


        return "userinfo";
    }



    // came from https://stackoverflow.com/questions/2066946/trigger-404-in-spring-mvc-controller
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    class UserDoesNotBelongToYouException extends RuntimeException {
        public UserDoesNotBelongToYouException(String s) {
            super(s);
        }
    }

}
