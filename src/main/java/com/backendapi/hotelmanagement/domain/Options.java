package com.backendapi.hotelmanagement.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "options")
public class Options {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="has_gps")
    private String hasGPS;

    private String transmission;
}
