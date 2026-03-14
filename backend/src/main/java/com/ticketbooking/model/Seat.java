package com.ticketbooking.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "seats")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "theater_id")
    private Theater theater;

    @Column(nullable = false)
    private String rowNum;

    @Column(nullable = false)
    private Integer colNum;

    @Enumerated(EnumType.STRING)
    private SeatType type = SeatType.REGULAR;

    public enum SeatType {
        REGULAR, PREMIUM, VIP
    }
}
