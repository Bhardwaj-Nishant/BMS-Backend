package com.bookmyshow.bms.Service;

import com.bookmyshow.bms.Dto.*;
import com.bookmyshow.bms.Exception.resourceNotFoundException;
import com.bookmyshow.bms.Repository.MovieRepository;
import com.bookmyshow.bms.Repository.ScreenRepository;
import com.bookmyshow.bms.Repository.ShowRepository;
import com.bookmyshow.bms.Repository.ShowSeatRepository;
import com.bookmyshow.bms.model.Movie;
import com.bookmyshow.bms.model.Screen;
import com.bookmyshow.bms.model.Show;
import com.bookmyshow.bms.model.ShowSeat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShowService {

    @Autowired
    private ScreenRepository screenRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private ShowSeatRepository showSeatRepository;

    public ShowDto createShow(ShowDto showDto) {
        Show show = new Show();
        Movie movie = movieRepository.findById(showDto.getMovie().getId())
                .orElseThrow(()->new resourceNotFoundException("Movie not found with id: " + showDto.getMovie().getId()));

        Screen screen = screenRepository.findById(showDto.getScreen().getId())
                .orElseThrow(()->new resourceNotFoundException("Screen not found with id: " + showDto.getScreen().getId()));

        show.setMovie(movie);
        show.setScreen(screen);
        show.setStartTime(showDto.getStartTime());
        show.setEndTime(showDto.getEndTime());

        Show savedShow = showRepository.save(show);

        List<ShowSeat> availableSeats =
                showSeatRepository.findByShowIdAndStatus(savedShow.getId(),"AVAILABLE");

        return mapToDto(savedShow, availableSeats);

    }

    private ShowDto mapToDto(Show show,List<ShowSeat> availableSeats) {
        ShowDto showDto = new ShowDto();
        showDto.setId(show.getId());
        showDto.setStartTime(show.getStartTime());
        showDto.setEndTime(show.getEndTime());

        showDto.setMovie(new MovieDto(
                show.getMovie().getId(),
                show.getMovie().getTitle(),
                show.getMovie().getDescription(),
                show.getMovie().getLanguage(),
                show.getMovie().getGenre(),
                show.getMovie().getDurationMins(),
                show.getMovie().getReleaseDate(),
                show.getMovie().getPosterUrl()
        ));

        TheaterDto theaterDto = new TheaterDto(
                show.getScreen().getTheater().getId(),
                show.getScreen().getTheater().getName(),
                show.getScreen().getTheater().getAddress(),
                show.getScreen().getTheater().getCity(),
                show.getScreen().getTheater().getTotalScreens()
        );

        showDto.setScreen(new ScreenDto(
                show.getScreen().getId(),
                show.getScreen().getName(),
                show.getScreen().getTotalSeats(),
                theaterDto
        ));

        List<ShowSeatDto> seatDtos = availableSeats.stream()
                .map(Seat -> {
                    ShowSeatDto seatDto = new ShowSeatDto();
                    seatDto.setId(Seat.getId());
                    seatDto.setStatus(Seat.getStatus());
                    seatDto.setPrice(Seat.getPrice());

                    SeatDto baseSeatDto = new SeatDto();
                    baseSeatDto.setId(Seat.getId());
                    baseSeatDto.setSeatNumber(Seat.getSeat().getSeatNumber());
                    baseSeatDto.setSeatType(Seat.getSeat().getSeatType());
                    baseSeatDto.setBasePrice(Seat.getSeat().getBasePrice());
                    seatDto.setSeat(baseSeatDto);
                    return seatDto;
                })
                .collect(Collectors.toList());

        showDto.setAvailableSeats(seatDtos);
        return showDto;
    }

    public ShowDto getShowById(Long id) {
        Show show = showRepository.findById(id)
                .orElseThrow(()->new resourceNotFoundException("Show not found with Id :"+id));

        List<ShowSeat> availableSeats = showSeatRepository.findByShowIdAndStatus(show.getId(),"AVAILABLE");
        return mapToDto(show,availableSeats);
    }

    public List<ShowDto> getAllShows() {
        List<Show> shows = showRepository.findAll();
        return shows.stream()
                .map(show->{
                    List<ShowSeat> availableSeats = showSeatRepository.findByShowIdAndStatus(show.getId(),"AVAILABLE");
                    return mapToDto(show,availableSeats);
                })
                .collect(Collectors.toList());
    }

    public List<ShowDto> getShowsByMovie(Long movieId) {
        List<Show> shows = showRepository.findByMovieId(movieId);
        return shows.stream()
                .map(show->{
                    List<ShowSeat> availableSeats = showSeatRepository.findByShowIdAndStatus(show.getId(),"AVAILABLE");
                    return mapToDto(show,availableSeats);
                })
                .collect(Collectors.toList());
    }

    public List<ShowDto> getShowsByMoviesAndCity(Long movieId, String city) {
        List<Show> shows = showRepository.findByMovie_IdAndScreen_Theater_City(movieId,city);
        return shows.stream()
                .map(show->{
                    List<ShowSeat> availableSeats = showSeatRepository.findByShowIdAndStatus(show.getId(),"AVAILABLE");
                    return mapToDto(show,availableSeats);
                })
                .collect(Collectors.toList());
    }

    public List<ShowDto> getShowsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Show> shows = showRepository.findByStartTimeBetween(startDate, endDate);
        return shows.stream()
                .map(show->{
                    List<ShowSeat> availableSeats = showSeatRepository.findByShowIdAndStatus(show.getId(),"AVAILABLE");
                    return mapToDto(show,availableSeats);
                })
                .collect(Collectors.toList());
    }
}
