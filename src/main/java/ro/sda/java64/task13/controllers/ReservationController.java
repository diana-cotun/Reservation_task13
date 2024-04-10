package ro.sda.java64.task13.controllers;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.sda.java64.task13.entities.Reservation;
import ro.sda.java64.task13.entities.Standard;
import ro.sda.java64.task13.errors.NoResourceFound;
import ro.sda.java64.task13.services.ReservationService;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public Reservation createReservation(@RequestBody Reservation reservation){
        return reservationService.addReservation(reservation);
    }

    @PutMapping
    public Reservation updateReservation(@RequestBody Reservation reservation) {
        return reservationService.updateReservation(reservation, reservation.getId());
    }

    @GetMapping("/list")
    public List<Reservation> getAllReservation() {
        return reservationService.allReservations();
    }

    @GetMapping("/{name}")
    public List<Reservation> getReservationByName(@PathVariable String name) {
        return reservationService.allReservationsByName(name);
    }

//     ResponseEntity<Reservation> SAU Reservation ???
//    @Repository, @Transactional cand le folosim

//    @DeleteMapping
//    public HttpStatus deleteReservationById(@RequestParam Long id) {
//        reservationService.deleteById(id);
//        return HttpStatus.NO_CONTENT;
//    }

    @DeleteMapping
    public ResponseEntity<Object> deleteReservationById(@RequestParam Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

//    @DeleteMapping("/{id}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void deleteReservationById(@PathVariable Long id) {
//        reservationService.deleteById(id);
//    }

    @GetMapping("/date")
    public List<Reservation> getReservationsByDate(@RequestParam @DateTimeFormat(iso= DateTimeFormat.ISO.DATE) LocalDate localDate){
        return reservationService.getActiveReservationsOnDate(localDate);
    }

    @GetMapping("/standard&price")
    public List<Reservation> getReservationsByStandardAndPriceGreaterThat(@RequestParam Standard standard, @RequestParam Integer price) {
        return reservationService.getReservationsByStandardAndPriceGreaterThan(standard, price);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    ResponseEntity<Object> handleIllegalRequests(IllegalArgumentException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = NoResourceFound.class)
    ResponseEntity<Object> handleIllegalRequests(NoResourceFound ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

}
