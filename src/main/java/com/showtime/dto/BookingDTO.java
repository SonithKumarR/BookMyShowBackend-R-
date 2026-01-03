package com.showtime.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class BookingDTO {
    private Long id;
    private String bookingReference;
    private UserDTO user;
    private ShowDTO show;
    private List<String> seatNumbers;
    private String seatType;
    private Integer totalSeats;
    private Double totalAmount;
    private Double convenienceFee;
    private Double taxAmount;
    private String bookingStatus;
    private String paymentStatus;
    private LocalDateTime bookedAt;
    private PaymentDTO payment;
    private List<TicketDTO> tickets;
}
