package doan.vinhbui.repository;

import doan.vinhbui.model.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TourRepository extends JpaRepository<Tour,Long> {
    @Query("SELECT t FROM Tour t " +
            "WHERE (:departureLocation IS NULL OR t.departure = :departureLocation) " +
            "AND (:destination IS NULL OR t.destination = :destination) " +
            "AND (:startDate IS NULL OR t.startDate = :startDate) " +
            "AND (:maxPrice IS NULL OR t.price <= :maxPrice)")
    List<Tour> searchTours(
            @Param("departureLocation") String departureLocation,
            @Param("destination") String destination,
            @Param("startDate") LocalDate startDate,
            @Param("maxPrice") Double maxPrice);

}
