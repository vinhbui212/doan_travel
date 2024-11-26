package doan.vinhbui.repository;

import doan.vinhbui.model.Customer;
import doan.vinhbui.model.CustomerTourPK;
import doan.vinhbui.model.Review;
import doan.vinhbui.model.Tour;
import jakarta.persistence.IdClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, CustomerTourPK> {
    List<Review> findByTour(Tour tour);

    Review findByCustomerAndTour(Customer customer, Tour tour);
}
