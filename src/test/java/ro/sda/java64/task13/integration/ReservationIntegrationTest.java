package ro.sda.java64.task13.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ro.sda.java64.task13.entities.Reservation;
import ro.sda.java64.task13.entities.Standard;
import ro.sda.java64.task13.repositories.ReservationRepository;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
public class ReservationIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void when_create_new_reservation_getNewValue() throws Exception {
        Reservation reservationObject = Reservation.builder()
//                .id(1L)
                .name("Diana")
                .hotelName("Artemis")
                .numberOfPeople(2)
                .standard(Standard.NORMAL)
                .startDate(LocalDate.ofEpochDay(2024-4-16))
                .endDate(LocalDate.ofEpochDay(2024-4-30))
                .price(200)
                .build();
        String requestBody = objectMapper.writeValueAsString(reservationObject);

        mockMvc.perform(MockMvcRequestBuilders.post("/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect( jsonPath("$.id").value("1"));
    }

    @Test
    void when_create_new_reservation_with_same_name_throw_exception() throws Exception {
        Reservation reservationObject = Reservation.builder()

                .name("Diana")
                .hotelName("Artemis")
                .numberOfPeople(2)
                .standard(Standard.NORMAL)
                .startDate(LocalDate.ofEpochDay(2024-4-16))
                .endDate(LocalDate.ofEpochDay(2024-4-30))
                .price(200)
                .build();

        //Adaug in baza de date
        reservationRepository.save(reservationObject);
        String requestBody = objectMapper.writeValueAsString(reservationObject);

        mockMvc.perform(MockMvcRequestBuilders.post("/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().is5xxServerError());
    }
    @Test
    void updateReservationTest() throws Exception {
        Reservation reservationObject = Reservation.builder()

                .name("Ana")
                .hotelName("Artemis")
                .numberOfPeople(4)
                .standard(Standard.NORMAL)
                .startDate(LocalDate.ofEpochDay(2024-4-16))
                .endDate(LocalDate.ofEpochDay(2024-4-30))
                .price(200)
                .build();
        Reservation savedObject= reservationRepository.save(reservationObject);
        savedObject.setName("ANDREI");
        String requestBody = objectMapper.writeValueAsString(reservationObject);

        mockMvc.perform(MockMvcRequestBuilders.put("/reservation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(jsonPath("$.name").value("ANDREI"));


    }
    @Test
    void when_we_have_an_object_with_a_name_then_receive_the_object_from_db() throws Exception {
        Reservation reservationObject = Reservation.builder()

                .name("Ana")
                .hotelName("Artemis")
                .numberOfPeople(4)
                .standard(Standard.NORMAL)
                .startDate(LocalDate.ofEpochDay(2024-4-16))
                .endDate(LocalDate.ofEpochDay(2024-4-30))
                .price(200)
                .build();
        Reservation reservationObject2 = Reservation.builder()

                .name("Ana")
                .hotelName("Artemis")
                .numberOfPeople(4)
                .standard(Standard.NORMAL)
                .startDate(LocalDate.ofEpochDay(2024-4-16))
                .endDate(LocalDate.ofEpochDay(2024-4-30))
                .price(200)
                .build();

        reservationRepository.save(reservationObject);
        reservationRepository.save(reservationObject2);
        mockMvc.perform(MockMvcRequestBuilders.get("/reservation/name/Ana"))
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Ana"))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[1].name").value("Ana"))
                .andExpect(jsonPath("$[1].id").value("2"));

    }

    @Test
    void when_we_have_an_object_with_a_name_then_no_object_found() throws Exception {
        Reservation reservationObject = Reservation.builder()

                .name("Ana")
                .hotelName("Artemis")
                .numberOfPeople(4)
                .standard(Standard.NORMAL)
                .startDate(LocalDate.ofEpochDay(2024-4-16))
                .endDate(LocalDate.ofEpochDay(2024-4-30))
                .price(200)
                .build();

        reservationRepository.save(reservationObject);
        mockMvc.perform(MockMvcRequestBuilders.get("/reservation/name/Azorel"))
                .andExpect(jsonPath("$").isEmpty());

    }
}
