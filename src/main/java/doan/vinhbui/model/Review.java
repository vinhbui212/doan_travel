package doan.vinhbui.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(CustomerTourPK.class)
public class Review {
    @Id
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    @Id
    @ManyToOne
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;
    @Column(nullable = false)
    private float rating;
    @Column(nullable = false)
    private String comment;
    @Column(nullable = false)
    private LocalDateTime date;
    private boolean hasReviewed;
}
