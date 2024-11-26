package doan.vinhbui.repository;

import doan.vinhbui.model.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking,Long> {
    @Query("SELECT b FROM Booking b WHERE b.customer.id = :customerId")
    Page<Booking> findByCustomerId(Long customerId,Pageable pageable);
    // Admin có thể lấy tất cả các booking, bao gồm cả đã hủy
    Page<Booking> findAll(Pageable pageable);
    boolean existsByCustomerIdAndTourId(Long customerId, Long tourId);

}
