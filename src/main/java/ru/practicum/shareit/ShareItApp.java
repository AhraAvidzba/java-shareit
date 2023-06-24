package ru.practicum.shareit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShareItApp {

    public static void main(String[] args) {
        SpringApplication.run(ShareItApp.class, args);

//        LocalDateTime t = LocalDateTime.now();
//        System.out.println(t);

//        List<LocalDateTime> itemBookings = List.of(LocalDateTime.parse("2023-06-27T01:01:25.490803"),
//                LocalDateTime.parse("2023-06-26T01:01:25.490803"),
//                LocalDateTime.parse("2023-06-25T01:01:25.490803"),
//                LocalDateTime.parse("2023-06-24T01:01:25.490803"),
//                LocalDateTime.parse("2023-06-23T01:01:25.490803"));
//        LocalDateTime lastBooking = getLastBooking(itemBookings, LocalDateTime.now());
//        LocalDateTime nextBooking = getNextBooking(itemBookings, LocalDateTime.now());
//        System.out.println(lastBooking);
//        System.out.println(nextBooking);
//    }
//
//
//    private static LocalDateTime getLastBooking(List<LocalDateTime> bookings, LocalDateTime targetDate){
//        List<LocalDateTime> lastBookingList = bookings;
//        for (LocalDateTime date : lastBookingList) {
//            if (date.isAfter(targetDate)) return date;
//        }
//        return null;
//    }
//
//    private static LocalDateTime getNextBooking(List<LocalDateTime> bookings, LocalDateTime targetDate){
//        List<LocalDateTime> nextBookingList = bookings;
//        for (LocalDateTime date : nextBookingList) {
//            if (date.isBefore(targetDate)) return date;
//        }
//        return null;
    }

}
