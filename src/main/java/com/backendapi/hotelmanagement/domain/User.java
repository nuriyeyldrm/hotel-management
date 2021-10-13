package com.backendapi.hotelmanagement.domain;

import com.backendapi.hotelmanagement.domain.enumeration.UserRole;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@Table(name = "users")
@Entity
public class User implements UserDetails {

    @Id
    @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    private Long id;

    @Size(max = 50)
    @NotNull(message = "Please enter your user name")
    @Column(nullable = false, unique = true, updatable = false, length = 50)
    private String username;

    @NotNull(message = "Please enter your password")
    @Size(min = 4, max = 60, message = "Please enter min 4 characters")
    @Column(name = "password_hash", nullable = false, length = 60)
    private String password;

    @Email(message = "Please enter valid email")
    @NotNull(message = "Please enter your email")
    @Size(min = 5, max = 254)
    @Column(nullable = false, unique = true, length = 254)
    private String email;

    @Size(max = 50)
    @NotNull(message = "Please enter your full name")
    @Column(nullable = false, length = 50)
    private String fullName;

    @Pattern(regexp = "^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$",
            message = "Please enter valid phone number")
    @Size(min = 14, max= 14, message = "Phone number should be exact 10 characters")
    @NotNull(message = "Please enter your phone number")
    @Column(nullable = false, length = 14)
    private String phoneNumber;

    @Pattern(regexp = "^(?!000|666)[0-8][0-9]{2}-(?!00)[0-9]{2}-(?!0000)[0-9]{4}$", message = "Please enter valid SSN")
    @Size(min = 11, max= 11, message = "SSN should be exact 9 characters")
    @NotNull(message = "Please enter your SSN")
    @Column(nullable = false, unique = true, length = 11)
    private String ssn;

    @NotNull(message = "Please enter your driving license")
    private String drivingLicense;

    @NotNull(message = "Please enter your country")
    private String country;

    private String state;

    @NotNull(message = "Please enter your address")
    private String address;

    @NotNull(message = "Please enter your working sector")
    private String workingSector;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="MM/dd/yyyy")
    @NotNull(message = "Please enter your birth date")
    private Date birthDate;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private Boolean locked = false;

    private Boolean enabled = true;

    public User(String email, String fullName, String phoneNumber, String ssn,
                String drivingLicense, String country, String state, String address,
                String workingSector, Date birthDate) {
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.ssn = ssn;
        this.drivingLicense = drivingLicense;
        this.country = country;
        this.state = state;
        this.address = address;
        this.workingSector = workingSector;
        this.birthDate = birthDate;
    }

    public User(String username, String password, String email, String fullName, String phoneNumber, String ssn,
                String drivingLicense, String country, String state, String address,
                String workingSector, Date birthDate) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.ssn = ssn;
        this.drivingLicense = drivingLicense;
        this.country = country;
        this.state = state;
        this.address = address;
        this.workingSector = workingSector;
        this.birthDate = birthDate;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        grantedAuthorityList.add(new SimpleGrantedAuthority(userRole.name()));
        return grantedAuthorityList;
    }



    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(username, user.username) &&
                Objects.equals(password, user.password) &&
                Objects.equals(email, user.email) &&
                Objects.equals(fullName, user.fullName) &&
                Objects.equals(phoneNumber, user.phoneNumber) &&
                Objects.equals(ssn, user.ssn) &&
                Objects.equals(drivingLicense, user.drivingLicense) &&
                Objects.equals(country, user.country) &&
                Objects.equals(state, user.state) &&
                Objects.equals(address, user.address) &&
                Objects.equals(birthDate, user.birthDate) &&
                userRole == user.userRole &&
                Objects.equals(locked, user.locked) &&
                Objects.equals(enabled, user.enabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, email, fullName, phoneNumber, ssn, drivingLicense, country,
                state, address, birthDate, userRole, locked, enabled);
    }
}
