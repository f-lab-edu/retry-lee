package com.admin.unitTest.service;

import com.admin.dto.request.AccommodationReqDto;
import com.admin.dto.request.AccommodationReqDto.RoomReqDto;
import com.admin.enums.room.BedType;
import com.admin.enums.room.RoomType;
import com.admin.enums.room.ViewType;
import com.admin.service.AccommodationService;
import com.storage.entity.Accommodation;
import com.storage.entity.Room;
import com.storage.repository.AccommodationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Tag("UnitTest")
public class AccommodationServiceUnitTest {

    @Mock
    private AccommodationRepository accommodationRepository;
    @InjectMocks
    private AccommodationService accommodationService;

    private AccommodationReqDto accommodationReqDto;
    @BeforeEach
    void setUp() {
        List<RoomReqDto> rooms = new ArrayList<>();
        rooms.add(new RoomReqDto(
                RoomType.SINGLE,
                ViewType.CITY,
                BedType.SINGLE,
                20.0,
                2,
                BigDecimal.valueOf(100.00),
                5
        ));

        accommodationReqDto = new AccommodationReqDto(
                "Test Hotel",
                "A great hotel",
                "country",
                "state",
                "city",
                "district",
                "street",
                "12345",
                40.7128,
                -74.0060,
                rooms
        );
    }

    @Test
    void successToRegister() {

        when(accommodationRepository.save(any(Accommodation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        accommodationService.registerAccommodation(accommodationReqDto);

        verify(accommodationRepository, times(1)).save(argThat(accommodation -> {
            assertEquals("Test Hotel", accommodation.getName());
            assertEquals("A great hotel", accommodation.getInfo());
            assertEquals("country", accommodation.getCountryName());
            assertEquals("state", accommodation.getState());
            assertEquals("city", accommodation.getCity());
            assertEquals("district", accommodation.getDistrict());
            assertEquals("street", accommodation.getStreet());
            assertEquals("12345", accommodation.getPostalCode());
            assertEquals(40.7128, accommodation.getLatitude());
            assertEquals(-74.0060, accommodation.getLongitude());

            List<Room> rooms = accommodation.getRooms();
            assertNotNull(rooms);
            assertEquals(1, rooms.size());
            Room room = rooms.getFirst();
            assertNotNull(room);
            assertEquals("SINGLE", room.getRoomType());
            assertEquals("CITY", room.getViewType());
            assertEquals("SINGLE", room.getBedType());
            assertEquals(20.0, room.getSquareMeter());
            assertEquals(2, room.getCapacity());
            assertEquals(0, BigDecimal.valueOf(100.00).compareTo(room.getPrice()));
            assertEquals(5, room.getStock());

            return true;
        }));
    }
}
