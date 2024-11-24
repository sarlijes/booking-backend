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

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

    private Booking firstBooking = null;

    @Test
    public void dependenciesAreInjected() {
        assertThat(bookingService).isNotNull();
        assertThat(bookingRepository).isNotNull();
        assertThat(customerRepository).isNotNull();
    }

    public void mock() {
        Customer c1 = Customer.builder().lastName("Brent").id(1L).build();
        Customer c2 = Customer.builder().lastName("Canterbury").id(2L).build();
        List<Booking> bookings = new ArrayList<>();
        // A booking for a certain customer (c1):
        firstBooking = Booking.builder()
                .startTime(LocalDateTime.of(2020, 1, 7, 9, 0))
                .customer(c1)
                .build();
        bookings.add(firstBooking);
        // Corner case (just on the threshold of interest since the first booking), same customer:
        bookings.add(Booking.builder()
                .startTime(LocalDateTime.of(2020, 1, 8, 9, 0))
                .customer(c1)
                .build());
        // Corner case (just outside the duration of interest since the second booking), same customer:
        bookings.add(Booking.builder()
                .startTime(LocalDateTime.of(2020, 1, 9, 9, 15))
                .customer(c1)
                .build());
        // Same day as the first one, but different customer:
        bookings.add(Booking.builder()
                .startTime(LocalDateTime.of(2020, 1, 7, 9, 30))
                .customer(c2)
                .build());
        when(bookingRepository
                .findByStartTimeGreaterThanOrderByStartTime(firstBooking.getStartTime()))
                .thenReturn(bookings);
    }

    public void mockDuplicates(LocalDateTime startTime) {
        Customer c3 = Customer.builder().lastName("Taylor-Clarke").id(3L).build();
        List<Booking> bookings = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            bookings.add(Booking.builder()
                    .startTime(LocalDateTime.of(2020, 2, 19, 9, 30))
                    .customer(c3)
                    .build());
        }
        when(bookingRepository
                .findByStartTimeGreaterThanOrderByStartTime(startTime))
                .thenReturn(bookings);
    }

    @Test
    public void canListDuplicatesWhenCustomerHasTheirSecondBookingJustOnTheThreshold() {
        mock();
        // The customer has two bookings within 24 hours of first booking's start time,
        // (just on the threshold), so they are duplicates
        List<Booking> result = bookingService.findDuplicateBookings(
                        firstBooking.getStartTime(),
                        Duration.ofDays(1));
        assertThat(result).hasSize(2);
    }

    @Test
    public void noDuplicatesAreReturnedWhenCustomerHasTheirSecondBookingJustAfterTheThreshold() {
        mock();
        List<Booking> result = bookingService.findDuplicateBookings(
                firstBooking.getStartTime(),
                Duration.ofHours(23).plusMinutes(59));
        assertThat(result).hasSize(0);
    }

    @Test
    public void canListDuplicatesWhenCustomerHasTheirSecondBookingJustInsideTheThreshold() {
        mock();
        List<Booking> result = bookingService.findDuplicateBookings(
                firstBooking.getStartTime(),
                Duration.ofDays(1).plusMinutes(1));
        assertThat(result).hasSize(2);
    }

    @Test
    public void canListDuplicatesWhenCustomerHasTwoBookingsWithTheSameStartTime() {
        LocalDateTime startTime = LocalDateTime.of(2020, 1, 7, 9, 0);
        mockDuplicates(startTime);
        List<Booking> result = bookingService.findDuplicateBookings(
                startTime,
                Duration.ofDays(1));
        assertThat(result).hasSize(2);
    }

    @Test
    public void canListDuplicatesAndCorrectBookingsAreReturned() {
        mock();
        List<Booking> result = bookingService.findDuplicateBookings(
                firstBooking.getStartTime(),
                Duration.ofDays(1));
        assertThat(result).hasSize(2);
        result.sort(Comparator.comparing(Booking::getStartTime));
        Booking b1 = result.get(0);
        assertThat(b1).isNotNull();
        assertThat(b1.getStartTime()).isEqualTo((LocalDateTime.of(2020, 1, 7, 9, 0)));
        assertThat(b1.getCustomer()).isNotNull();
        assertThat(b1.getCustomer().getId()).isEqualTo(1L);
        assertThat(b1.getCustomer().getLastName()).isEqualTo("Brent");

        Booking b2 = result.get(1);
        assertThat(b2).isNotNull();
        assertThat(b2.getStartTime()).isEqualTo((LocalDateTime.of(2020, 1, 8, 9, 0)));
        assertThat(b2.getCustomer()).isNotNull();
        assertThat(b2.getCustomer().getId()).isEqualTo(1L);
        assertThat(b2.getCustomer().getLastName()).isEqualTo("Brent");
    }
}
