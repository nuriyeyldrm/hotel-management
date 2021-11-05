package com.backendapi.hotelmanagement.service;

import com.backendapi.hotelmanagement.dao.AdminDao;
import com.backendapi.hotelmanagement.dao.UserDao;
import com.backendapi.hotelmanagement.dao.PagingResponse;
import com.backendapi.hotelmanagement.domain.Role;
import com.backendapi.hotelmanagement.domain.User;
import com.backendapi.hotelmanagement.domain.enumeration.PagingHeaders;
import com.backendapi.hotelmanagement.domain.enumeration.UserRole;
import com.backendapi.hotelmanagement.exception.AuthException;
import com.backendapi.hotelmanagement.exception.BadRequestException;
import com.backendapi.hotelmanagement.exception.ConflictException;
import com.backendapi.hotelmanagement.exception.ResourceNotFoundException;
import com.backendapi.hotelmanagement.repository.RoleRepository;
import com.backendapi.hotelmanagement.repository.UserPageableRepository;
import com.backendapi.hotelmanagement.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final UserPageableRepository userPageableRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final static String USERNAME_NOT_FOUND_MSG = "user with username %s not found";

    private final static String USER_NOT_FOUND_MSG = "user with id %d not found";

    public List<User> fetchAllUsers(){
        return userRepository.findAll();
    }

    public UserDao findByUsername(String username) throws ResourceNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(USERNAME_NOT_FOUND_MSG, username)));

        return new UserDao(user.getUsername(), user.getEmail(), user.getFullName(), user.getPhoneNumber(),
                user.getSsn(), user.getDrivingLicense(), user.getCountry(), user.getState(), user.getAddress(),
                user.getWorkingSector(), user.getBirthDate());
    }

    public User findById(Long id) throws ResourceNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(USER_NOT_FOUND_MSG, id)));
    }

    public PagingResponse get(Specification<User> spec, Pageable pageable) {
        Page<User> page = userPageableRepository.findAll(spec, pageable);
        List<User> content = page.getContent();
        return new PagingResponse(page.getTotalElements(), (long) page.getNumber(),
                (long) page.getNumberOfElements(), pageable.getOffset(), (long) page.getTotalPages(), content);
    }

    public List<User> get(Specification<User> spec, Sort sort) {
        return userPageableRepository.findAll(spec, sort);
    }

    public PagingResponse get(Specification<User> spec, HttpHeaders headers, Sort sort) {
        if (isRequestPaged(headers)) {
            return get(spec, buildPageRequest(headers, sort));
        } else {
            List<User> entities = get(spec, sort);
            return new PagingResponse((long) entities.size(), 0L,
                    0L, 0L, 0L, entities);
        }
    }

    private boolean isRequestPaged(HttpHeaders headers) {
        return headers.containsKey(PagingHeaders.PAGE_NUMBER.getName()) &&
                headers.containsKey(PagingHeaders.PAGE_SIZE.getName());
    }

    private Pageable buildPageRequest(HttpHeaders headers, Sort sort) {
        int page = Integer.parseInt(Objects.requireNonNull(headers.get(PagingHeaders.PAGE_NUMBER.getName())).get(0));
        int size = Integer.parseInt(Objects.requireNonNull(headers.get(PagingHeaders.PAGE_SIZE.getName())).get(0));
        return PageRequest.of(page, size, sort);
    }


    public void add(AdminDao adminDao) throws BadRequestException {
        if (userRepository.existsByUsername(adminDao.getUsername())) {
            throw new ConflictException("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(adminDao.getEmail())) {
            throw new ConflictException("Error: Email is already in use!");
        }

        if (userRepository.existsBySsn(adminDao.getSsn())) {
            throw new ConflictException("Error: SSN is already in use!");
        }

        String encodedPassword = passwordEncoder.encode(adminDao.getPassword());

        if (adminDao.getEnabled() == null)
            adminDao.setEnabled(false);

        User user = new User(adminDao.getUsername(), encodedPassword, adminDao.getEmail(), adminDao.getFullName(),
                adminDao.getPhoneNumber(), adminDao.getSsn(), adminDao.getDrivingLicense(), adminDao.getCountry(),
                adminDao.getState(), adminDao.getAddress(), adminDao.getWorkingSector(), adminDao.getBirthDate(),
                adminDao.getEnabled());

        Set<String> userRoles = adminDao.getRole();
        Set<Role> roles = addRoles(userRoles);

        user.setRoles(roles);
        userRepository.save(user);
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
        user.setEnabled(true);

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

    public void updateUser(String username, UserDao userDao) throws BadRequestException {

        boolean emailExists = userRepository.existsByEmail(userDao.getEmail());
        boolean ssnExists = userRepository.existsBySsn(userDao.getSsn());
        Optional<User> userDetails = userRepository.findByUsername(username);

        if (emailExists && !userDao.getEmail().equals(userDetails.get().getEmail())){
            throw new ConflictException("Error: Email is already in use!");
        }

        if (ssnExists && !userDao.getSsn().equals(userDetails.get().getSsn())){
            throw new ConflictException("Error: SSN is already in use!");
        }

        userRepository.update(username, userDao.getEmail(), userDao.getFullName(), userDao.getPhoneNumber(),
                userDao.getSsn(), userDao.getDrivingLicense(), userDao.getCountry(), userDao.getState(),
                userDao.getAddress(), userDao.getWorkingSector(), userDao.getBirthDate());
    }

    public void updateUserAuth(Long id, AdminDao adminDao) throws BadRequestException {

        boolean emailExists = userRepository.existsByEmail(adminDao.getEmail());
        boolean ssnExists = userRepository.existsBySsn(adminDao.getSsn());
        Optional<User> userDetails = userRepository.findById(id);

        if (emailExists && !adminDao.getEmail().equals(userDetails.get().getEmail())){
            throw new ConflictException("Error: Email is already in use!");
        }

        if (ssnExists && !adminDao.getSsn().equals(userDetails.get().getSsn())){
            throw new ConflictException("Error: SSN is already in use!");
        }

        String encodedPassword = passwordEncoder.encode(adminDao.getPassword());

        adminDao.setPassword(encodedPassword);

        Set<String> userRoles = adminDao.getRole();
        Set<Role> roles = addRoles(userRoles);

        User user = new User(id, adminDao.getUsername(), adminDao.getPassword(), adminDao.getEmail(),
                adminDao.getFullName(), adminDao.getPhoneNumber(), adminDao.getSsn(), adminDao.getDrivingLicense(),
                adminDao.getCountry(), adminDao.getState(), adminDao.getAddress(), adminDao.getWorkingSector(),
                adminDao.getBirthDate(), roles, adminDao.getEnabled());

        userRepository.save(user);
    }

    public void updatePassword(String username, String newPassword, String oldPassword) throws BadRequestException {
        Optional<User> user = userRepository.findByUsername(username);
        if (!(BCrypt.hashpw(oldPassword, user.get().getPassword()).equals(user.get().getPassword())))
            throw new BadRequestException("password does not match");

        String hashedPassword = passwordEncoder.encode(newPassword);
        user.get().setPassword(hashedPassword);
        userRepository.save(user.get());
    }

    public void removeByUsername(Long id) throws ResourceNotFoundException {
        boolean userExists = userRepository.existsById(id);

        if (!userExists){
            throw new ResourceNotFoundException("user does not exist");
        }

        userRepository.deleteById(id);
    }

    public Set<Role> addRoles(Set<String> userRoles) {
        Set<Role> roles = new HashSet<>();

        if (userRoles == null) {
            Role userRole = roleRepository.findByName(UserRole.ROLE_CUSTOMER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            userRoles.forEach(role -> {
                switch (role) {
                    case "Administrator":
                        Role adminRole = roleRepository.findByName(UserRole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "CustomerService":
                        Role customerServiceRole = roleRepository.findByName(UserRole.ROLE_CUSTOMER_SERVICE)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(customerServiceRole);

                        break;

                    case "Manager":
                        Role managerRole = roleRepository.findByName(UserRole.ROLE_MANAGER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(managerRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(UserRole.ROLE_CUSTOMER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        return roles;
    }
}
