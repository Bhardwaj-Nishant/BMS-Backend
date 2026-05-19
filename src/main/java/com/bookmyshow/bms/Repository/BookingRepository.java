package com.bookmyshow.bms.Repository;

import com.bookmyshow.bms.model.Booking;
import com.bookmyshow.bms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);

    Optional<Booking> findByBookingNumber(String bookingNumber);

    List<Booking> findByShowId(Long Id);

    Long user(User user);
}
