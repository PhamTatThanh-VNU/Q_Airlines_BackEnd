package com.example.qairlines.Services;

import com.example.qairlines.DTO.LocationDTO;
import com.example.qairlines.Model.Location;
import com.example.qairlines.Repository.LocationRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
public class LocationService {
    private final LocationRepository locationRepository;

    public List<LocationDTO> getAllLocation() {
        return locationRepository.findAllLocation();
    }
}
