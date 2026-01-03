package com.showtime.service;

import com.showtime.dto.*;
import com.showtime.dto.response.*;
import com.showtime.dto.request.*;
import com.showtime.model.*;
import com.showtime.repository.MovieRepository;
import com.showtime.repository.ShowRepository;
import com.showtime.repository.TheaterRepository;
import com.showtime.repository.ScreenRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShowService {
    
    private final ShowRepository showRepository;
    private final MovieRepository movieRepository;
    private final TheaterRepository theaterRepository;
    private final ScreenRepository screenRepository;
    private final ObjectMapper objectMapper;
    
    public List<ShowDTO> getShowsByMovie(Long movieId, String city) {
        LocalDate today = LocalDate.now();
        return showRepository.findShowsByMovieAndCity(movieId, today, city).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<LocalDate> getAvailableDatesForMovie(Long movieId) {
        return showRepository.findAvailableDatesForMovie(movieId);
    }
    
    public List<ShowDTO> getShowsByTheater(Long theaterId, LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        return showRepository.findByTheaterIdAndShowDateAndIsActiveTrueOrderByShowTime(theaterId, date).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public SeatLayoutResponse getSeatLayout(Long showId) {
        Show show = showRepository.findById(showId)
            .orElseThrow(() -> new RuntimeException("Show not found"));
        
        SeatLayoutResponse response = new SeatLayoutResponse();
        response.setClassicPrice(show.getPriceClassic());
        response.setPremiumPrice(show.getPricePremium());
        
        try {
            List<String> bookedSeats = objectMapper.readValue(show.getBookedSeats(), List.class);
            response.setBookedSeats(bookedSeats);
            
            // Generate seat layout (5 rows, 20 seats per row)
            List<List<SeatDTO>> seatLayout = new ArrayList<>();
            int totalRows = 5;
            int seatsPerRow = 20;
            
            for (int row = 0; row < totalRows; row++) {
                List<SeatDTO> rowSeats = new ArrayList<>();
                for (int seatNum = 1; seatNum <= seatsPerRow; seatNum++) {
                    String seatNumber = String.format("%c%d", 'A' + row, seatNum);
                    SeatDTO seat = new SeatDTO();
                    seat.setSeatNumber(seatNumber);
                    
                    // First 2 rows are premium
                    if (row < 2) {
                        seat.setSeatType("PREMIUM");
                        seat.setPrice(show.getPricePremium());
                    } else {
                        seat.setSeatType("CLASSIC");
                        seat.setPrice(show.getPriceClassic());
                    }
                    
                    seat.setAvailable(!bookedSeats.contains(seatNumber));
                    seat.setSelected(false);
                    rowSeats.add(seat);
                }
                seatLayout.add(rowSeats);
            }
            
            response.setSeatLayout(seatLayout);
            return response;
            
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing seat layout");
        }
    }
    
    @Transactional
    public ShowDTO createShow(ShowRequest request) {
        Movie movie = movieRepository.findById(request.getMovieId())
            .orElseThrow(() -> new RuntimeException("Movie not found"));
        Theater theater = theaterRepository.findById(request.getTheaterId())
            .orElseThrow(() -> new RuntimeException("Theater not found"));
        Screen screen = screenRepository.findById(request.getScreenId())
            .orElseThrow(() -> new RuntimeException("Screen not found"));
        
        Show show = new Show();
        show.setMovie(movie);
        show.setTheater(theater);
        show.setScreen(screen);
        show.setShowDate(request.getShowDate());
        show.setShowTime(request.getShowTime());
        show.setPriceClassic(request.getPriceClassic());
        show.setPricePremium(request.getPricePremium());
        show.setAvailableSeats(screen.getTotalSeats());
        show.setBookedSeats("[]");
        
        show = showRepository.save(show);
        return convertToDTO(show);
    }
    
    @Transactional
    public void updateBookedSeats(Long showId, List<String> seatNumbers) {
        Show show = showRepository.findById(showId)
            .orElseThrow(() -> new RuntimeException("Show not found"));
        
        try {
            List<String> bookedSeats = objectMapper.readValue(show.getBookedSeats(), List.class);
            bookedSeats.addAll(seatNumbers);
            show.setBookedSeats(objectMapper.writeValueAsString(bookedSeats));
            show.setAvailableSeats(show.getAvailableSeats() - seatNumbers.size());
            showRepository.save(show);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error updating booked seats");
        }
    }
    
    public Long getTotalShows() {
        return showRepository.countActiveShows();
    }
    
    private ShowDTO convertToDTO(Show show) {
        ShowDTO dto = new ShowDTO();
        dto.setId(show.getId());
        dto.setShowDate(show.getShowDate());
        dto.setShowTime(show.getShowTime());
        dto.setEndTime(show.getEndTime());
        dto.setPriceClassic(show.getPriceClassic());
        dto.setPricePremium(show.getPricePremium());
        dto.setAvailableSeats(show.getAvailableSeats());
        dto.setBookedSeats(show.getBookedSeats());
        dto.setActive(show.isActive());
        
        if (show.getMovie() != null) {
            MovieDTO movieDTO = new MovieDTO();
            movieDTO.setId(show.getMovie().getId());
            movieDTO.setTitle(show.getMovie().getTitle());
            movieDTO.setDuration(show.getMovie().getDuration());
            movieDTO.setLanguage(show.getMovie().getLanguage());
            dto.setMovie(movieDTO);
        }
        
        if (show.getTheater() != null) {
            TheaterDTO theaterDTO = new TheaterDTO();
            theaterDTO.setId(show.getTheater().getId());
            theaterDTO.setName(show.getTheater().getName());
            theaterDTO.setCity(show.getTheater().getCity());
            dto.setTheater(theaterDTO);
        }
        
        return dto;
    }
}