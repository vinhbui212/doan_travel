package doan.vinhbui.service;

import doan.vinhbui.exception.DataNotFoundException;
import doan.vinhbui.model.Location;

public interface LocationService {
    Location createLocation(Location location);
    Location updateLocation(Location location,long lo_id) throws DataNotFoundException;
    String deleteLocation(long lo_id);

}
