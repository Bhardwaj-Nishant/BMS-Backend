package com.bookmyshow.bms.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShowSeatDto {
    private Long id;
    private ShowDto show;
    private SeatDto seat;
    private String status;
    private Double price;
}
