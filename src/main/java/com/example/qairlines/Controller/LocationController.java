    package com.example.qairlines.Controller;

    import com.example.qairlines.DTO.LocationDTO;
    import com.example.qairlines.Model.Location;
    import com.example.qairlines.Services.LocationService;
    import lombok.RequiredArgsConstructor;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @RequestMapping("/locations")
    @RequiredArgsConstructor
    public class LocationController {
        private final LocationService locationService;

        /**
         * Route to get all location
         * @return list of location data transfer
         */
        @GetMapping("/all")
        public ResponseEntity<List<LocationDTO>> getAllLocation() {
            try {
                List<LocationDTO> locations = locationService.getAllLocation();
                if (locations.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(locations);
                }
                return ResponseEntity.status(HttpStatus.OK).body(locations);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }

        /**
         * @param location is body of location include locationName, airportName and code
         * @return new Location
         */
        @PostMapping("/add")
        public ResponseEntity<Location> addLocation(@RequestBody Location location) {
            return ResponseEntity.ok(locationService.addLocation(location));
        }

        /**
         * Route to update existed location
         * @param id of location
         * @param location is new location
         * @return location just updated
         */
        @PutMapping("/update/{id}")
        public ResponseEntity<Location> updateLocation(
                @PathVariable Long id,
                @RequestBody Location location) {
            return ResponseEntity.ok(locationService.updateLocation(id, location));
        }

        /**
         * Route to delete location by id
         * @param id is id of location
         * @return no return anything, just delete
         */
        @DeleteMapping("/delete/{id}")
        public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
            locationService.deleteLocation(id);
            return ResponseEntity.noContent().build();
        }
    }
