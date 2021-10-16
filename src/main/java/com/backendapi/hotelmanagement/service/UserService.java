package com.backendapi.hotelmanagement.service;

import com.backendapi.hotelmanagement.domain.User;
import com.backendapi.hotelmanagement.domain.enumeration.UserRole;
import com.backendapi.hotelmanagement.exception.AuthException;
import com.backendapi.hotelmanagement.exception.BadRequestException;
import com.backendapi.hotelmanagement.exception.ConflictException;
import com.backendapi.hotelmanagement.exception.ResourceNotFoundException;
import com.backendapi.hotelmanagement.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final static String USERNAME_NOT_FOUND_MSG = "user with user name %s not found";
    private final static String USER_NOT_FOUND_MSG = "user with id %d not found";
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USERNAME_NOT_FOUND_MSG, userName)));
    }

    public List<User> fetchAllUsers(){
        return userRepository.findAll();
    }

    public User findById(Long id) throws ResourceNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(USER_NOT_FOUND_MSG, id)));
    }

    public void register(User user) throws BadRequestException {

        boolean userExists = userRepository.findByUsername(user.getUsername()).isPresent();
        boolean emailExists = userRepository.findByEmail(user.getEmail()).isPresent();
        boolean ssnExists = userRepository.findBySsn(user.getSsn()).isPresent();

        if (userExists){
            throw new ConflictException("user name already taken");
        }

        if (emailExists){
            throw new ConflictException("email already taken");
        }

        if (ssnExists){
            throw new ConflictException("SSN already taken");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());

        user.setPassword(encodedPassword);
        user.setUserRole(UserRole.Customer);

        userRepository.save(user);
    }

    public Optional<User> login(String username, String password) throws AuthException {
        try {
            Optional<User> user = userRepository.findByUsername(username);

            if (!BCrypt.checkpw(password, user.get().getPassword()))
                throw new AuthException("invalid credentials");
            return user;
        } catch (Exception e) {
            throw new AuthException("invalid credentials");
        }
    }

    public void updateUser(Long id, User user) throws BadRequestException {

        boolean emailExists = userRepository.findByEmail(user.getEmail()).isPresent();
        boolean ssnExists = userRepository.findBySsn(user.getSsn()).isPresent();
        Optional<User> userDetails = userRepository.findById(id);

        if (emailExists && !user.getEmail().equals(userDetails.get().getEmail())){
            throw new ConflictException("email already taken");
        }

        if (ssnExists && !user.getSsn().equals(userDetails.get().getSsn())){
            throw new ConflictException("SSN already taken");
        }

        userRepository.update(id, user.getEmail(), user.getFullName(), user.getPhoneNumber(), user.getSsn(),
                user.getDrivingLicense(), user.getCountry(), user.getState(), user.getAddress(),
                user.getWorkingSector(), user.getBirthDate());
    }

    public void updatePassword(Long id, String newPassword, String oldPassword) throws BadRequestException {
        Optional<User> user = userRepository.findById(id);
        if (!(BCrypt.hashpw(oldPassword, user.get().getPassword()).equals(user.get().getPassword())))
            throw new BadRequestException("password does not match");
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(10));
        user.get().setPassword(hashedPassword);
        userRepository.save(user.get());
    }

    public void removeById(Long id) throws ResourceNotFoundException {
        boolean userExists = userRepository.findById(id).isPresent();

        if (!userExists){
            throw new ResourceNotFoundException("user not exist");
        }
        userRepository.deleteById(id);
    }
}
