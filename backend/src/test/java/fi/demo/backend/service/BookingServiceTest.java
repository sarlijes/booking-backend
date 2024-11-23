package fi.demo.backend.service;

import fi.demo.backend.entity.Booking;
import fi.demo.backend.repository.BookingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @InjectMocks
    private BookingService bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Test
    public void dependenciesAreInjected() {
        assertThat(bookingService).isNotNull();
        assertThat(bookingRepository).isNotNull();
    }

    @Test
    public void canListDuplicatesWhenCustomerHasTwoReservations() {
        // Basic case
        List<Booking> result = bookingService
                .findDuplicateBookings(LocalDateTime.of(2020, 1, 7, 8, 0));
        assertThat(result).hasSize(2);
    }

}
