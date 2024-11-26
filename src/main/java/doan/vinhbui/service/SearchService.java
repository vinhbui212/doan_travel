package doan.vinhbui.service;

import doan.vinhbui.model.Tour;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface SearchService  {
    List<Tour> searchTours(String departureLocation, String destination, LocalDate startDate, Double maxPrice);
}
