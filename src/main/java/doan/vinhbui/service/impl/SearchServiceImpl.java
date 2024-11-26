package doan.vinhbui.service.impl;

import doan.vinhbui.model.Tour;
import doan.vinhbui.repository.TourRepository;
import doan.vinhbui.service.SearchService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@RequiredArgsConstructor
@Service
public class SearchServiceImpl implements SearchService {
    private  final Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);

    private final TourRepository tourRepository;
    @Override
    public List<Tour> searchTours(String departureLocation, String destination, LocalDate startDate, Double maxPrice) {
        List<Tour> tours = tourRepository.searchTours(departureLocation, destination, startDate, maxPrice);
        if (tours.isEmpty()) {
            logger.info("No tours found for the given criteria");
        } else {
            logger.info("Found tours: {}", tours);
        }
        return tours;
    }
}
