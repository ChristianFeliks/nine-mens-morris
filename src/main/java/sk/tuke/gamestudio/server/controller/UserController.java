package sk.tuke.gamestudio.server.controller;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.entity.User;
import sk.tuke.gamestudio.service.UserService;
import sk.tuke.gamestudio.service.UserException;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/ninemensmorris")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "ninemensmorris_register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        try {
            User existingUser = userService.findByUsername(user.getUsername());
            if (existingUser != null) {
                model.addAttribute("registrationError", "Username is already in use. Please choose a different one.");
                return "ninemensmorris_register";
            }
            userService.registerUser(user);
            return "ninemensmorris_login"; // redirect to login page after successful registration
        } catch (UserException e) {
            model.addAttribute("registrationError", e.getMessage());
            return "ninemensmorris_register";
        }
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "ninemensmorris_login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String username, @RequestParam String password, Model model, HttpSession session) {
        try {
            User user = userService.findByUsername(username);
            if (user != null && BCrypt.checkpw(password, user.getPassword())) {
                session.setAttribute("username", username); // store the username in the session
                return "redirect:/ninemensmorris/startup";
            } else {
                model.addAttribute("loginError", "Invalid username or password.");
                return "ninemensmorris_login";
            }
        } catch (UserException e) {
            model.addAttribute("error", e.getMessage());
            return "ninemensmorris_login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // invalidate the session
        return "redirect:/ninemensmorris/login"; // redirect to the login page
    }
}