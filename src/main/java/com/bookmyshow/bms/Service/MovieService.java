package com.bookmyshow.bms.Service;

import com.bookmyshow.bms.Dto.MovieDto;
import com.bookmyshow.bms.Exception.resourceNotFoundException;
import com.bookmyshow.bms.Repository.MovieRepository;
import com.bookmyshow.bms.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public MovieDto createMovie(MovieDto movieDto) {
        Movie movie = mapToEntity(movieDto);
        Movie savedMovie = movieRepository.save(movie);
        return mapToDto(savedMovie);
    }

    private Movie mapToEntity(MovieDto movieDto) {
        Movie movie = new Movie();
        movie.setId(movieDto.getId());
        movie.setTitle(movieDto.getTitle());
        movie.setDescription(movieDto.getDescription());
        movie.setLanguage(movieDto.getLanguage());
        movie.setGenre(movieDto.getGenre());
        movie.setDurationMins(movieDto.getDurationMins());
        movie.setReleaseDate(movieDto.getReleaseDate());
        movie.setPosterUrl(movieDto.getPosterUrl());
        return movie;
    }

    private MovieDto mapToDto(Movie movie) {
        MovieDto movieDto = new MovieDto();
        movieDto.setId(movie.getId());
        movieDto.setTitle(movie.getTitle());
        movieDto.setDescription(movie.getDescription());
        movieDto.setLanguage(movie.getLanguage());
        movieDto.setGenre(movie.getGenre());
        movieDto.setDurationMins(movie.getDurationMins());
        movieDto.setReleaseDate(movie.getReleaseDate());
        movieDto.setPosterUrl(movie.getPosterUrl());
        return movieDto;
    }

    public MovieDto getMovieById(Long id){
        Movie movie = movieRepository.findById(id)
                .orElseThrow(()->new resourceNotFoundException("Movie not found with id: " + id));
        return mapToDto(movie);
    }

    public List<MovieDto> getAllMovies() {
        List<Movie> movies = movieRepository.findAll();
        return movies.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<MovieDto> getMovieByLanguage(String language) {
        List<Movie> movies = movieRepository.findByLanguage(language);
        return movies.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<MovieDto> getMovieByGenre(String genre) {
        List<Movie> movies = movieRepository.findByLanguage(genre);
        return movies.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<MovieDto> getMovieByTitle(String title) {
        List<Movie> movies = movieRepository.findByLanguage(title);
        return movies.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public MovieDto updateMovie(Long id,MovieDto movieDto) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(()->new resourceNotFoundException("Movie not found with id: " + id));
        movie.setId(movieDto.getId());
        movie.setTitle(movieDto.getTitle());
        movie.setDescription(movieDto.getDescription());
        movie.setLanguage(movieDto.getLanguage());
        movie.setGenre(movieDto.getGenre());
        movie.setDurationMins(movieDto.getDurationMins());
        movie.setReleaseDate(movieDto.getReleaseDate());
        movie.setPosterUrl(movieDto.getPosterUrl());

        Movie UpdatedMovie = movieRepository.save(movie);
        return mapToDto(UpdatedMovie);
    }

    public void deleteMovie(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(()->new resourceNotFoundException("Movie not found with id: " + id));
        movieRepository.delete(movie);
    }

}
