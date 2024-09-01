package com.admin.service;

import com.admin.dto.request.AccommodationReqDto;
import com.admin.dto.request.AccommodationReqDto.RoomReqDto;
import com.storage.entity.Accommodation;
import com.storage.entity.Room;
import com.storage.repository.AccommodationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccommodationService {
    private final AccommodationRepository accommodationRepository;

    @Transactional
    public void registerAccommodation(AccommodationReqDto req) {

        List<Room> rooms = new ArrayList<>();
        for (RoomReqDto roomReq : req.getRooms()) {
            Room room = Room.builder()
                    .roomType(roomReq.getRoomType().name())
                    .viewType(roomReq.getViewType().name())
                    .bedType(roomReq.getBedType().name())
                    .squareMeter(roomReq.getSquareMeter())
                    .capacity(roomReq.getCapacity())
                    .price(roomReq.getPrice())
                    .stock(roomReq.getStock())
                    .build();
            rooms.add(room);
        }

        Accommodation accommodation = Accommodation.builder()
                .name(req.getName())
                .info(req.getInfo())
                .countryName(req.getCountryName())
                .state(req.getState())
                .city(req.getCity())
                .district(req.getDistrict())
                .street(req.getStreet())
                .postalCode(req.getPostalCode())
                .latitude(req.getLatitude())
                .longitude(req.getLongitude())
                .rooms(rooms)  // 미리 생성한 Room 리스트를 설정
                .build();

        accommodationRepository.save(accommodation);
    }
}
