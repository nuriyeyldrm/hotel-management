package com.backendapi.hotelmanagement.domain;

import com.backendapi.hotelmanagement.domain.enumeration.HotelGroup;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Setter
@Getter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "hotels")
public class Hotel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 15, message = "Size is exceeded")
    @NotNull(message = "Please enter hotel code")
    @Column(nullable = false, unique = true, length = 15)
    private String code;

    @Size(max = 50, message = "Size is exceeded")
    @NotNull(message = "Please enter hotel name")
    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private int stars;

    @Size(max = 250, message = "Size is exceeded")
    @NotNull(message = "Please enter hotel address")
    @Column(nullable = false, length = 250)
    private String address;

    @Pattern(regexp = "^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$",
            message = "Please enter valid phone number")
    @Size(min = 14, max= 14, message = "Phone number should be exact 10 characters")
    @NotNull(message = "Please enter hotel phone number")
    @Column(nullable = false, length = 14)
    private String phone;

    @Email(message = "Please enter valid email")
    @Size(min = 5, max = 254)
    @NotNull(message = "Please enter hotel email")
    @Column(nullable = false, length = 254)
    private String email;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Please enter hotel group")
    @Column(nullable = false)
    private HotelGroup idGroup;

    public Hotel(String code, String name, String address, String phone, String email, HotelGroup idGroup) {
        this.code = code;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.idGroup = idGroup;
    }
}
