package doan.vinhbui.service.impl;

import doan.vinhbui.exception.DataNotFoundException;
import doan.vinhbui.model.Location;
import doan.vinhbui.repository.LocationRepository;
import doan.vinhbui.service.LocationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class LocationServiceImpl implements LocationService {
    private LocationRepository locationRepository;
    @Override
    public Location createLocation(Location location) {
        Location location1=new Location();
        location1.setName(location.getName());
        location1.setDescription(location.getDescription());
        return locationRepository.save(location1);
    }

    @Override
    @Transactional
    public Location updateLocation(Location location, long lo_id) throws DataNotFoundException {
        Location location1=locationRepository.findById(lo_id) .orElseThrow(() ->
                new DataNotFoundException(
                        "Cannot find location with id: "+location.getId()));
        location1.setName(location.getName());
        location1.setDescription(location.getDescription());
        return locationRepository.save(location1);
    }

    @Override
    public String deleteLocation(long lo_id) {
        Optional<Location> location=locationRepository.findById(lo_id);
        location.ifPresent(locationRepository::delete);
        return null;
    }
}
