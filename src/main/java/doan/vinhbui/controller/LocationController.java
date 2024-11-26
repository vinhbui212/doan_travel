package doan.vinhbui.controller;

import doan.vinhbui.exception.DataNotFoundException;
import doan.vinhbui.model.Location;
import doan.vinhbui.service.LocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/location")
public class LocationController {
    private LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping
    public ResponseEntity<?> addLocation(@RequestBody Location location){
        Location location1=locationService.createLocation(location);
        return ResponseEntity.ok().body(location1);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateLocation(@RequestBody Location location,@PathVariable long id) throws DataNotFoundException {
        Location location1=locationService.updateLocation(location,id);
        return ResponseEntity.ok().body(location1);
    }

    @DeleteMapping("/{id}")
    public String deleteLocation( long id) throws DataNotFoundException {
            return locationService.deleteLocation(id);

    }
}
