package com.bookmyshow.bms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="seats")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String seatNumber;

    @Column(nullable = false)
    private String SeatType;

    @Column(nullable = false)
    private Double BasePrice;

    @ManyToOne
    @JoinColumn(name = "screen_id", nullable = false)
    private Screen screen;
}
