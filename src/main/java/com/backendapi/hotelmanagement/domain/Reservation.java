package com.backendapi.hotelmanagement.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
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
import java.util.Date;

@Setter
@Getter
@ToString
@NoArgsConstructor
@Table(name = "reservations")
@Entity
public class Reservation implements Serializable {

    @Id
    @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    private Long id;

    @Size(max = 50, message = "Size is exceeded")
    @NotNull(message = "Please choose room")
    @Column(nullable = false, length = 50)
    private String hotelRoom;

    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username",
            insertable = false, updatable = false, nullable = false)
    @NotNull(message = "Please enter username")
    private User user;

    @NotNull(message = "Please enter room price")
    @Column(nullable = false)
    private Double price;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="MM/dd/yyyy")
    @NotNull(message = "Please enter start date")
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="MM/dd/yyyy")
    @NotNull(message = "Please enter end date")
    private Date endDate;

    @NotNull(message = "Please enter number of adult")
    @Column(nullable = false)
    private Integer numOfAdult;

    @NotNull(message = "Please enter number of children")
    @Column(nullable = false)
    private Integer numOfChildren;

    @Size(max = 50, message = "Size is exceeded")
    @NotNull(message = "Please enter your full name")
    @Column(nullable = false, length = 50)
    private String contactNameSurname;

    @Pattern(regexp = "^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$",
            message = "Please enter valid phone number")
    @Size(min = 14, max= 14, message = "Phone number should be exact 10 characters")
    @NotNull(message = "Please enter your phone number")
    @Column(nullable = false, length = 14)
    private String contactPhone;

    @Email(message = "Please enter valid email")
    @NotNull(message = "Please enter your email")
    @Size(min = 5, max = 254)
    @Column(nullable = false, unique = true, length = 254)
    private String contactEmail;

    @Size(max = 500, message = "Size is exceeded")
    @Column(length = 500)
    private String note;

    private Boolean approved;

    private Boolean isPaid;
}
