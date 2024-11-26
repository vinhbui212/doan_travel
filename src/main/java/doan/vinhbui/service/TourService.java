package doan.vinhbui.service;

import doan.vinhbui.dto.TourDTO;
import doan.vinhbui.exception.DataNotFoundException;
import doan.vinhbui.model.Location;
import doan.vinhbui.model.Tour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TourService {
    ResponseEntity<String> createTour(TourDTO tourDTO, String authorizationHeader);
    Tour updateTour(TourDTO tourDTO,long tour_id) throws DataNotFoundException;
    String deleteTour(long tour_id);
    TourDTO getTourById(long tour_id);
    Page<TourDTO> findAllTour(Pageable pageable);
}
