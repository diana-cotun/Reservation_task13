package ro.sda.java64.task13.services;

import ch.qos.logback.core.model.INamedModel;
import org.springframework.stereotype.Service;
import ro.sda.java64.task13.entities.Reservation;
import ro.sda.java64.task13.entities.Standard;
import ro.sda.java64.task13.errors.NoResourceFound;
import ro.sda.java64.task13.repositories.ReservationRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Reservation addReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public Reservation updateReservation(Reservation reservation, Long id) {
        Reservation entity = getById(id);
        entity.setName(reservation.getName());
        entity.setPrice(reservation.getPrice());
        entity.setStandard(reservation.getStandard());
        entity.setHotelName(reservation.getHotelName());
        entity.setNumberOfPeople(reservation.getNumberOfPeople());
        entity.setEndDate(reservation.getEndDate());
        entity.setStartDate(reservation.getStartDate());
        return reservationRepository.save(entity);
    }

    public List<Reservation> allReservations() {
        return reservationRepository.findAll();
    }

    public List<Reservation> allReservationsByName(String name) {
        return reservationRepository.findAllByName(name);
    }

    public void deleteById(Long id) {
        Reservation entityToDelete = getById(id);
        reservationRepository.delete(entityToDelete);
    }

    public Reservation getById(Long id) {
        return reservationRepository.findById(id).orElseThrow(() -> new NoResourceFound("Entity with id " + id + " doesn't exist"));
    }

    public List<Reservation> getActiveReservationsOnDate(LocalDate localDate) {
        return reservationRepository.findAllByStartDateLessThanEqualAndEndDateGreaterThanEqual(localDate,localDate);
    }

    public List<Reservation> getReservationsByStandardAndPriceGreaterThan(Standard standard, Integer price) {
        return reservationRepository.findAllByStandardAndPriceGreaterThan(standard, price);
    }
}
