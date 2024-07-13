package myCompany.taskManagement.userService.controller;

import myCompany.taskManagement.userService.model.User;
import myCompany.taskManagement.userService.payload.ApiResponse;
import myCompany.taskManagement.userService.payload.LoginRequest;
import myCompany.taskManagement.userService.security.JwtTokenProvider;
import myCompany.taskManagement.userService.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody User user) {
        logger.info("Received request to register user: {}", user.getUsername());
        userService.registerUser(user);
        return new ResponseEntity<>(new ApiResponse(true, "User registered successfully"), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> loginUser(@RequestBody LoginRequest loginRequest) {
        logger.info("Received login request for username: {}", loginRequest.getUsername());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new ApiResponse(true, jwt));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        logger.info("Received request to fetch user by id: {}", id);
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        logger.info("Received request to update user with id: {}", id);
        return new ResponseEntity<>(userService.updateUser(id, user), HttpStatus.OK);
    }

    @PostMapping("/{id}/role")
    public ResponseEntity<ApiResponse> assignRole(@PathVariable Long id, @RequestBody String role) {
        logger.info("Received request to assign role {} to user with id: {}", role, id);
        userService.assignRole(id, role);
        return ResponseEntity.ok(new ApiResponse(true, "Role assigned successfully"));
    }
}
