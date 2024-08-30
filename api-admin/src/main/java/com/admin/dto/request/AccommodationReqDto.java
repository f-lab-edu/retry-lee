package com.admin.dto.request;

import com.admin.enums.room.BedType;
import com.admin.enums.room.RoomType;
import com.admin.enums.room.ViewType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
public class AccommodationReqDto {
    @NotBlank(message = "Accommodation name is required.")
    private String name;

    private String info;

    @NotBlank(message = "Country name is required.")
    private String countryName;

    @NotBlank(message = "State/Province is required.")
    private String state;

    @NotBlank(message = "City is required.")
    private String city;

    private String district;

    @NotBlank(message = "Street name is required.")
    private String street;

    @NotBlank(message = "Postal code is required.")
    private String postalCode;

    @Min(value = -90, message = "Latitude must be greater than or equal to -90.")
    @Max(value = 90, message = "Latitude must be less than or equal to 90.")
    private double latitude;

    @Min(value = -180, message = "Longitude must be greater than or equal to -180.")
    @Max(value = 180, message = "Longitude must be less than or equal to 180.")
    private double longitude;

    @NotEmpty(message = "At least one room information is required.")
    private List<RoomReqDto> rooms;

    @Getter
    @AllArgsConstructor
    public static class RoomReqDto {
        @NotBlank(message = "Room type is required.")
        private RoomType roomType;

        @NotBlank(message = "View type is required.")
        private ViewType viewType;

        @NotBlank(message = "Bed type is required.")
        private BedType bedType;

        @Min(value = 1, message = "Area must be at least 1.0 square meters.")
        private double squareMeter;

        @Min(value = 1, message = "Capacity must be at least 1 person.")
        private int capacity;

        @NotNull(message = "Price is required.")
        @DecimalMin(value = "0.01", message = "Price must be at least 0.01.")
        @Digits(integer = 8, fraction = 2, message = "Price can have up to 2 decimal places.")
        private BigDecimal price;

        @PositiveOrZero(message = "Stock must be zero or positive.")
        private int stock;
    }
}