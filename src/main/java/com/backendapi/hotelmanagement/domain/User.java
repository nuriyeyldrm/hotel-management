package com.backendapi.hotelmanagement.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 3, max = 20, message = "Please enter min 3 characters")
    @NotNull(message = "Please enter your user name")
    @Column(nullable = false, unique = true, updatable = false, length = 20)
    private String username;

    @Size(min = 4, max = 60, message = "Please enter min 4 characters")
    @NotNull(message = "Please enter your password")
    @Column(nullable = false, length = 120)
    private String password;

    @Email(message = "Please enter valid email")
    @Size(min = 5, max = 150)
    @NotNull(message = "Please enter your email")
    @Column(nullable = false, unique = true, length = 150)
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
    @Column(nullable = false)
    private String drivingLicense;

    @NotNull(message = "Please enter your country")
    @Column(nullable = false)
    private String country;

    @NotNull(message = "Please enter your state")
    @Column(nullable = false)
    private String state;

    @NotNull(message = "Please enter your address")
    @Column(nullable = false)
    private String address;

    @NotNull(message = "Please enter your working sector")
    @Column(nullable = false)
    private String workingSector;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="MM/dd/yyyy")
    @NotNull(message = "Please enter your birth date")
    @Column(nullable = false)
    private Date birthDate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    private Boolean enabled;

    public User(String username, String password, String email, String fullName, String phoneNumber, String ssn,
                String drivingLicense, String country, String state, String address,
                String workingSector, Date birthDate, Boolean enabled) {
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
        this.enabled = enabled;
    }
}
