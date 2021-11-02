package com.backendapi.hotelmanagement.controller;

import com.backendapi.hotelmanagement.dao.AdminDao;
import com.backendapi.hotelmanagement.dao.UserDao;
import com.backendapi.hotelmanagement.dao.PagingResponse;
import com.backendapi.hotelmanagement.domain.User;
import com.backendapi.hotelmanagement.domain.enumeration.PagingHeaders;
import com.backendapi.hotelmanagement.security.jwt.JwtUtils;
import com.backendapi.hotelmanagement.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Join;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.*;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
@RestController
@Produces(MediaType.APPLICATION_JSON)
@RequestMapping("/api")
public class UserController {

    public UserService userService;

    public AuthenticationManager authenticationManager;

    public JwtUtils jwtUtils;

    @GetMapping("/admin/auth/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.fetchAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/admin/{id}/auth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> getUserById(@PathVariable Long id){
        User user = userService.findById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Transactional
    @GetMapping(value = "/admin/auth/search")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> get(
            @Join(path = "roles", alias = "r")
            @And({
                    @Spec(path = "id", params = "id", spec = Equal.class),
                    @Spec(path = "username", params = "username", spec = Like.class),
                    @Spec(path = "email", params = "email", spec = Like.class),
                    @Spec(path = "fullName", params = "fullName", spec = Like.class),
                    @Spec(path = "birthDate",  params = "birthDate", spec = Equal.class),
                    @Spec(path = "phoneNumber", params = "phoneNumber", spec = Like.class),
                    @Spec(path = "r.id", params = "role", spec = Equal.class),
                    @Spec(path = "enabled", params = "enabled", spec = Equal.class)
            }) Specification<User> spec,
            Sort sort,
            @RequestHeader HttpHeaders headers) {
        final PagingResponse response = userService.get(spec, headers, sort);
        return new ResponseEntity<>(response.getElements(), returnHttpHeaders(response), HttpStatus.OK);
    }

    @GetMapping("/user/auth")
    public ResponseEntity<UserDao> getUserByUsername(HttpServletRequest request){
        String username = (String) request.getAttribute("username");
        UserDao userDao = userService.findByUsername(username);
        return new ResponseEntity<>(userDao, HttpStatus.OK);
    }

    @PostMapping("/admin/auth/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Boolean>> addUser(@Valid @RequestBody AdminDao adminDao) {
        userService.add(adminDao);

        Map<String, Boolean> map = new HashMap<>();
        map.put("User registered successfully!", true);
        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

    @PostMapping("/user/signup")
    public ResponseEntity<Map<String, Boolean>> registerUser(@Valid @RequestBody User user) {
        userService.register(user);

        Map<String, Boolean> map = new HashMap<>();
        map.put("User registered successfully!", true);
        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

    @PostMapping("/user/login")
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

    @PutMapping("/user/auth")
    public ResponseEntity<Map<String, Boolean>> updateUser(HttpServletRequest request,
                                                           @Valid @RequestBody UserDao userDao) {
        String username = (String) request.getAttribute("username");
        userService.updateUser(username, userDao);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PutMapping("/admin/{id}/auth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Boolean>> updateUserAuth(@PathVariable Long id,
                                                           @Valid @RequestBody AdminDao adminDao) {
        userService.updateUserAuth(id, adminDao);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PatchMapping("/user/auth")
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

    @DeleteMapping("/admin/{id}/auth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Boolean>> deleteUser(@PathVariable Long id){
        userService.removeByUsername(id);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    public HttpHeaders returnHttpHeaders(PagingResponse response) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(PagingHeaders.COUNT.getName(), String.valueOf(response.getCount()));
        headers.set(PagingHeaders.PAGE_SIZE.getName(), String.valueOf(response.getPageSize()));
        headers.set(PagingHeaders.PAGE_OFFSET.getName(), String.valueOf(response.getPageOffset()));
        headers.set(PagingHeaders.PAGE_NUMBER.getName(), String.valueOf(response.getPageNumber()));
        headers.set(PagingHeaders.PAGE_TOTAL.getName(), String.valueOf(response.getPageTotal()));
        return headers;
    }

}
