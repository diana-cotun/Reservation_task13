package ro.sda.java64.task13.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ro.sda.java64.task13.entities.Reservation;
import ro.sda.java64.task13.repositories.ReservationRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
//@SpringBootTest

class ReservationServiceTest {

    @TestConfiguration
    static class ReservationServiceTestConfig{
        @Bean
        ReservationService reservationService(ReservationRepository reservationRepository){
            return new ReservationService(reservationRepository);
        }
    }

    @Autowired
    ReservationService reservationService;

    @MockBean
    ReservationRepository reservationRepository;

    @Test
    void whenAddReservationThenReceiveTheSameReservation() {
        Reservation reservation=new Reservation();
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
       Reservation actual= reservationService.addReservation(reservation);

       assertEquals(reservation,actual,"We received the same reservation");
    }
    @Test
    void when_fetch_by_existing_id_then_receive_the_object(){
        //Given
        Long id=1l;
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
        verify(reservationRepository,times(1)).findById(any());

        //Verify actual vs expected
        assertEquals(expectedReservation,actualReservation,"Value as Expected");


    }

    @Test
    void when_fetch_by_non_existing_then_receive_exception(){

    }
}