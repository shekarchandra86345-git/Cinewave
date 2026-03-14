package com.ticketbooking.dto;

import lombok.Data;
import java.util.List;
import java.math.BigDecimal;

@Data
public class BookingRequest {
    private Long id;
    private String user;
    private String movie;
    private Long movieId;
    private Long theaterId;
    private List<Integer> seats;
    private BigDecimal total;
    private BigDecimal paid;
    private String type; // 'full' or 'partial'
    private String showTime;
}
