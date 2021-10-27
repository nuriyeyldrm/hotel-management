package com.backendapi.hotelmanagement.controller;

import com.backendapi.hotelmanagement.domain.User;
import com.backendapi.hotelmanagement.security.jwt.JwtUtils;
import com.backendapi.hotelmanagement.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
@RestController
@Produces(MediaType.APPLICATION_JSON)
@RequestMapping("/api/user")
public class UserController {

    public UserService userService;

    public AuthenticationManager authenticationManager;

    public JwtUtils jwtUtils;

    @GetMapping("/auth/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.fetchAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/auth")
    public ResponseEntity<User> getUserByUsername(HttpServletRequest request){
        String username = (String) request.getAttribute("username");
        User user = userService.findByUsername(username);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Boolean>> registerUser(@Valid @RequestBody User user) {
        userService.register(user);

        Map<String, Boolean> map = new HashMap<>();
        map.put("User registered successfully!", true);
        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> authenticateUser(@RequestBody Map<String, Object> userMap){
        String username = (String) userMap.get("username");
        String password = (String) userMap.get("password");

        userService.login(username, password);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        Map<String, String> map = new HashMap<>();
        map.put("token", jwt);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PutMapping("/auth")
    public ResponseEntity<Map<String, Boolean>> updateUser(HttpServletRequest request,
                                                           @Valid @RequestBody User user) {
        String username = (String) request.getAttribute("username");
        userService.updateUser(username, user);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PatchMapping("/auth")
    public ResponseEntity<Map<String, Boolean>> updatePassword(HttpServletRequest request,
                                                               @RequestBody Map<String, Object> userMap) {
        String username = (String) request.getAttribute("username");
        String newPassword = (String) userMap.get("newPassword");
        String oldPassword = (String) userMap.get("oldPassword");
        userService.updatePassword(username, newPassword, oldPassword);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @DeleteMapping("/auth")
    public ResponseEntity<Map<String, Boolean>> deleteUser(HttpServletRequest request){
        String username = (String) request.getAttribute("username");
        userService.removeByUsername(username);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

}
