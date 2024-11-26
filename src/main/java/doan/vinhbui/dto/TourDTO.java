package doan.vinhbui.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class TourDTO {

    private String title;
    private String description;
    private String priceCurrency;
    private String startDate;
    private String endDate;
    private boolean isAbroad;
    private String schedule;
    private String imgUrl;
    private double price_aldults;
    private double price_children;
    private String departure;
    private String destination;
}
