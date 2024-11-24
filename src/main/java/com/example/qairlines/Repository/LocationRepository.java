package com.example.qairlines.Repository;

import com.example.qairlines.DTO.LocationDTO;
import com.example.qairlines.Model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    @Query("SELECT new com.example.qairlines.DTO.LocationDTO(l.locationName,l.airportName,l.code)  from Location l")
    List<LocationDTO> findAllLocation();
}
