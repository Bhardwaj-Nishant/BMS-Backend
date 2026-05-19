package com.bookmyshow.bms.Service;

import com.bookmyshow.bms.Dto.*;
import com.bookmyshow.bms.Exception.resourceNotFoundException;
import com.bookmyshow.bms.Exception.seatUnavailableException;
import com.bookmyshow.bms.Repository.BookingRepository;
import com.bookmyshow.bms.Repository.ShowRepository;
import com.bookmyshow.bms.Repository.ShowSeatRepository;
import com.bookmyshow.bms.Repository.UserRepository;
import com.bookmyshow.bms.model.*;
import jakarta.servlet.Filter;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookingService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private ShowSeatRepository showSeatRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Transactional
    public BookingDto createBooking(BookingRequestDto bookingRequest) {
        User users= userRepository.findById(bookingRequest.getUserId())
                .orElseThrow(()->new resourceNotFoundException("User Not Found"));

        Show shows= showRepository.findById(bookingRequest.getShowId())
                .orElseThrow(()->new resourceNotFoundException("Show Not Found"));

        List<ShowSeat> selectedSeats = showSeatRepository.findAllById(bookingRequest.getSeatIds());

        for(ShowSeat seat : selectedSeats){
            if(!"AVAILABLE".equals(seat.getStatus())){
                throw new seatUnavailableException("Seat" + seat.getSeat().getSeatNumber() + "is not available");
            }
            seat.setStatus("LOCKED");
        }

        showSeatRepository.saveAll(selectedSeats);

        Double totalAmount = selectedSeats.stream()
                .mapToDouble(ShowSeat::getPrice)
                .sum();

        Payment payment = new Payment();
        payment.setAmount(totalAmount);
        payment.setPaymentTime(LocalDateTime.now());
        payment.setPaymentType(bookingRequest.getPaymentMethod());
        payment.setStatus("SUCCESS");
        payment.setTransactionId(UUID.randomUUID().toString());

        Booking booking = new Booking();
        booking.setUser(users);
        booking.setShow(shows);
        booking.setBookingTime(LocalDateTime.now());
        booking.setStatus("CONFIRMED");
        booking.setTotalAmount(totalAmount);
        booking.setBookingNumber(UUID.randomUUID().toString());
        booking.setPayment(payment);

        Booking savedBooking = bookingRepository.save(booking);

        selectedSeats.forEach(seat -> {
            seat.setStatus("BOOKED");
            seat.setBooking(savedBooking);
        });

        showSeatRepository.saveAll(selectedSeats);

        return mapToBookingDto(savedBooking,selectedSeats);
    }

    private BookingDto mapToBookingDto(Booking booking,List<ShowSeat> seats) {

        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setBookingNumber(booking.getBookingNumber());
        bookingDto.setBookingTime(booking.getBookingTime());
        bookingDto.setStatus(booking.getStatus());
        bookingDto.setTotalAmount(booking.getTotalAmount());

        UserDto userDto = new UserDto();
        userDto.setId(booking.getUser().getId());
        userDto.setName(booking.getUser().getName());
        userDto.setEmail(booking.getUser().getEmail());
        userDto.setPhoneNumber(booking.getUser().getPhone());
        bookingDto.setUser(userDto);

        ShowDto showDto = new ShowDto();
        showDto.setId(booking.getShow().getId());
        showDto.setStartTime(booking.getShow().getStartTime());
        showDto.setEndTime(booking.getShow().getEndTime());

        MovieDto movieDto = new MovieDto();
        movieDto.setId(booking.getShow().getMovie().getId());
        movieDto.setTitle(booking.getShow().getMovie().getTitle());
        movieDto.setDescription(booking.getShow().getMovie().getDescription());
        movieDto.setGenre(booking.getShow().getMovie().getGenre());
        movieDto.setDurationMins(booking.getShow().getMovie().getDurationMins());
        movieDto.setReleaseDate(booking.getShow().getMovie().getReleaseDate());
        movieDto.setPosterUrl(booking.getShow().getMovie().getPosterUrl());
        showDto.setMovie(movieDto);

        ScreenDto screenDto = new ScreenDto();
        screenDto.setId(booking.getShow().getScreen().getId());
        screenDto.setName(booking.getShow().getScreen().getName());
        screenDto.setTotalSeats(booking.getShow().getScreen().getTotalSeats());

        TheaterDto theaterDto = new TheaterDto();
        theaterDto.setId(booking.getShow().getScreen().getTheater().getId());
        theaterDto.setName(booking.getShow().getScreen().getTheater().getName());
        theaterDto.setAddress(booking.getShow().getScreen().getTheater().getAddress());
        theaterDto.setCity(booking.getShow().getScreen().getTheater().getCity());
        theaterDto.setTotalScreens(booking.getShow().getScreen().getTheater().getTotalScreens());

        screenDto.setTheater(theaterDto);
        showDto.setScreen(screenDto);
        bookingDto.setShow(showDto);

        List<ShowSeatDto> seatDtos = seats.stream().map(seat ->{
            ShowSeatDto showSeatDto = new ShowSeatDto();
            showSeatDto.setId(seat.getId());
            showSeatDto.setStatus(seat.getStatus());
            showSeatDto.setPrice(seat.getPrice());

            SeatDto seatDto = new SeatDto();
            seatDto.setId(seat.getSeat().getId());
            seatDto.setSeatNumber(seat.getSeat().getSeatNumber());
            seatDto.setSeatType(seat.getSeat().getSeatType());
            seatDto.setBasePrice(seat.getSeat().getBasePrice());

            showSeatDto.setSeat(seatDto);
            return showSeatDto;
        })
                .collect(Collectors.toList());
        bookingDto.setSeats(seatDtos);

        if(booking.getPayment() != null) {
            PaymentDto paymentDto = new PaymentDto();
            paymentDto.setId(booking.getPayment().getId());
            paymentDto.setAmount(booking.getPayment().getAmount());
            paymentDto.setPaymentMethod(booking.getPayment().getPaymentType());
            paymentDto.setPaymentTime(booking.getPayment().getPaymentTime());
            paymentDto.setStatus(booking.getPayment().getStatus());
            paymentDto.setTransactionId(booking.getPayment().getTransactionId());
            bookingDto.setPayment(paymentDto);
        }
        return bookingDto;
    }

    public BookingDto getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(()->new resourceNotFoundException("Booking Not Found"));

        List<ShowSeat> seats = showSeatRepository.findAll()
                .stream()
                .filter(seat -> seat.getBooking()!=null && seat.getBooking().getId().equals(booking.getId()))
                .collect(Collectors.toList());

        return mapToBookingDto(booking, seats);
    }

    public BookingDto getBookingByNumber(String number) {
        Booking booking = bookingRepository.findByBookingNumber(number)
                .orElseThrow(()->new resourceNotFoundException("Booking Not Found"));

        List<ShowSeat> seats = showSeatRepository.findAll()
                .stream()
                .filter(seat -> seat.getBooking()!=null && seat.getBooking().getId().equals(booking.getId()))
                .collect(Collectors.toList());

        return mapToBookingDto(booking, seats);
    }

    public List<BookingDto> getBookingByUserId(Long userId) {
        List<Booking> bookings = bookingRepository.findByUserId(userId);

        return bookings.stream()
                .map(booking -> {
                    List<ShowSeat> seats = showSeatRepository.findAll()
                            .stream()
                            .filter(seat -> seat.getBooking()!=null && seat.getBooking().getId().equals(booking.getId()))
                            .collect(Collectors.toList());
                    return mapToBookingDto(booking, seats);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public BookingDto cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(()->new resourceNotFoundException("Booking Not Found"));

        booking.setStatus("CANCELLED");

        List<ShowSeat> seats = showSeatRepository.findAll()
                .stream()
                .filter(seat -> seat.getBooking()!=null && seat.getBooking().getId().equals(booking.getId()))
                .collect(Collectors.toList());

        seats.forEach(seat -> {
            seat.setStatus("AVAILABLE");
            seat.setBooking(null);
        });

        if(booking.getPayment() != null) {
            booking.getPayment().setStatus("REFUNDED");
        }

        Booking updatedBooking = bookingRepository.save(booking);
        showSeatRepository.saveAll(seats);

        return mapToBookingDto(updatedBooking, seats);
    }
}
