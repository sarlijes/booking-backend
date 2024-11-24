package fi.demo.backend.repository;

import fi.demo.backend.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    /**
     * Returns the bookings with start times greater than the given datetime, ordered by their start time
     * @param   from: the datetime of interest
     * @return  A list of bookings
     */
    List<Booking> findByStartTimeGreaterThanOrderByStartTime(LocalDateTime from);

}