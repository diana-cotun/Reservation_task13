package ro.sda.java64.task13.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.sda.java64.task13.entities.Reservation;
import ro.sda.java64.task13.entities.Standard;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllByName(String name);

    List<Reservation> findAllByStandardAndPriceGreaterThan(Standard standard, Integer price);

    List<Reservation> findAllByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate startDate, LocalDate endDate);


}
