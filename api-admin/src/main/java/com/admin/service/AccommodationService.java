package com.admin.service;

import com.admin.dto.request.AccommodationReqDto;
import com.admin.dto.request.AccommodationReqDto.RoomReqDto;
import com.admin.dto.response.AccommodationResDto;
import com.admin.dto.response.AccommodationResDto.RoomResDto;
import com.admin.exception.CustomException;
import com.admin.exception.type.ErrorCode;
import com.storage.entity.Accommodation;
import com.storage.entity.Room;
import com.storage.repository.AccommodationRepository;
import com.storage.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccommodationService {
    private final AccommodationRepository accommodationRepository;
    private final RoomRepository roomRepository;

    @Transactional
    public AccommodationResDto registerAccommodation(AccommodationReqDto req) {

        BigDecimal latitude = req.getLatitude();
        BigDecimal longitude = req.getLongitude();
        BigDecimal distance = new BigDecimal("0.001"); // 약 100m 반경

        BigDecimal minLat = latitude.subtract(distance);
        BigDecimal maxLat = latitude.add(distance);
        BigDecimal minLon = longitude.subtract(distance);
        BigDecimal maxLon = longitude.add(distance);

        List<Accommodation> nearbyAccommodations = accommodationRepository.findNearbyAccommodations(
                minLat, maxLat, minLon, maxLon
        );

        if(!nearbyAccommodations.isEmpty()) {
            throw new CustomException(ErrorCode.ERROR_BE1001);
        }

        // 1. Accommodation 엔티티로 변환
        Accommodation accommodation = Accommodation.builder()
                .nameEn(req.getNameEn())
                .info(req.getInfo())
                .country(req.getCountry())
                .state(req.getState())
                .city(req.getCity())
                .district(req.getDistrict())
                .street(req.getStreet())
                .postalCode(req.getPostalCode())
                .latitude(req.getLatitude())
                .longitude(req.getLongitude())
                .build();

        // 2. Accommodation 저장
        Accommodation savedAccommodation = accommodationRepository.save(accommodation);

        // 3. Room 엔티티 생성 및 저장
        List<Room> rooms = new ArrayList<>();
        for (RoomReqDto roomReq : req.getRooms()) {
            Room room = Room.builder()
                    .accommodation(savedAccommodation)
                    .roomType(roomReq.getRoomType())
                    .viewType(roomReq.getViewType())
                    .bedType(roomReq.getBedType())
                    .squareMeter(roomReq.getSquareMeter())
                    .capacity(roomReq.getCapacity())
                    .price(roomReq.getPrice())
                    .stock(roomReq.getStock())
                    .build();
            rooms.add(room);
        }

        List<Room> savedRooms = roomRepository.saveAll(rooms);

        // 4. AccommodationRegisterResDto 생성 및 반환
        List<RoomResDto> roomResList = new ArrayList<>();
        for (Room savedRoom : savedRooms) {
            RoomResDto roomRes = RoomResDto.builder()
                    .roomType(savedRoom.getRoomType())
                    .price(savedRoom.getPrice())
                    .stock(savedRoom.getStock())
                    .squareMeter(savedRoom.getSquareMeter())
                            .build();
            roomResList.add(roomRes);
        }
        return AccommodationResDto.builder()
                .nameEn(savedAccommodation.getNameEn())
                .country(savedAccommodation.getCountry())
                .state(savedAccommodation.getState())
                .city(savedAccommodation.getCity())
                .latitude(savedAccommodation.getLatitude())
                .longitude(savedAccommodation.getLongitude())
                .rooms(roomResList)
                .build();
    }
}
