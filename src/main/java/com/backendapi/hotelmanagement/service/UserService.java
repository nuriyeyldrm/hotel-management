package com.backendapi.hotelmanagement.service;

import com.backendapi.hotelmanagement.domain.User;
import com.backendapi.hotelmanagement.domain.enumeration.UserRole;
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

    private final static String USER_NOT_FOUND_MSG = "user with user name %s not found";
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, userName)));
    }

    public List<User> fetchAllUsers(){
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public void register(User user) {

        boolean userExists = userRepository.findByUsername(user.getUsername()).isPresent();

        if (userExists){
            throw new IllegalStateException("user name already taken");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());

        user.setPassword(encodedPassword);
        user.setUserRole(UserRole.USER);

        userRepository.save(user);
    }

    public Optional<User> login(String username, String password){
        Optional<User> user = userRepository.findByUsername(username);
        if (!BCrypt.checkpw(password, user.get().getPassword()))
            throw new RuntimeException("invalid credentials");
        return user;
    }

    public void updateUser(Long id, User user) {
        userRepository.update(id, user.getEmail(), user.getFullName(), user.getPhoneNumber(), user.getSsn(),
                user.getDrivingLicense(), user.getCountry(), user.getState(), user.getAddress(),
                user.getWorkingSector(), user.getBirthDate());
    }

    public void updatePassword(Long id, String newPassword, String oldPassword){
        Optional<User> user = userRepository.findById(id);
        if (!(BCrypt.hashpw(oldPassword, user.get().getPassword()).equals(user.get().getPassword())))
            throw new RuntimeException("password does not match");
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(10));
        userRepository.updatePassword(id, hashedPassword);
    }

    public void removeUser(Long id){
        boolean userExists = userRepository.findById(id).isPresent();

        if (!userExists){
            throw new IllegalStateException(" id does not exist");
        }
        userRepository.deleteById(id);
    }
}
