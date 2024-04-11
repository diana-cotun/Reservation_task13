package ro.sda.java64.task13.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ro.sda.java64.task13.entities.Reservation;
import ro.sda.java64.task13.errors.NoResourceFound;
import ro.sda.java64.task13.repositories.ReservationRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
//@SpringBootTest

class ReservationServiceTest {

    @TestConfiguration
    static class ReservationServiceTestConfig {
        @Bean
        ReservationService reservationService(ReservationRepository reservationRepository) {
            return new ReservationService(reservationRepository);
        }
    }

    @Autowired
    ReservationService reservationService;

    @MockBean
    ReservationRepository reservationRepository;

    @Test
    void whenAddReservationThenReceiveTheSameReservation() {
        Reservation reservation = new Reservation();
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        Reservation actual = reservationService.addReservation(reservation);

        assertEquals(reservation, actual, "We received the same reservation");
    }

    @Test
    void when_fetch_by_existing_id_then_receive_the_object() {
        //Given
        Long id = 1L;
        Reservation expectedReservation = Reservation.builder()
                .id(id)
                .name("ÄlaBala")
                .hotelName("Piatra arsa")
                .price(100)
                .build();
        //Mocking
        when(reservationRepository.findById(id)).thenReturn(Optional.of(expectedReservation));

        //when
        Reservation actualReservation = reservationService.getById(id);


        //Verify call/method invocation times
        verify(reservationRepository, times(1)).findById(any());

        //Verify actual vs expected
        assertEquals(expectedReservation, actualReservation, "Value as Expected");
    }

    @Test
    void when_adding_new_reservation_with_same_name_throw_exception() {
        //Given
        Long id = 1L;
        Reservation expectedReservation = Reservation.builder()
                .id(id)
                .name("ÄlaBala")
                .hotelName("Piatra arsa")
                .price(100)
                .build();
        when(reservationRepository.findAllByName(anyString())).thenReturn(List.of(expectedReservation));

        assertThrows(RuntimeException.class, () -> {
            reservationService.addReservation(expectedReservation);
        });
        verify(reservationRepository, times(1)).findAllByName(any());
        verify(reservationRepository, times(0)).save(any());
    }

    @Test
    void when_fetch_by_non_existing_then_receive_exception() {
        when(reservationRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NoResourceFound.class, () -> {
            reservationService.getById(1L);
        });
    }

    @Test
    void when_deleting_by_non_existing_Id_throw_exception() {
        when(reservationRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NoResourceFound.class, () -> {
            reservationService.stergeDupaId(1L);
        });

        //Never called deleteById
        verify(reservationRepository, never()).deleteById(any());
    }

    @Test
    void when_deleting_by_an_existing_Id_then_delete_repo_should_be_called() {
        Long id = 1L;
        Reservation expectedReservation = Reservation.builder()
                .id(id)
                .name("ÄlaBala")
                .hotelName("Piatra arsa")
                .price(100)
                .build();

        when(reservationRepository.findById(any())).thenReturn(Optional.of(expectedReservation));

        Reservation actualReservation = reservationService.stergeDupaId(id);

        assertEquals(expectedReservation, actualReservation);
        verify(reservationRepository, times(1)).deleteById(any());

    }


}