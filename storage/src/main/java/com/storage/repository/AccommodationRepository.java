package com.storage.repository;

import com.storage.entity.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {

    @Query("SELECT a FROM Accommodation a WHERE " +
            "a.latitude >= :minLat AND a.latitude <= :maxLat AND " +
            "a.longitude >= :minLon AND a.longitude <= :maxLon")
    List<Accommodation> findNearbyAccommodations(BigDecimal minLat, BigDecimal maxLat, BigDecimal minLon, BigDecimal maxLon);
}
