package com.showtime.service;

import com.showtime.dto.*;
import com.showtime.model.*;
import com.showtime.repository.*;
import com.showtime.service.EmailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.showtime.dto.response.*;
import com.showtime.dto.request.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {
    
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ShowRepository showRepository;
    private final PaymentRepository paymentRepository;
    private final EmailService emailService;
    private final ObjectMapper objectMapper;
    
    @Transactional
    public BookingResponse createBooking(BookingRequest request, User user) {
        Show show = showRepository.findById(request.getShowId())
            .orElseThrow(() -> new RuntimeException("Show not found"));
        
        // Validate seat availability
        List<String> seatNumbers = request.getSeatNumbers();
        validateSeatAvailability(show, seatNumbers);
        
        // Calculate total amount
        double seatPrice = "PREMIUM".equals(request.getSeatType()) ? 
            show.getPricePremium() : show.getPriceClassic();
        double totalAmount = seatPrice * seatNumbers.size();
        double convenienceFee = 30.0 * seatNumbers.size();
        double taxAmount = totalAmount * 0.18; // 18% GST
        double finalAmount = totalAmount + convenienceFee + taxAmount;
        
        // Generate booking reference
        String bookingReference = "BKG" + System.currentTimeMillis() + user.getId();
        
        // Create booking
        Booking booking = new Booking();
        booking.setBookingReference(bookingReference);
        booking.setUser(user);
        booking.setShow(show);
        try {
            booking.setSeatNumbers(objectMapper.writeValueAsString(seatNumbers));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing seat numbers");
        }
        booking.setSeatType(request.getSeatType());
        booking.setTotalSeats(seatNumbers.size());
        booking.setTotalAmount(finalAmount);
        booking.setConvenienceFee(convenienceFee);
        booking.setTaxAmount(taxAmount);
        booking.setBookingStatus(Booking.BookingStatus.CONFIRMED);
        booking.setPaymentStatus(Booking.PaymentStatus.PENDING);
        
        booking = bookingRepository.save(booking);
        
        // Update show booked seats
        updateShowBookedSeats(show, seatNumbers);
        
        // Create payment record
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setAmount(finalAmount);
        payment.setStatus(Payment.PaymentStatus.PENDING);
        paymentRepository.save(payment);
        
        // Generate tickets
        generateTickets(booking, seatNumbers, seatPrice);
        
        BookingResponse response = new BookingResponse();
        response.setBookingId(booking.getId());
        response.setBookingReference(bookingReference);
        response.setTotalAmount(finalAmount);
        response.setPaymentStatus("PENDING");
        
        // Send confirmation email
        emailService.sendBookingConfirmation(user.getEmail(), booking);
        
        return response;
    }
    
    private void validateSeatAvailability(Show show, List<String> seatNumbers) {
        try {
            List<String> bookedSeats = objectMapper.readValue(show.getBookedSeats(), List.class);
            for (String seat : seatNumbers) {
                if (bookedSeats.contains(seat)) {
                    throw new RuntimeException("Seat " + seat + " is already booked");
                }
            }
            if (seatNumbers.size() > show.getAvailableSeats()) {
                throw new RuntimeException("Not enough seats available");
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error validating seat availability");
        }
    }
    
    private void updateShowBookedSeats(Show show, List<String> seatNumbers) {
        try {
            List<String> bookedSeats = objectMapper.readValue(show.getBookedSeats(), List.class);
            bookedSeats.addAll(seatNumbers);
            show.setBookedSeats(objectMapper.writeValueAsString(bookedSeats));
            show.setAvailableSeats(show.getAvailableSeats() - seatNumbers.size());
            showRepository.save(show);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error updating show seats");
        }
    }
    
    private void generateTickets(Booking booking, List<String> seatNumbers, double seatPrice) {
        for (String seatNumber : seatNumbers) {
            Ticket ticket = new Ticket();
            ticket.setBooking(booking);
            ticket.setTicketNumber("TKT" + System.currentTimeMillis() + seatNumber);
            ticket.setSeatNumber(seatNumber);
            ticket.setSeatType(booking.getSeatType());
            ticket.setPrice(seatPrice);
            // Generate QR code URL (in real app, generate actual QR code)
            ticket.setQrCodeUrl("/api/bookings/" + booking.getId() + "/ticket/" + ticket.getTicketNumber() + "/qr");
        }
    }
    
    public List<BookingDTO> getUserBookings(Long userId) {
        return bookingRepository.findByUserIdOrderByBookedAtDesc(userId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public BookingHistoryDTO getUserBookingHistory(Long userId) {
        BookingHistoryDTO history = new BookingHistoryDTO();
        history.setUpcomingBookings(bookingRepository.findUpcomingBookings(userId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList()));
        history.setPastBookings(bookingRepository.findPastBookings(userId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList()));
        history.setCancelledBookings(bookingRepository.findByUserIdAndBookingStatusOrderByBookedAtDesc(
            userId, "CANCELLED").stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList()));
        return history;
    }
    
    @Transactional
    public void cancelBooking(Long bookingId, String reason) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        if (!booking.getBookingStatus().equals(Booking.BookingStatus.CONFIRMED)) {
            throw new RuntimeException("Booking cannot be cancelled");
        }
        
        // Update booking status
        booking.setBookingStatus(Booking.BookingStatus.CANCELLED);
        booking.setCancelledAt(LocalDateTime.now());
        booking.setCancellationReason(reason);
        booking.setPaymentStatus(Booking.PaymentStatus.REFUNDED);
        bookingRepository.save(booking);
        
        // Update payment status
        Optional<Payment> paymentOpt = paymentRepository.findById(booking.getPayment().getId());
        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();
            payment.setStatus(Payment.PaymentStatus.REFUNDED);
            payment.setRefundDate(LocalDateTime.now());
            payment.setRefundAmount(booking.getTotalAmount());
            payment.setRefundReason(reason);
            paymentRepository.save(payment);
        }
        
        // Update show available seats
        try {
            Show show = booking.getShow();
            List<String> bookedSeats = objectMapper.readValue(show.getBookedSeats(), List.class);
            List<String> cancelledSeats = objectMapper.readValue(booking.getSeatNumbers(), List.class);
            bookedSeats.removeAll(cancelledSeats);
            show.setBookedSeats(objectMapper.writeValueAsString(bookedSeats));
            show.setAvailableSeats(show.getAvailableSeats() + cancelledSeats.size());
            showRepository.save(show);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error updating show seats");
        }
        
        // Send cancellation email
        emailService.sendBookingCancellation(booking.getUser().getEmail(), booking);
    }
    
    public Long getTotalBookings() {
        return bookingRepository.countConfirmedBookings();
    }
    
    public Double getTotalRevenue() {
        return bookingRepository.getTotalRevenue();
    }
    
    private BookingDTO convertToDTO(Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setId(booking.getId());
        dto.setBookingReference(booking.getBookingReference());
        dto.setTotalSeats(booking.getTotalSeats());
        dto.setTotalAmount(booking.getTotalAmount());
        dto.setConvenienceFee(booking.getConvenienceFee());
        dto.setTaxAmount(booking.getTaxAmount());
        dto.setBookingStatus(booking.getBookingStatus().name());
        dto.setPaymentStatus(booking.getPaymentStatus().name());
        dto.setBookedAt(booking.getBookedAt());
        
        try {
            dto.setSeatNumbers(objectMapper.readValue(booking.getSeatNumbers(), List.class));
        } catch (JsonProcessingException e) {
            dto.setSeatNumbers(new ArrayList<>());
        }
        
        if (booking.getShow() != null) {
            ShowDTO showDTO = new ShowDTO();
            showDTO.setId(booking.getShow().getId());
            showDTO.setShowDate(booking.getShow().getShowDate());
            showDTO.setShowTime(booking.getShow().getShowTime());
            
            if (booking.getShow().getMovie() != null) {
                MovieDTO movieDTO = new MovieDTO();
                movieDTO.setId(booking.getShow().getMovie().getId());
                movieDTO.setTitle(booking.getShow().getMovie().getTitle());
                showDTO.setMovie(movieDTO);
            }
            
            if (booking.getShow().getTheater() != null) {
                TheaterDTO theaterDTO = new TheaterDTO();
                theaterDTO.setId(booking.getShow().getTheater().getId());
                theaterDTO.setName(booking.getShow().getTheater().getName());
                theaterDTO.setCity(booking.getShow().getTheater().getCity());
                showDTO.setTheater(theaterDTO);
            }
            
            dto.setShow(showDTO);
        }
        
        return dto;
    }
}