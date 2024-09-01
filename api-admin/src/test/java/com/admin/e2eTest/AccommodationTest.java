package com.admin.e2eTest;

import com.admin.dto.request.AccommodationReqDto;
import com.admin.dto.request.AccommodationReqDto.RoomReqDto;
import com.admin.enums.room.BedType;
import com.admin.enums.room.RoomType;
import com.admin.enums.room.ViewType;
import com.storage.repository.AccommodationRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("E2eTest")
public class AccommodationTest extends BaseE2eTest{

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private AccommodationRepository accommodationRepository;

    @AfterEach
    public void cleanup() {
        accommodationRepository.deleteAll();
    }

    private AccommodationReqDto createValidAccommodationReqDto;

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

        createValidAccommodationReqDto = new AccommodationReqDto(
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
    public void successToRegister() {
        AccommodationReqDto reqDto = createValidAccommodationReqDto;
        ResponseEntity<Void> response = testRestTemplate.postForEntity("/accommodation/register", reqDto, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("fails to register due to invalid accommodation name")
    public void failsToRegisterWithInvalidName() {
        AccommodationReqDto reqDto = createValidAccommodationReqDto;
        reqDto = new AccommodationReqDto("", reqDto.getInfo(), reqDto.getCountryName(), reqDto.getState(),
                reqDto.getCity(), reqDto.getDistrict(), reqDto.getStreet(),
                reqDto.getPostalCode(), reqDto.getLatitude(), reqDto.getLongitude(),
                reqDto.getRooms());
        ResponseEntity<String> response = testRestTemplate.postForEntity("/accommodation/register", reqDto, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Accommodation name is required.");
    }

    @Test
    @DisplayName("fails to register due to invalid country name")
    public void failsToRegisterWithInvalidCountryName() {
        AccommodationReqDto reqDto = createValidAccommodationReqDto;
        reqDto = new AccommodationReqDto(reqDto.getName(), reqDto.getInfo(), "", reqDto.getState(),
                reqDto.getCity(), reqDto.getDistrict(), reqDto.getStreet(),
                reqDto.getPostalCode(), reqDto.getLatitude(), reqDto.getLongitude(),
                reqDto.getRooms());
        ResponseEntity<String> response = testRestTemplate.postForEntity("/accommodation/register", reqDto, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Country name is required.");
    }

    @Test
    @DisplayName("fails to register due to invalid state")
    public void failsToRegisterWithInvalidState() {
        AccommodationReqDto reqDto = createValidAccommodationReqDto;
        reqDto = new AccommodationReqDto(reqDto.getName(), reqDto.getInfo(), reqDto.getCountryName(), "",
                reqDto.getCity(), reqDto.getDistrict(), reqDto.getStreet(),
                reqDto.getPostalCode(), reqDto.getLatitude(), reqDto.getLongitude(),
                reqDto.getRooms());
        ResponseEntity<String> response = testRestTemplate.postForEntity("/accommodation/register", reqDto, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("State/Province is required.");
    }

    @Test
    @DisplayName("fails to register due to invalid city")
    public void failsToRegisterWithInvalidCity() {
        AccommodationReqDto reqDto = createValidAccommodationReqDto;
        reqDto = new AccommodationReqDto(reqDto.getName(), reqDto.getInfo(), reqDto.getCountryName(), reqDto.getState(),
                "", reqDto.getDistrict(), reqDto.getStreet(),
                reqDto.getPostalCode(), reqDto.getLatitude(), reqDto.getLongitude(),
                reqDto.getRooms());
        ResponseEntity<String> response = testRestTemplate.postForEntity("/accommodation/register", reqDto, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("City is required.");
    }

    @Test
    @DisplayName("fails to register due to invalid street")
    public void failsToRegisterWithInvalidStreet() {
        AccommodationReqDto reqDto = createValidAccommodationReqDto;
        reqDto = new AccommodationReqDto(reqDto.getName(), reqDto.getInfo(), reqDto.getCountryName(), reqDto.getState(),
                reqDto.getCity(), reqDto.getDistrict(), "",
                reqDto.getPostalCode(), reqDto.getLatitude(), reqDto.getLongitude(),
                reqDto.getRooms());
        ResponseEntity<String> response = testRestTemplate.postForEntity("/accommodation/register", reqDto, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Street name is required.");
    }

    @Test
    @DisplayName("fails to register due to invalid postal code")
    public void failsToRegisterWithInvalidPostalCode() {
        AccommodationReqDto reqDto = createValidAccommodationReqDto;
        reqDto = new AccommodationReqDto(reqDto.getName(), reqDto.getInfo(), reqDto.getCountryName(), reqDto.getState(),
                reqDto.getCity(), reqDto.getDistrict(), reqDto.getStreet(),
                "", reqDto.getLatitude(), reqDto.getLongitude(),
                reqDto.getRooms());
        ResponseEntity<String> response = testRestTemplate.postForEntity("/accommodation/register", reqDto, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Postal code is required.");
    }

    @Test
    @DisplayName("fails to register due to invalid latitude")
    public void failsToRegisterWithInvalidLatitude() {
        AccommodationReqDto reqDto = createValidAccommodationReqDto;
        reqDto = new AccommodationReqDto(reqDto.getName(), reqDto.getInfo(), reqDto.getCountryName(), reqDto.getState(),
                reqDto.getCity(), reqDto.getDistrict(), reqDto.getStreet(),
                reqDto.getPostalCode(), 91.0, reqDto.getLongitude(),
                reqDto.getRooms());
        ResponseEntity<String> response = testRestTemplate.postForEntity("/accommodation/register", reqDto, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Latitude must be less than or equal to 90.");
    }

    @Test
    @DisplayName("fails to register due to invalid longitude")
    public void failsToRegisterWithInvalidLongitude() {
        AccommodationReqDto reqDto = createValidAccommodationReqDto;
        reqDto = new AccommodationReqDto(reqDto.getName(), reqDto.getInfo(), reqDto.getCountryName(), reqDto.getState(),
                reqDto.getCity(), reqDto.getDistrict(), reqDto.getStreet(),
                reqDto.getPostalCode(), reqDto.getLatitude(), 181.0,
                reqDto.getRooms());
        ResponseEntity<String> response = testRestTemplate.postForEntity("/accommodation/register", reqDto, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Longitude must be less than or equal to 180.");
    }

    @Test
    @DisplayName("fails to register due to empty rooms")
    public void failsToRegisterWithEmptyRooms() {
        AccommodationReqDto reqDto = createValidAccommodationReqDto;
        List<RoomReqDto> rooms = new ArrayList<>();
        reqDto = new AccommodationReqDto(reqDto.getName(), reqDto.getInfo(), reqDto.getCountryName(), reqDto.getState(),
                reqDto.getCity(), reqDto.getDistrict(), reqDto.getStreet(),
                reqDto.getPostalCode(), reqDto.getLatitude(), reqDto.getLongitude(),
                rooms);
        ResponseEntity<String> response = testRestTemplate.postForEntity("/accommodation/register", reqDto, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("At least one room information is required.");
    }

    @Test
    @DisplayName("fails to register due to invalid square meter")
    public void failsToRegisterWithInvalidSquareMeter() {
        AccommodationReqDto reqDto = createValidAccommodationReqDto;
        List<RoomReqDto> rooms = new ArrayList<>();
        rooms.add(new AccommodationReqDto.RoomReqDto(RoomType.SINGLE, ViewType.CITY, BedType.SINGLE, 0.0, 2, BigDecimal.valueOf(100.00), 5));
        reqDto = new AccommodationReqDto(reqDto.getName(), reqDto.getInfo(), reqDto.getCountryName(), reqDto.getState(),
                reqDto.getCity(), reqDto.getDistrict(), reqDto.getStreet(),
                reqDto.getPostalCode(), reqDto.getLatitude(), reqDto.getLongitude(),
                rooms);
        ResponseEntity<String> response = testRestTemplate.postForEntity("/accommodation/register", reqDto, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Area must be at least 1.0 square meters.");
    }

    @Test
    @DisplayName("fails to register due to invalid capacity")
    public void failsToRegisterWithInvalidCapacity() {
        AccommodationReqDto reqDto = createValidAccommodationReqDto;
        List<RoomReqDto> rooms = new ArrayList<>();
        rooms.add(new AccommodationReqDto.RoomReqDto(RoomType.SINGLE, ViewType.CITY, BedType.SINGLE, 20.0, 0, BigDecimal.valueOf(100.00), 5));
        reqDto = new AccommodationReqDto(reqDto.getName(), reqDto.getInfo(), reqDto.getCountryName(), reqDto.getState(),
                reqDto.getCity(), reqDto.getDistrict(), reqDto.getStreet(),
                reqDto.getPostalCode(), reqDto.getLatitude(), reqDto.getLongitude(),
                rooms);
        ResponseEntity<String> response = testRestTemplate.postForEntity("/accommodation/register", reqDto, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Capacity must be at least 1 person.");
    }

    @Test
    @DisplayName("fails to register due to invalid price")
    public void failsToRegisterWithInvalidPrice() {
        AccommodationReqDto reqDto = createValidAccommodationReqDto;
        List<RoomReqDto> rooms = new ArrayList<>();
        rooms.add(new AccommodationReqDto.RoomReqDto(RoomType.SINGLE, ViewType.CITY, BedType.SINGLE, 20.0, 2, BigDecimal.valueOf(0.00), 5));
        reqDto = new AccommodationReqDto(reqDto.getName(), reqDto.getInfo(), reqDto.getCountryName(), reqDto.getState(),
                reqDto.getCity(), reqDto.getDistrict(), reqDto.getStreet(),
                reqDto.getPostalCode(), reqDto.getLatitude(), reqDto.getLongitude(),
                rooms);
        ResponseEntity<String> response = testRestTemplate.postForEntity("/accommodation/register", reqDto, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Price must be at least 0.01.");
    }

    @Test
    @DisplayName("fails to register due to invalid stock")
    public void failsToRegisterWithInvalidStock() {
        AccommodationReqDto reqDto = createValidAccommodationReqDto;
        List<RoomReqDto> rooms = new ArrayList<>();
        rooms.add(new AccommodationReqDto.RoomReqDto(RoomType.SINGLE, ViewType.CITY, BedType.SINGLE, 20.0, 2, BigDecimal.valueOf(100.00), -1));
        reqDto = new AccommodationReqDto(reqDto.getName(), reqDto.getInfo(), reqDto.getCountryName(), reqDto.getState(),
                reqDto.getCity(), reqDto.getDistrict(), reqDto.getStreet(),
                reqDto.getPostalCode(), reqDto.getLatitude(), reqDto.getLongitude(),
                rooms);
        ResponseEntity<String> response = testRestTemplate.postForEntity("/accommodation/register", reqDto, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Stock must be zero or positive.");
    }
}
