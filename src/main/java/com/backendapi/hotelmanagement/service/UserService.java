package com.backendapi.hotelmanagement.service;

import com.backendapi.hotelmanagement.domain.Role;
import com.backendapi.hotelmanagement.domain.User;
import com.backendapi.hotelmanagement.domain.enumeration.UserRole;
import com.backendapi.hotelmanagement.exception.AuthException;
import com.backendapi.hotelmanagement.exception.BadRequestException;
import com.backendapi.hotelmanagement.exception.ConflictException;
import com.backendapi.hotelmanagement.exception.ResourceNotFoundException;
import com.backendapi.hotelmanagement.repository.RoleRepository;
import com.backendapi.hotelmanagement.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final static String USERNAME_NOT_FOUND_MSG = "user with id %s not found";

    public List<User> fetchAllUsers(){
        return userRepository.findAll();
    }

    public User findByUsername(String username) throws ResourceNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(USERNAME_NOT_FOUND_MSG, username)));
    }

    public void register(User user) throws BadRequestException {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new ConflictException("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ConflictException("Error: Email is already in use!");
        }

        if (userRepository.existsBySsn(user.getSsn())) {
            throw new ConflictException("Error: SSN is already in use!");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());

        user.setPassword(encodedPassword);

        Set<Role> roles = new HashSet<>();
        Role customerRole = roleRepository.findByName(UserRole.ROLE_CUSTOMER)
                .orElseThrow(() -> new ResourceNotFoundException("Error: Role is not found."));
        roles.add(customerRole);

        user.setRoles(roles);
        userRepository.save(user);
    }

    public void login(String username, String password) throws AuthException {
        try {
            Optional<User> user = userRepository.findByUsername(username);

            if (!BCrypt.checkpw(password, user.get().getPassword()))
                throw new AuthException("invalid credentials");
        } catch (Exception e) {
            throw new AuthException("invalid credentials");
        }
    }

    public void updateUser(String username, User user) throws BadRequestException {

        boolean emailExists = userRepository.existsByEmail(user.getEmail());
        boolean ssnExists = userRepository.existsBySsn(user.getSsn());
        Optional<User> userDetails = userRepository.findByUsername(username);

        if (emailExists && !user.getEmail().equals(userDetails.get().getEmail())){
            throw new ConflictException("Error: Email is already in use!");
        }

        if (ssnExists && !user.getSsn().equals(userDetails.get().getSsn())){
            throw new ConflictException("Error: SSN is already in use!");
        }

        userRepository.update(username, user.getEmail(), user.getFullName(), user.getPhoneNumber(), user.getSsn(),
                user.getDrivingLicense(), user.getCountry(), user.getState(), user.getAddress(),
                user.getWorkingSector(), user.getBirthDate());
    }

    public void updatePassword(String username, String newPassword, String oldPassword) throws BadRequestException {
        Optional<User> user = userRepository.findByUsername(username);
        if (!(BCrypt.hashpw(oldPassword, user.get().getPassword()).equals(user.get().getPassword())))
            throw new BadRequestException("password does not match");

        String hashedPassword = passwordEncoder.encode(newPassword);
        user.get().setPassword(hashedPassword);
        userRepository.save(user.get());
    }

    public void removeByUsername(String username) throws ResourceNotFoundException {
        boolean userExists = userRepository.existsByUsername(username);

        if (!userExists){
            throw new ResourceNotFoundException("user does not exist");
        }
        Optional<User> user = userRepository.findByUsername(username);
        userRepository.deleteById(user.get().getId());
    }
}
