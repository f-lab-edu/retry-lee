package com.admin.unitTest.controller;

import com.admin.controller.AccommodationController;
import com.admin.dto.request.AccommodationReqDto;
import com.admin.dto.request.AccommodationReqDto.RoomReqDto;
import com.admin.enums.room.BedType;
import com.admin.enums.room.RoomType;
import com.admin.enums.room.ViewType;
import com.admin.service.AccommodationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccommodationController.class)
@Tag("UnitTest")
public class AccommodationControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccommodationService accommodationService;

    @Autowired
    private ObjectMapper objectMapper;

    private AccommodationReqDto reqDto;

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

        reqDto = new AccommodationReqDto(
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
    @DisplayName("success to register")
    public void successToRegister() throws Exception {
        doNothing().when(accommodationService).registerAccommodation(any(AccommodationReqDto.class));

        mockMvc.perform(post("/accommodation/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqDto)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Fails to register due to invalid name")
    public void failsToRegisterWithInvalidName() throws Exception {
        AccommodationReqDto invalidDto = new AccommodationReqDto(
                "",
                reqDto.getInfo(),
                reqDto.getCountryName(),
                reqDto.getState(),
                reqDto.getCity(),
                reqDto.getDistrict(),
                reqDto.getStreet(),
                reqDto.getPostalCode(),
                reqDto.getLatitude(),
                reqDto.getLongitude(),
                reqDto.getRooms()
        );

        mockMvc.perform(post("/accommodation/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Accommodation name is required."));
    }

    @Test
    @DisplayName("Fails to register due to invalid country")
    public void failsToRegisterWithInvalidCountry() throws Exception {
        AccommodationReqDto invalidDto = new AccommodationReqDto(
                reqDto.getName(),
                reqDto.getInfo(),
                "",
                reqDto.getState(),
                reqDto.getCity(),
                reqDto.getDistrict(),
                reqDto.getStreet(),
                reqDto.getPostalCode(),
                reqDto.getLatitude(),
                reqDto.getLongitude(),
                reqDto.getRooms()
        );

        mockMvc.perform(post("/accommodation/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Country name is required."));
    }

    @Test
    @DisplayName("Fails to register due to invalid state")
    public void failsToRegisterWithInvalidState() throws Exception {
        AccommodationReqDto invalidDto = new AccommodationReqDto(
                reqDto.getName(),
                reqDto.getInfo(),
                reqDto.getCountryName(),
                "",
                reqDto.getCity(),
                reqDto.getDistrict(),
                reqDto.getStreet(),
                reqDto.getPostalCode(),
                reqDto.getLatitude(),
                reqDto.getLongitude(),
                reqDto.getRooms()
        );

        mockMvc.perform(post("/accommodation/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("State/Province is required."));
    }

    @Test
    @DisplayName("Fails to register due to invalid city")
    public void failsToRegisterWithInvalidCity() throws Exception {
        AccommodationReqDto invalidDto = new AccommodationReqDto(
                reqDto.getName(),
                reqDto.getInfo(),
                reqDto.getCountryName(),
                reqDto.getState(),
                "",
                reqDto.getDistrict(),
                reqDto.getStreet(),
                reqDto.getPostalCode(),
                reqDto.getLatitude(),
                reqDto.getLongitude(),
                reqDto.getRooms()
        );

        mockMvc.perform(post("/accommodation/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("City is required."));
    }

    @Test
    @DisplayName("Fails to register due to invalid street")
    public void failsToRegisterWithInvalidStreet() throws Exception {
        AccommodationReqDto invalidDto = new AccommodationReqDto(
                reqDto.getName(),
                reqDto.getInfo(),
                reqDto.getCountryName(),
                reqDto.getState(),
                reqDto.getCity(),
                reqDto.getDistrict(),
                "",
                reqDto.getPostalCode(),
                reqDto.getLatitude(),
                reqDto.getLongitude(),
                reqDto.getRooms()
        );

        mockMvc.perform(post("/accommodation/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Street name is required."));
    }

    @Test
    @DisplayName("Fails to register due to invalid postal code")
    public void failsToRegisterWithInvalidPostalCode() throws Exception {
        AccommodationReqDto invalidDto = new AccommodationReqDto(
                reqDto.getName(),
                reqDto.getInfo(),
                reqDto.getCountryName(),
                reqDto.getState(),
                reqDto.getCity(),
                reqDto.getDistrict(),
                reqDto.getStreet(),
                "",
                reqDto.getLatitude(),
                reqDto.getLongitude(),
                reqDto.getRooms()
        );

        mockMvc.perform(post("/accommodation/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Postal code is required."));
    }

    @Test
    @DisplayName("Fails to register due to invalid latitude")
    public void failsToRegisterWithInvalidLatitude() throws Exception {
        AccommodationReqDto invalidDto = new AccommodationReqDto(
                reqDto.getName(),
                reqDto.getInfo(),
                reqDto.getCountryName(),
                reqDto.getState(),
                reqDto.getCity(),
                reqDto.getDistrict(),
                reqDto.getStreet(),
                reqDto.getPostalCode(),
                91.0,
                reqDto.getLongitude(),
                reqDto.getRooms()
        );

        mockMvc.perform(post("/accommodation/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Latitude must be less than or equal to 90."));
    }

    @Test
    @DisplayName("Fails to register due to invalid longitude")
    public void failsToRegisterWithInvalidLongitude() throws Exception {
        AccommodationReqDto invalidDto = new AccommodationReqDto(
                reqDto.getName(),
                reqDto.getInfo(),
                reqDto.getCountryName(),
                reqDto.getState(),
                reqDto.getCity(),
                reqDto.getDistrict(),
                reqDto.getStreet(),
                reqDto.getPostalCode(),
                reqDto.getLatitude(),
                181.0,
                reqDto.getRooms()
        );

        mockMvc.perform(post("/accommodation/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Longitude must be less than or equal to 180."));
    }

    @Test
    @DisplayName("Fails to register due to empty rooms")
    public void failsToRegisterWithEmptyRooms() throws Exception {
        AccommodationReqDto invalidDto = new AccommodationReqDto(
                reqDto.getName(),
                reqDto.getInfo(),
                reqDto.getCountryName(),
                reqDto.getState(),
                reqDto.getCity(),
                reqDto.getDistrict(),
                reqDto.getStreet(),
                reqDto.getPostalCode(),
                reqDto.getLatitude(),
                reqDto.getLongitude(),
                Collections.emptyList()
        );

        mockMvc.perform(post("/accommodation/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("At least one room information is required."));
    }

    @Test
    @DisplayName("Fails to register due to invalid room type")
    public void failsToRegisterWithInvalidRoomType() throws Exception {
        List<RoomReqDto> invalidRooms = new ArrayList<>(reqDto.getRooms());
        invalidRooms.set(0, new RoomReqDto(
                null,
                invalidRooms.getFirst().getViewType(),
                invalidRooms.getFirst().getBedType(),
                invalidRooms.getFirst().getSquareMeter(),
                invalidRooms.getFirst().getCapacity(),
                invalidRooms.getFirst().getPrice(),
                invalidRooms.getFirst().getStock()
        ));

        AccommodationReqDto invalidDto = new AccommodationReqDto(
                reqDto.getName(),
                reqDto.getInfo(),
                reqDto.getCountryName(),
                reqDto.getState(),
                reqDto.getCity(),
                reqDto.getDistrict(),
                reqDto.getStreet(),
                reqDto.getPostalCode(),
                reqDto.getLatitude(),
                reqDto.getLongitude(),
                invalidRooms
        );

        mockMvc.perform(post("/accommodation/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Room type is required."));
    }

    @Test
    @DisplayName("Fails to register due to invalid view type")
    public void failsToRegisterWithInvalidViewType() throws Exception {
        List<RoomReqDto> invalidRooms = new ArrayList<>(reqDto.getRooms());
        invalidRooms.set(0, new RoomReqDto(
                invalidRooms.getFirst().getRoomType(),
                null,
                invalidRooms.getFirst().getBedType(),
                invalidRooms.getFirst().getSquareMeter(),
                invalidRooms.getFirst().getCapacity(),
                invalidRooms.getFirst().getPrice(),
                invalidRooms.getFirst().getStock()
        ));

        AccommodationReqDto invalidDto = new AccommodationReqDto(
                reqDto.getName(),
                reqDto.getInfo(),
                reqDto.getCountryName(),
                reqDto.getState(),
                reqDto.getCity(),
                reqDto.getDistrict(),
                reqDto.getStreet(),
                reqDto.getPostalCode(),
                reqDto.getLatitude(),
                reqDto.getLongitude(),
                invalidRooms
        );

        mockMvc.perform(post("/accommodation/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("View type is required."));
    }

    @Test
    @DisplayName("Fails to register due to invalid bed type")
    public void failsToRegisterWithInvalidBedType() throws Exception {
        List<RoomReqDto> invalidRooms = new ArrayList<>(reqDto.getRooms());
        invalidRooms.set(0, new RoomReqDto(
                invalidRooms.getFirst().getRoomType(),
                invalidRooms.getFirst().getViewType(),
                null,
                invalidRooms.getFirst().getSquareMeter(),
                invalidRooms.getFirst().getCapacity(),
                invalidRooms.getFirst().getPrice(),
                invalidRooms.getFirst().getStock()
        ));

        AccommodationReqDto invalidDto = new AccommodationReqDto(
                reqDto.getName(),
                reqDto.getInfo(),
                reqDto.getCountryName(),
                reqDto.getState(),
                reqDto.getCity(),
                reqDto.getDistrict(),
                reqDto.getStreet(),
                reqDto.getPostalCode(),
                reqDto.getLatitude(),
                reqDto.getLongitude(),
                invalidRooms
        );

        mockMvc.perform(post("/accommodation/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Bed type is required."));
    }

    @Test
    @DisplayName("Fails to register due to invalid square meter")
    public void failsToRegisterWithInvalidSquareMeter() throws Exception {
        List<RoomReqDto> invalidRooms = new ArrayList<>(reqDto.getRooms());
        invalidRooms.set(0, new RoomReqDto(
                invalidRooms.getFirst().getRoomType(),
                invalidRooms.getFirst().getViewType(),
                invalidRooms.getFirst().getBedType(),
                0.5,
                invalidRooms.getFirst().getCapacity(),
                invalidRooms.getFirst().getPrice(),
                invalidRooms.getFirst().getStock()
        ));

        AccommodationReqDto invalidDto = new AccommodationReqDto(
                reqDto.getName(),
                reqDto.getInfo(),
                reqDto.getCountryName(),
                reqDto.getState(),
                reqDto.getCity(),
                reqDto.getDistrict(),
                reqDto.getStreet(),
                reqDto.getPostalCode(),
                reqDto.getLatitude(),
                reqDto.getLongitude(),
                invalidRooms
        );

        mockMvc.perform(post("/accommodation/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Area must be at least 1.0 square meters."));
    }

    @Test
    @DisplayName("Fails to register due to invalid capacity")
    public void failsToRegisterWithInvalidCapacity() throws Exception {
        List<RoomReqDto> invalidRooms = new ArrayList<>(reqDto.getRooms());
        invalidRooms.set(0, new RoomReqDto(
                invalidRooms.getFirst().getRoomType(),
                invalidRooms.getFirst().getViewType(),
                invalidRooms.getFirst().getBedType(),
                invalidRooms.getFirst().getSquareMeter(),
                0,
                invalidRooms.getFirst().getPrice(),
                invalidRooms.getFirst().getStock()
        ));

        AccommodationReqDto invalidDto = new AccommodationReqDto(
                reqDto.getName(),
                reqDto.getInfo(),
                reqDto.getCountryName(),
                reqDto.getState(),
                reqDto.getCity(),
                reqDto.getDistrict(),
                reqDto.getStreet(),
                reqDto.getPostalCode(),
                reqDto.getLatitude(),
                reqDto.getLongitude(),
                invalidRooms
        );

        mockMvc.perform(post("/accommodation/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Capacity must be at least 1 person."));
    }

    @Test
    @DisplayName("Fails to register due to invalid price")
    public void failsToRegisterWithInvalidPrice() throws Exception {
        List<RoomReqDto> invalidRooms = new ArrayList<>(reqDto.getRooms());
        invalidRooms.set(0, new RoomReqDto(
                invalidRooms.getFirst().getRoomType(),
                invalidRooms.getFirst().getViewType(),
                invalidRooms.getFirst().getBedType(),
                invalidRooms.getFirst().getSquareMeter(),
                invalidRooms.getFirst().getCapacity(),
                BigDecimal.ZERO,
                invalidRooms.getFirst().getStock()
        ));

        AccommodationReqDto invalidDto = new AccommodationReqDto(
                reqDto.getName(),
                reqDto.getInfo(),
                reqDto.getCountryName(),
                reqDto.getState(),
                reqDto.getCity(),
                reqDto.getDistrict(),
                reqDto.getStreet(),
                reqDto.getPostalCode(),
                reqDto.getLatitude(),
                reqDto.getLongitude(),
                invalidRooms
        );

        mockMvc.perform(post("/accommodation/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Price must be at least 0.01."));
    }

    @Test
    @DisplayName("Fails to register due to invalid stock")
    public void failsToRegisterWithInvalidStock() throws Exception {
        List<RoomReqDto> invalidRooms = new ArrayList<>(reqDto.getRooms());
        invalidRooms.set(0, new RoomReqDto(
                invalidRooms.getFirst().getRoomType(),
                invalidRooms.getFirst().getViewType(),
                invalidRooms.getFirst().getBedType(),
                invalidRooms.getFirst().getSquareMeter(),
                invalidRooms.getFirst().getCapacity(),
                invalidRooms.getFirst().getPrice(),
                -1
        ));

        AccommodationReqDto invalidDto = new AccommodationReqDto(
                reqDto.getName(),
                reqDto.getInfo(),
                reqDto.getCountryName(),
                reqDto.getState(),
                reqDto.getCity(),
                reqDto.getDistrict(),
                reqDto.getStreet(),
                reqDto.getPostalCode(),
                reqDto.getLatitude(),
                reqDto.getLongitude(),
                invalidRooms
        );

        mockMvc.perform(post("/accommodation/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Stock must be zero or positive."));
    }
}
