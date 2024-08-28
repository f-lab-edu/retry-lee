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
    @NotBlank(message = "숙소 이름은 필수입니다.")
    private String name;

    private String info;

    @NotBlank(message = "국가명은 필수입니다.")
    private String countryName;

    @NotBlank(message = "국가코드는 필수 입니다.")
    private String countryCode;
    @NotBlank(message = "주/도는 필수입니다.")
    private String state;

    @NotBlank(message = "도시는 필수입니다.")
    private String city;

    private String district;

    @NotBlank(message = "도로명은 필수입니다.")
    private String street;

    @NotBlank(message = "우편번호는 필수입니다.")
    private String postalCode;

    @NotNull(message = "위도는 필수입니다.")
    @DecimalMin(value = "-90.000000", message = "위도는 -90 이상이어야 합니다.")
    @DecimalMax(value = "90.000000", message = "위도는 90 이하여야 합니다.")
    private BigDecimal latitude;

    @NotNull(message = "경도는 필수입니다.")
    @DecimalMin(value = "-180.000000", message = "경도는 -180 이상이어야 합니다.")
    @DecimalMax(value = "180.000000", message = "경도는 180 이하여야 합니다.")
    private BigDecimal longitude;

    @NotEmpty(message = "최소 하나의 방 정보가 필요합니다.")
    private List<RoomReqDto> rooms;

    @Getter
    @AllArgsConstructor
    public static class RoomReqDto {
        @NotBlank(message = "방 타입은 필수입니다.")
        private RoomType roomType;

        @NotBlank(message = "뷰 타입은 필수입니다.")
        private ViewType viewType;

        @NotBlank(message = "침대 타입은 필수입니다.")
        private BedType bedType;

        /**
         * 숙소 면적 표시 정책에 따라 수정 가능.
         */
        @NotNull(message = "면적은 필수입니다.")
        @Min(value = 1, message = "면적은 1.0㎡ 이상이어야 합니다.")
        private Double squareMeter;

        @NotNull(message = "수용 인원은 필수입니다.")
        @Min(value = 1, message = "수용 인원은 최소 1명 이상이어야 합니다.")
        private Integer capacity;

        /**
         * 가격 정책에 따라 소수점, 최대 가격 설정 가능
         */
        @NotNull(message = "가격은 필수입니다.")
        @DecimalMin(value = "0.01", message = "가격은 0.01 이상이어야 합니다.")
        @Digits(integer = 8, fraction = 2, message = "가격은 소수점 둘째자리까지 입력 가능합니다.")
        private BigDecimal price;

        @NotNull(message = "재고는 필수입니다.")
        @PositiveOrZero(message = "재고는 0 이상이어야 합니다.")
        private Integer stock;
    }
}
