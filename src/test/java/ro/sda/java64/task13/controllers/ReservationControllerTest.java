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
import ro.sda.java64.task13.entities.Standard;
import ro.sda.java64.task13.errors.NoResourceFound;
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
        Reservation expectedReservation = Reservation.builder()
                .id(id).name("ÄlaBala")
                .hotelName("Piatra arsa")
                .price(100).build();

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

    @Test
    void when_delete_by_missing_id_then_receive_not_found_status() throws Exception {
        Long id = 1L;
        when(reservationService.stergeDupaId(id)).thenThrow(NoResourceFound.class);

        mockMvc.perform(MockMvcRequestBuilders.delete("/reservation")
                .param("id",String.valueOf(id)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
        ;
        verify(reservationService,times(1)).stergeDupaId(any());

    }

    @Test
    void when_create_reservation_receive_id_forReservation() throws Exception {

        Long id = 1L;
        Reservation sentReservation = Reservation.builder().name("AlaBala").hotelName("Piatra arsa").price(100).build();
        Reservation expectedReservation = Reservation.builder().id(id).name("AlaBala").hotelName("Piatra arsa").price(100).build();
        //mock
        when(reservationService.addReservation(sentReservation)).thenReturn(expectedReservation);
        String requestBody=objectMapper.writeValueAsString(sentReservation);

        String expectedResult = objectMapper.writeValueAsString(expectedReservation);

        mockMvc.perform(MockMvcRequestBuilders.post("/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().json(expectedResult))
        ;

        verify(reservationService,times(1)).addReservation(any());
    }
    @Test
    void when_update_reservation_by_Id_then_receive_updated_reservation() throws Exception {

        Reservation sentReservation = Reservation.builder()
                .id(1L)
                .name("Diana")
                .hotelName("Artemis")
                .numberOfPeople(2)
                .standard(Standard.NORMAL)
                .startDate(LocalDate.ofEpochDay(2024-4-16))
                .endDate(LocalDate.ofEpochDay(2024-4-30))
                .price(200)
                .build();
        Reservation updatedReservation = Reservation.builder()
                .numberOfPeople(4)
                .price(500)
                .build();

        when(reservationService.updateReservation(sentReservation, sentReservation.getId())).thenReturn(updatedReservation);

        String requestBody = objectMapper.writeValueAsString(sentReservation);
        String receiveReservation = objectMapper.writeValueAsString(updatedReservation);


        mockMvc.perform(MockMvcRequestBuilders.put("/reservation")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(receiveReservation));

//        verify(reservationService, times(1)).updateReservation(any(), anyLong());
    }

    @Test
    void when_fetch_reservations_by_name_than_receive_list_reservations() throws Exception {
        Reservation existingReservation = Reservation.builder()
                .name("Diana")
                .hotelName("Artemis")
                .numberOfPeople(2)
                .build();

        when(reservationService.allReservationsByName(existingReservation.getName())).thenReturn(List.of(existingReservation));

        String receiveReservation = objectMapper.writeValueAsString(List.of(existingReservation));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/reservation/name/Diana"))

                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(receiveReservation));

        verify(reservationService, times(1)).allReservationsByName(anyString());
    }

}