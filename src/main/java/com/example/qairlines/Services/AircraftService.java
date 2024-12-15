package com.example.qairlines.Services;

import com.example.qairlines.DTO.AirCraftDTO;
import com.example.qairlines.Model.AirCraft;
import com.example.qairlines.Repository.AircraftRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AircraftService {
    @Autowired
    private AircraftRepository aircraftRepository;

    /**
     * @return
     */
    public List<AirCraftDTO> getAllAirCrafts() {
        return aircraftRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Add a new AirCraft
    public AirCraft addAirCraft(AirCraft airCraft) {
        return aircraftRepository.save(airCraft);
    }

    // Update an AirCraft
    public AirCraft updateAirCraft(Long id, AirCraft updatedAirCraft) {
        AirCraft existingAirCraft = aircraftRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aircraft not found with ID: " + id));

        existingAirCraft.setAircraftCode(updatedAirCraft.getAircraftCode());
        existingAirCraft.setManufacturer(updatedAirCraft.getManufacturer());
        existingAirCraft.setModel(updatedAirCraft.getModel());
        existingAirCraft.setSeatCapacity(updatedAirCraft.getSeatCapacity());
        return aircraftRepository.save(existingAirCraft);
    }

    // Delete an AirCraft
    public void deleteAirCraft(Long id) {
        if (!aircraftRepository.existsById(id)) {
            throw new EntityNotFoundException("Aircraft not found with ID: " + id);
        }
        aircraftRepository.deleteById(id);
    }

    /**
     * The function to convert aircraft to DTO
     *
     * @param airCraft is input of function , aircraft you want to convert
     * @return aircraft data transfer
     */
    private AirCraftDTO convertToDTO(AirCraft airCraft) {
        return new AirCraftDTO(
                airCraft.getId(),
                airCraft.getAircraftCode(),
                airCraft.getManufacturer(),
                airCraft.getModel(),
                airCraft.getSeatCapacity()
        );
    }
}
