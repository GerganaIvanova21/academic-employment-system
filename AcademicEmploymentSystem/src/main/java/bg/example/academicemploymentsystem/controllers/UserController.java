package bg.example.academicemploymentsystem.controllers;

import bg.example.academicemploymentsystem.entities.User;
import bg.example.academicemploymentsystem.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/egn/{egn}")
    public ResponseEntity<User> getUserByEgn(@PathVariable String egn) {
        User user = userService.getUserByEgn(egn);
        return ResponseEntity.ok(user);
    }

}
