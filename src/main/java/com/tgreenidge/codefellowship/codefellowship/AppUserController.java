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


@Controller
public class AppUserController {

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    PasswordEncoder bCryptPasswordEncoder;


    @PostMapping("/users")
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
    public String getProfilePage(Principal p, Model m) {
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
