package ro.sda.java64.task13.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ro.sda.java64.task13.entities.Reservation;
import ro.sda.java64.task13.services.ReservationService;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ReservationService reservationService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void when_fetch_reservation_by_id_object_should_be_returned() throws Exception {
        Long id = 1L;
        Reservation expectedReservation = Reservation.builder().id(id).name("ÄlaBala").hotelName("Piatra arsa").price(100).build();

        String returnList = objectMapper.writeValueAsString(List.of(expectedReservation));

        when(reservationService.getActiveReservationsOnDate(any())).thenReturn(List.of(expectedReservation));
        mockMvc.perform(
                //Create the call
                MockMvcRequestBuilders.get("/reservation/date")
                        .param("localDate", String.valueOf(LocalDate.now())))
                //check the response
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(returnList));

        verify(reservationService,times(1)).getActiveReservationsOnDate(any());

        verify(reservationService,never()).getById(any());
    }


}