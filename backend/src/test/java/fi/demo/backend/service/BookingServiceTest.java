package fi.demo.backend.service;

import fi.demo.backend.entity.Booking;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @InjectMocks
    private BookingService bookingService;

    @Test
    public void dependencyIsInjected() {
        assertThat(bookingService).isNotNull();
    }

    @Test
    public void canListDuplicatesWhenCustomerHasTwoReservations() {
        // Basic case
        List<Booking> result = bookingService.findDuplicateBookings();
        assertThat(result).hasSize(2);
    }

}
