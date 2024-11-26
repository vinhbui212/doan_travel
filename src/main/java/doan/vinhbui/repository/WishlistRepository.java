package doan.vinhbui.repository;

import doan.vinhbui.model.Customer;
import doan.vinhbui.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist,Long> {
    boolean existsByCustomerAndTour_Id(Customer customer, Long tourId);
    void deleteByCustomerAndTour_Id(Customer customer, Long tourId);
    List<Wishlist> findByCustomer_Id(Long customerId);

}
