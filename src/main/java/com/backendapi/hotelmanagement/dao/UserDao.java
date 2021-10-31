package com.backendapi.hotelmanagement.dao;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDao {

    @Size(min = 3, max = 20, message = "Please enter min 3 characters")
    @NotNull(message = "Please enter your user name")
    private String username;

    @JsonIgnore
    private String password;

    @Email(message = "Please enter valid email")
    @Size(min = 5, max = 150)
    @NotNull(message = "Please enter your email")
    private String email;

    @Size(max = 50)
    @NotNull(message = "Please enter your full name")
    private String fullName;

    @Pattern(regexp = "^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$",
            message = "Please enter valid phone number")
    @Size(min = 14, max= 14, message = "Phone number should be exact 10 characters")
    @NotNull(message = "Please enter your phone number")
    private String phoneNumber;

    @Pattern(regexp = "^(?!000|666)[0-8][0-9]{2}-(?!00)[0-9]{2}-(?!0000)[0-9]{4}$", message = "Please enter valid SSN")
    @Size(min = 11, max= 11, message = "SSN should be exact 9 characters")
    @NotNull(message = "Please enter your SSN")
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

    public UserDao(String username, String email, String fullName, String phoneNumber, String ssn, String drivingLicense,
                   String country, String state, String address, String workingSector, Date birthDate) {
        this.username = username;
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
}
