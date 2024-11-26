package doan.vinhbui.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "hotel",uniqueConstraints = @UniqueConstraint(columnNames = "hotel_name"))
@Data
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "price_per_night")
    private double pricePerNight;
    private String currency;
    private String address;
    @Column(name = "telephone_no")
    private String telephoneNo;
    @Column(name = "contact_mail")
    private String contactMail;
    @Column(name = "hotel_name")
    private String name;
    private String status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnore
    private Customer customer;
    @Column(name = "booking_date")
    private LocalDate bookingDate;
}
