package com.admin.service;

import com.admin.dto.request.AccommodationReqDto;
import com.admin.dto.request.AccommodationReqDto.RoomReqDto;
import com.admin.enums.country.Country;
import com.admin.exception.CustomException;
import com.admin.exception.type.ErrorCode;
import com.storage.entity.Accommodation;
import com.storage.entity.Room;
import com.storage.repository.AccommodationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccommodationService {
    private final AccommodationRepository accommodationRepository;

    @Transactional
    public void registerAccommodation(AccommodationReqDto req) {

        if (!Country.isValidCountry(req.getCountryCode(), req.getCountryName())) {
            throw new CustomException(ErrorCode.ERROR_BE1002);
        }

        // 1. Accommodation 엔티티로 변환
        Accommodation accommodation = Accommodation.builder()
                .name(req.getName())
                .info(req.getInfo())
                .countryCode(req.getCountryCode())
                .countryName(req.getCountryName())
                .state(req.getState())
                .city(req.getCity())
                .district(req.getDistrict())
                .street(req.getStreet())
                .postalCode(req.getPostalCode())
                .latitude(req.getLatitude())
                .longitude(req.getLongitude())
                .build();

        // 2. Room 엔티티 생성
        for (RoomReqDto roomReq : req.getRooms()) {
            Room room = Room.builder()
                    .accommodation(accommodation)
                    .roomType(roomReq.getRoomType().name())
                    .viewType(roomReq.getViewType().name())
                    .bedType(roomReq.getBedType().name())
                    .squareMeter(roomReq.getSquareMeter())
                    .capacity(roomReq.getCapacity())
                    .price(roomReq.getPrice())
                    .stock(roomReq.getStock())
                    .build();
            accommodation.getRooms().add(room);
        }

        accommodationRepository.save(accommodation);
    }
}
