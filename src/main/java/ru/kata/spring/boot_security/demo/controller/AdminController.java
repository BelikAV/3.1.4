package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String adminPage(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("newUser", new User());
        return "admin";
    }

    @GetMapping("/api/users")
    @ResponseBody
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/api/add")
    @ResponseBody
    public ResponseEntity<?> addUser(@RequestBody User user) {
        try {
            userService.saveUser(user);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/api/edit/{id}")
    @ResponseBody
    public ResponseEntity<?> editUser(@PathVariable Long id, @RequestBody User userData) {
        try {
            User user = userService.getUser(id);
            if (user == null) return ResponseEntity.notFound().build();

            user.setUsername(userData.getUsername());
            user.setName(userData.getName());
            user.setAge(userData.getAge());
            user.setEmail(userData.getEmail());
            if (userData.getPassword() != null && !userData.getPassword().isEmpty()) {
                user.setPassword(userData.getPassword());
            }

            userService.updateUser(user);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/api/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
