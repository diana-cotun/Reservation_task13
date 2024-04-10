package ro.sda.java64.task13.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Reservation {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String hotelName;
    private Integer numberOfPeople;
    private Standard standard;
    private Integer price;
    private LocalDate startDate;
    private LocalDate endDate;

}
