package com.showtime.repository;

import com.showtime.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserIdOrderByBookedAtDesc(Long userId);
    
    List<Booking> findByUserIdAndBookingStatusOrderByBookedAtDesc(Long userId, String bookingStatus);
    
    Optional<Booking> findByBookingReference(String bookingReference);
    
    @Query("SELECT b FROM Booking b WHERE b.show.showDate >= CURRENT_DATE " +
           "AND b.bookingStatus = 'CONFIRMED' AND b.user.id = :userId")
    List<Booking> findUpcomingBookings(@Param("userId") Long userId);
    
    @Query("SELECT b FROM Booking b WHERE b.show.showDate < CURRENT_DATE " +
           "AND b.bookingStatus = 'CONFIRMED' AND b.user.id = :userId")
    List<Booking> findPastBookings(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.bookingStatus = 'CONFIRMED'")
    Long countConfirmedBookings();
    
    @Query("SELECT SUM(b.totalAmount) FROM Booking b WHERE b.bookingStatus = 'CONFIRMED' " +
           "AND b.paymentStatus = 'COMPLETED'")
    Double getTotalRevenue();
    
    @Query("SELECT FUNCTION('DATE_FORMAT', b.bookedAt, '%Y-%m') as month, " +
           "SUM(b.totalAmount) as revenue FROM Booking b " +
           "WHERE b.bookedAt >= :startDate AND b.bookingStatus = 'CONFIRMED' " +
           "AND b.paymentStatus = 'COMPLETED' " +
           "GROUP BY FUNCTION('DATE_FORMAT', b.bookedAt, '%Y-%m') " +
           "ORDER BY month")
    List<Object[]> getMonthlyRevenue(@Param("startDate") LocalDateTime startDate);
}