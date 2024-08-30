package com.admin.service;

import com.admin.dto.request.AccommodationReqDto;
import com.admin.dto.request.AccommodationReqDto.RoomReqDto;
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
                .build();

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
