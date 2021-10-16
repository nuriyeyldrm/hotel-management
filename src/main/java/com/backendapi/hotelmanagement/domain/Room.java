package com.backendapi.hotelmanagement.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Setter
@Getter
@ToString
@NoArgsConstructor
@Table(name = "rooms")
@Entity
public class Room implements Serializable {

    @Id
    @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_hotel", referencedColumnName = "name",
            insertable = false, updatable = false, nullable = false)
    @NotNull(message = "Please choose hotel")
    private Hotel idHotel;

    @Size(max = 15, message = "Size is exceeded")
    @NotNull(message = "Please enter room code")
    @Column(nullable = false, length = 15)
    private String code;

    @Size(max = 50, message = "Size is exceeded")
    @NotNull(message = "Please enter room name")
    @Column(nullable = false, length = 50)
    private String name;

    @Size(max = 50, message = "Size is exceeded")
    @NotNull(message = "Please enter room location")
    @Column(nullable = false, length = 50)
    private String location;

    @Size(max = 500, message = "Size is exceeded")
    @Column(length = 500)
    private String description;

    @NotNull(message = "Please enter room price")
    @Column(nullable = false)
    private Double price;

    @Size(max = 50, message = "Size is exceeded")
    @NotNull(message = "Please enter room type")
    @Column(nullable = false, length = 50)
    private String roomType;

    @NotNull(message = "Please enter number of adult")
    @Column(nullable = false)
    private Integer numOfAdult;

    @NotNull(message = "Please enter number of children")
    @Column(nullable = false)
    private Integer numOfChildren;

    @NotNull(message = "Room is available!")
    @Column(nullable = false)
    private Boolean isAvailable;

    public Room(Hotel idHotel, String code, String name, String location, String description, Double price,
                String roomType, Integer numOfAdult, Integer numOfChildren, Boolean isAvailable) {
        this.idHotel = idHotel;
        this.code = code;
        this.name = name;
        this.location = location;
        this.description = description;
        this.price = price;
        this.roomType = roomType;
        this.numOfAdult = numOfAdult;
        this.numOfChildren = numOfChildren;
        this.isAvailable = isAvailable;
    }
}
