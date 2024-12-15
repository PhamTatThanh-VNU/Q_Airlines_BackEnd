package com.example.qairlines.Services;

import com.example.qairlines.DTO.LocationDTO;
import com.example.qairlines.Model.Location;
import com.example.qairlines.Repository.LocationRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Data
public class LocationService {
    private final LocationRepository locationRepository;

    /**
     * This function for get all location of q_airlines
     * @return list of location data transfer
     */
    public List<LocationDTO> getAllLocation() {
        return locationRepository.findAllLocation();
    }

    /**
     * This function for add new Location
     * @param location is location you want to add
     * @return
     */
    public Location addLocation(Location location) {
        return locationRepository.save(location);
    }

    /**
     * The function to update existed location
     * @param id is id of location you need to update
     * @param location is new location for update
     * @return new location you just updated
     */
    public Location updateLocation(Long id, Location location) {
        Optional<Location> existingLocation = locationRepository.findById(id);
        if (existingLocation.isPresent()) {
            Location loc = existingLocation.get();
            loc.setLocationName(location.getLocationName());
            loc.setAirportName(location.getAirportName());
            loc.setCode(location.getCode());
            return locationRepository.save(loc);
        }
        throw new RuntimeException("Location not found with id " + id);
    }
    /**
     * This function for deleting location
     * @param id is id in database of location
     */
    public void deleteLocation(Long id) {
        if (locationRepository.existsById(id)) {
            locationRepository.deleteById(id);
        } else {
            throw new RuntimeException("Location not found with id " + id);
        }
    }

}
