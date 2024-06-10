package sk.tuke.gamestudio.server.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.entity.User;
import sk.tuke.gamestudio.service.UserService;
import sk.tuke.gamestudio.service.UserException;

@RestController
@RequestMapping("/api/user")
public class UserServiceRest {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public void registerUser(@RequestBody User user) {
        try {
            userService.registerUser(user);
        } catch (UserException e) {
            // handle exception
        }
    }

    @GetMapping("/{username}")
    public User findByUsername(@PathVariable String username) {
        try {
            return userService.findByUsername(username);
        } catch (UserException e) {
            // handle exception
            return null;
        }
    }

    @DeleteMapping("/reset")
    public void reset() {
        try {
            userService.reset();
        } catch (UserException e) {
            // handle exception
        }
    }
}