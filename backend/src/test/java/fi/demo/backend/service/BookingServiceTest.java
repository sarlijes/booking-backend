package fi.demo.backend.service;

import fi.demo.backend.entity.Booking;
import fi.demo.backend.entity.Customer;
import fi.demo.backend.repository.BookingRepository;
import fi.demo.backend.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @InjectMocks
    private BookingService bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Test
    public void dependenciesAreInjected() {
        assertThat(bookingService).isNotNull();
        assertThat(bookingRepository).isNotNull();
        assertThat(customerRepository).isNotNull();
    }

    public void mock() {
        Customer c1 = Customer.builder().lastName("Brent").build();
        Customer c2 = Customer.builder().lastName("Canterbury").build();
        when(customerRepository.findById(1L)).thenReturn(Optional.of(c1));
        List<Booking> bookings = new ArrayList<>();
        // Several bookings on the same day for the same customer
        bookings.add(Booking.builder()
                .startTime(LocalDateTime.of(2020, 1, 7, 1, 58))
                .customer(c1)
                .build());
        bookings.add(Booking.builder()
                .startTime(LocalDateTime.of(2020, 1, 7, 7, 59))
                .customer(c1)
                .build());
        bookings.add(Booking.builder()
                .startTime(LocalDateTime.of(2020, 1, 7, 8, 0))
                .customer(c1)
                .build());
        bookings.add(Booking.builder()
                .startTime(LocalDateTime.of(2020, 1, 7, 8, 1))
                .customer(c1)
                .build());
        // Same day, but different customer
        bookings.add(Booking.builder()
                .startTime(LocalDateTime.of(2020, 1, 7, 8, 0))
                .customer(c2)
                .build());
        // Different future days, both customers
        for (int i = 1; i <= 31; i++) {
            bookings.add(Booking.builder()
                    .startTime(LocalDateTime.of(2020, 3, i, 8, 0))
                    .customer(c1)
                    .build());
            bookings.add(Booking.builder()
                    .startTime(LocalDateTime.of(2020, 3, i, 9, 0))
                    .customer(c2)
                    .build());
        }
        when(bookingRepository
                .findByStartTimeGreaterThan(LocalDateTime.of(2020, 1, 7, 8, 0)))
                .thenReturn(bookings);
    }

    @Test
    public void canListDuplicatesWhenCustomerHasTwoBookings() {
        mock();
        // Basic case
        List<Booking> result = bookingService
                .findDuplicateBookings(LocalDateTime.of(2020, 1, 7, 8, 0));
        assertThat(result).hasSize(2);
    }

}
