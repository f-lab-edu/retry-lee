package com.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class AccommodationResDto {

    private String nameEn;
    private String country;
    private String state;
    private String city;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private List<RoomResDto> rooms;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class RoomResDto {
        private String roomType;
        private BigDecimal squareMeter;
        private BigDecimal price;
        private Integer stock;
    }
}
