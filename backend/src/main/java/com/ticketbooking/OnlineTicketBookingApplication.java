package com.ticketbooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OnlineTicketBookingApplication {
    public static void main(String[] args) {
        SpringApplication.run(OnlineTicketBookingApplication.class, args);
    }
}
