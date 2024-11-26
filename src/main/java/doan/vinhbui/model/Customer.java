package doan.vinhbui.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "customer")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Customer extends User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "address")
    private String address;
    @Column(name = "card_no")
    private String cardNo;
    @OneToMany(mappedBy = "customer")
    private List<Booking> bookings;
    @OneToMany(mappedBy = "customer")
    private List<Hotel> hotels;
    @OneToMany(mappedBy = "customer")
    private List<Flight> flights;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Wishlist> wishlist ;

    public Customer(String email, String password, Boolean isGmail, Boolean isVerified, String firstName, String lastName) {
        this.setEmail(email);
        this.setPassword(password);
        this.setIsGmail(isGmail);
        this.setIsVerified(isVerified);
        this.setFirstName(firstName);
        this.setLastName(lastName);
    }

}
