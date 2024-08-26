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
    private Long hotelId;
    private String nameEn;
    private String info;
    private String country;
    private String state;
    private String city;
    private String district;
    private String street;
    private String postalCode;
    private BigDecimal latitude;
    private BigDecimal longitude;

    @OneToMany(mappedBy = "accommodation", cascade = CascadeType.ALL)
    private List<Room> rooms = new ArrayList<>();
}
