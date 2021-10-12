package com.backendapi.hotelmanagement.controller;

import com.backendapi.hotelmanagement.domain.User;
import com.backendapi.hotelmanagement.constant.Constants;
import com.backendapi.hotelmanagement.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.*;

@AllArgsConstructor
@RestController
@Produces(MediaType.APPLICATION_JSON)
@RequestMapping(path = "/api/user")
public class UserController {

    public UserService userService;

    @GetMapping("/auth/all")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.fetchAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/auth")
    public ResponseEntity<Optional<User>> getUserById(HttpServletRequest request){
        Long id = (Long) request.getAttribute("id");
        Optional<User> user = userService.findById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Boolean>> register(@Valid @RequestBody User user) {
        userService.register(user);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, Object> userMap){
        String username = (String) userMap.get("username");
        String password = (String) userMap.get("password");
        Optional<User> user = userService.login(username, password);
        return new ResponseEntity<>(generateJWTToken(user), HttpStatus.OK);
    }

    @PutMapping("/auth")
    public ResponseEntity<Map<String, Boolean>> updateUser(HttpServletRequest request,
                                                           @Valid @RequestBody User user) {
        Long id = (Long) request.getAttribute("id");
        userService.updateUser(id, user);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PutMapping("/auth/password")
    public ResponseEntity<Map<String, Boolean>> updatePassword(HttpServletRequest request,
                                                               @RequestBody Map<String, Object> userMap) {
        Long id = (Long) request.getAttribute("id");
        String newPassword = (String) userMap.get("newPassword");
        String oldPassword = (String) userMap.get("oldPassword");
        userService.updatePassword(id, newPassword, oldPassword);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @DeleteMapping("/auth")
    public ResponseEntity<Map<String, Boolean>> deleteUser(HttpServletRequest request){
        Long id = (Long) request.getAttribute("id");
        userService.removeById(id);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    private Map<String, String> generateJWTToken(Optional<User> user){
        long timestamp = System.currentTimeMillis();
        String token = Jwts.builder().signWith(SignatureAlgorithm.HS256, Constants.API_SECRET_KEY)
                .setIssuedAt(new Date(timestamp))
                .setExpiration(new Date(timestamp + Constants.TOKEN_VALIDITY))
                .claim("id", user.get().getId())
                .claim("username", user.get().getUsername())
                .claim("password", user.get().getPassword())
                .compact();
        Map<String, String> map = new HashMap<>();
        map.put("id_token", token);
        return map;
    }
}
