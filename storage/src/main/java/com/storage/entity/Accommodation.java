package com.storage.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Accommodation extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accommodationId;
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String info;

    @Column(nullable = false)
    private String countryName;

    @Column(nullable = false)
    private String countryCode;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String city;

    private String district;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String postalCode;

    @Column(nullable = false, precision = 8, scale = 6)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 9, scale = 6)
    private BigDecimal longitude;

    @OneToMany(mappedBy = "accommodation", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Room> rooms = new ArrayList<>();
}
