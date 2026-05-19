package com.bookmyshow.bms.Repository;

import com.bookmyshow.bms.model.ShowSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ShowSeatRepository extends JpaRepository<ShowSeat, Long> {
    List<ShowSeat> findByShowId(Long movieId);

    List<ShowSeat> findByShowIdAndStatus(Long showId, String status);

}
