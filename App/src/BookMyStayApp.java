import java.util.*;
import java.util.concurrent.*;

public class BookMyStayApp {

    static class Reservation {
        private String reservationID;
        private String guestName;
        private String roomType;

        public Reservation(String reservationID, String guestName, String roomType) {
            this.reservationID = reservationID;
            this.guestName = guestName;
            this.roomType = roomType;
        }

        public String getReservationID() { return reservationID; }
        public String getGuestName() { return guestName; }
        public String getRoomType() { return roomType; }

        @Override
        public String toString() {
            return reservationID + " - " + guestName + " (" + roomType + ")";
        }
    }
    static class Inventory {
        private final Map<String, Integer> availableRooms = new HashMap<>();

        public Inventory() {
            availableRooms.put("Single Room", 2);
            availableRooms.put("Double Room", 2);
            availableRooms.put("Suite Room", 1);
        }

        public synchronized boolean bookRoom(String roomType) {
            int count = availableRooms.getOrDefault(roomType, 0);
            if (count <= 0) return false;
            availableRooms.put(roomType, count - 1);
            return true;
        }

        public synchronized void displayAvailability() {
            System.out.println("\n=== Room Availability ===");
            for (Map.Entry<String, Integer> entry : availableRooms.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue() + " available");
            }
        }
    }
    static class BookingHistory {
        private final List<Reservation> confirmedBookings = Collections.synchronizedList(new ArrayList<>());

        public void addReservation(Reservation res) {
            confirmedBookings.add(res);
            System.out.println("Booking confirmed: " + res);
        }

        public void displayAll() {
            System.out.println("\n=== Booking History ===");
            synchronized (confirmedBookings) {
                for (Reservation res : confirmedBookings) {
                    System.out.println(res);
                }
            }
        }
    }
    static class BookingTask implements Runnable {
        private final Inventory inventory;
        private final BookingHistory history;
        private final Reservation reservation;

        public BookingTask(Inventory inventory, BookingHistory history, Reservation reservation) {
            this.inventory = inventory;
            this.history = history;
            this.reservation = reservation;
        }

        @Override
        public void run() {
            if (inventory.bookRoom(reservation.getRoomType())) {
                history.addReservation(reservation);
            } else {
                System.out.println("Failed to book room for: " + reservation);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Welcome to Book My Stay App");
        System.out.println("Concurrent Booking Simulation v11.1\n");

        Inventory inventory = new Inventory();
        BookingHistory history = new BookingHistory();

        // Simulate multiple guest booking requests
        Reservation[] requests = {
                new Reservation("R-101", "Alice", "Single Room"),
                new Reservation("R-102", "Bob", "Double Room"),
                new Reservation("R-103", "Charlie", "Single Room"),
                new Reservation("R-104", "Diana", "Suite Room"),
                new Reservation("R-105", "Eve", "Single Room"),
        };

        ExecutorService executor = Executors.newFixedThreadPool(3);

        for (Reservation r : requests) {
            executor.submit(new BookingTask(inventory, history, r));
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        inventory.displayAvailability();
        history.displayAll();
    }
}