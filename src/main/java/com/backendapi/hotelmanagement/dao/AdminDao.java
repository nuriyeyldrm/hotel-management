package com.backendapi.hotelmanagement.dao;

import com.fasterxml.jackson.annotation.JsonFormat;
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
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdminDao {

    @Size(min = 3, max = 20, message = "Please enter min 3 characters")
    @NotNull(message = "Please enter your user name")
    private String username;

    @Size(min = 4, max = 60, message = "Please enter min 4 characters")
    @NotNull(message = "Please enter your password")
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

    private Set<String> role;

    private Boolean enabled;
}
