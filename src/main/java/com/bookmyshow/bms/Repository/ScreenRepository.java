package com.bookmyshow.bms.Repository;

import com.bookmyshow.bms.model.Screen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScreenRepository extends JpaRepository<Screen, Long> {

    List<Screen> findByTheaterId(Long theaterId);

}
