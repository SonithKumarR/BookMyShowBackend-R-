package com.showtime.dto.response;

import java.util.List;
import com.showtime.dto.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    private Long bookingId;
    private String bookingReference;
    private Double totalAmount;
    private String paymentStatus;
    private List<TicketDTO> tickets;
}
