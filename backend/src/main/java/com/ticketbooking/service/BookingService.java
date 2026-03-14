package com.ticketbooking.service;

import com.ticketbooking.dto.BookingRequest;
import com.ticketbooking.model.*;
import com.ticketbooking.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final TheaterRepository theaterRepository;
    private final ShowRepository showRepository;
    private final SeatRepository seatRepository;

    @Transactional
    public void processBooking(BookingRequest request) {
        // Find or create User
        User user = userRepository.findByUsername(request.getUser())
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setUsername(request.getUser());
                    newUser.setEmail(request.getUser() + "@example.com"); // Mock email
                    newUser.setPassword("password"); // Mock password
                    newUser.setRole(User.Role.USER);
                    return userRepository.save(newUser);
                });

        // Find current movie & theater
        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        Theater theater = theaterRepository.findById(request.getTheaterId())
                .orElseThrow(() -> new RuntimeException("Theater not found"));

        // Create mock show for this booking since frontend doesn't use Show IDs yet
        Show show = new Show();
        show.setMovie(movie);
        show.setTheater(theater);
        show.setBasePrice(request.getTotal().divide(new java.math.BigDecimal(request.getSeats().size())));
        show.setShowTime(LocalDateTime.now().plusDays(1)); // mock time
        show = showRepository.save(show);

        // Map frontend seat array to Seat entities
        List<Seat> seats = new ArrayList<>();
        for (Integer seatIndex : request.getSeats()) {
            Seat seat = new Seat();
            seat.setTheater(theater);
            seat.setColNum(seatIndex);
            seat.setRowNum("A"); // Mock row
            seat.setType(Seat.SeatType.REGULAR);
            seats.add(seatRepository.save(seat));
        }

        // Create Booking
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setShow(show);
        booking.setTotalAmount(request.getTotal());
        booking.setAdvancePaid(request.getPaid());
        booking.setSeats(seats);

        if ("partial".equals(request.getType())) {
            booking.setBookingType(Booking.BookingType.FREEZE);
            booking.setStatus(Booking.BookingStatus.FROZEN);
            booking.setFreezeDeadline(LocalDateTime.now().plusMinutes(30)); 
        } else {
            booking.setBookingType(Booking.BookingType.FULL);
            booking.setStatus(Booking.BookingStatus.CONFIRMED);
        }

        bookingRepository.save(booking);
    }

    public List<BookingRequest> getAllBookings() {
        return bookingRepository.findAll().stream().map(b -> {
            BookingRequest req = new BookingRequest();
            req.setId(b.getId());
            req.setUser(b.getUser().getUsername());
            req.setMovie(b.getShow().getMovie().getTitle());
            req.setMovieId(b.getShow().getMovie().getId());
            req.setTheaterId(b.getShow().getTheater().getId());
            req.setSeats(b.getSeats().stream().map(Seat::getColNum).collect(Collectors.toList()));
            req.setTotal(b.getTotalAmount());
            req.setPaid(b.getAdvancePaid());
            req.setType(b.getBookingType() == Booking.BookingType.FREEZE ? "partial" : "full");
            
            // Format time properly to avoid error on null
            if (b.getShow() != null && b.getShow().getShowTime() != null) {
                req.setShowTime(b.getShow().getShowTime().toString());
            } else {
                req.setShowTime("7:30 PM"); // fallback
            }
            return req;
        }).collect(Collectors.toList());
    }

    @Transactional
    public void cancelBooking(Long bookingId) {
        bookingRepository.findById(bookingId).ifPresent(booking -> {
            List<Seat> seatsToDelete = new ArrayList<>(booking.getSeats());
            booking.getSeats().clear();
            bookingRepository.save(booking); // Unlink many-to-many
            seatRepository.deleteAll(seatsToDelete); // Delete actual seats
            bookingRepository.delete(booking);
        });
    }

    /**
     * Runs every minute to unfreeze tickets that have passed their deadline.
     */
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void processExpiredFreezes() {
        List<Booking> expiredBookings = bookingRepository.findByStatusAndFreezeDeadlineBefore(
            Booking.BookingStatus.FROZEN, LocalDateTime.now());
        
        for (Booking booking : expiredBookings) {
            booking.setStatus(Booking.BookingStatus.CANCELLED);
            bookingRepository.save(booking);
            System.out.println("Booking " + booking.getId() + " was automatically un-frozen due to deadline expiry.");
        }
    }
}
