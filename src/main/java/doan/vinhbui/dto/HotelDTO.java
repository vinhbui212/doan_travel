package doan.vinhbui.dto;

import doan.vinhbui.model.Customer;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelDTO {
    private double pricePerNight;
    private String name;
    private String address;
    private Long customerId;
    private String status;
}
