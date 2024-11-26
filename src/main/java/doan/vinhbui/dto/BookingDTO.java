package doan.vinhbui.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingDTO {
    private Long customerId;
    private String customerName;
    private LocalDate bookingDate;
    private int numOfPeople;
    private int numOfChildren;
    private String status;
    private Long tourId;
    private String tourName;
    private LocalDate travelDate;



}
