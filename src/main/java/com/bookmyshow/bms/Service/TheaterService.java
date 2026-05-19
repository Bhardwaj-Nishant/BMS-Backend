package com.bookmyshow.bms.Service;

import com.bookmyshow.bms.Dto.TheaterDto;
import com.bookmyshow.bms.Exception.resourceNotFoundException;
import com.bookmyshow.bms.Repository.MovieRepository;
import com.bookmyshow.bms.Repository.TheaterRepository;
import com.bookmyshow.bms.model.Theater;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TheaterService {

    private TheaterRepository theaterRepository;

    private TheaterDto createTheater(TheaterDto theaterDto) {
        Theater theater = mapToEntity(theaterDto);
        Theater savedTheater = theaterRepository.save(theater);
        return mapToDto(savedTheater);
    }

    private TheaterDto getTheaterById(Long id) {
        Theater theater = theaterRepository.findById(id).
                orElseThrow(()-> new resourceNotFoundException("Theater not found with id: " + id));
        return mapToDto(theater);
    }

    private List<TheaterDto> getAllTheaters() {
        List<Theater> theaters = theaterRepository.findAll();
        return theaters.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private List<TheaterDto> getAllTheatersByCity(String city) {
        List<Theater> theaters = theaterRepository.findByCity(city);
        return theaters.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private TheaterDto updateTheater(Long id,TheaterDto theaterDto) {
        Theater theater = theaterRepository.findById(id)
                .orElseThrow(()-> new resourceNotFoundException("Theater not found with id: " + id));
        theater.setId(theaterDto.getId());
        theater.setName(theaterDto.getName());
        theater.setAddress(theaterDto.getAddress());
        theater.setCity(theaterDto.getCity());
        theater.setTotalScreens(theaterDto.getTotalScreens());

        Theater savedTheater = theaterRepository.save(theater);
        return mapToDto(savedTheater);
    }

    private void deleteTheater(Long id) {
        Theater theater = theaterRepository.findById(id)
                        .orElseThrow(()-> new resourceNotFoundException("Theater not found with id: " + id));
        theaterRepository.deleteById(id);
    }

    private Theater mapToEntity(TheaterDto theaterDto) {
        Theater theater = new Theater();
        theater.setName(theaterDto.getName());
        theater.setAddress(theaterDto.getAddress());
        theater.setCity(theaterDto.getCity());
        theater.setTotalScreens(theaterDto.getTotalScreens());
        return theater;
    }

    private TheaterDto mapToDto(Theater theater) {
        TheaterDto theaterDto = new TheaterDto();
        theaterDto.setId(theater.getId());
        theaterDto.setName(theater.getName());
        theaterDto.setAddress(theater.getAddress());
        theaterDto.setCity(theater.getCity());
        theaterDto.setTotalScreens(theater.getTotalScreens());
        return theaterDto;
    }

}
