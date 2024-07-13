package myCompany.taskManagement.userService.service;

import myCompany.taskManagement.userService.model.Role;
import myCompany.taskManagement.userService.model.User;
import myCompany.taskManagement.userService.repository.UserRepository;
import myCompany.taskManagement.userService.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService implements org.springframework.security.core.userdetails.UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            logger.error("Username {} is already taken", user.getUsername());
            throw new RuntimeException("Username is already taken");
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            logger.error("Email {} is already taken", user.getEmail());
            throw new RuntimeException("Email is already taken");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        logger.info("User {} registered successfully", user.getUsername());
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id : " + id));
    }

    public User updateUser(Long id, User userDetails) {
        User user = getUserById(id);
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        user.setRole(userDetails.getRole());
        return userRepository.save(user);
    }

    public void assignRole(Long id, String role) {
        User user = getUserById(id);
        user.setRole(Role.valueOf(role));
        userRepository.save(user);
        logger.info("Role {} assigned to user with id {}", role, id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username : " + username));
        return UserPrincipal.create(user);
    }

    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found with id : " + id)
        );

        return UserPrincipal.create(user);
    }
}
