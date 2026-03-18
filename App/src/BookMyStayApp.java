import java.util.*;

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
        private Map<String, Integer> availableRooms;

        public Inventory() {
            availableRooms = new HashMap<>();
            availableRooms.put("Single Room", 2);
            availableRooms.put("Double Room", 2);
            availableRooms.put("Suite Room", 1);
        }

        public void bookRoom(String roomType) {
            int count = availableRooms.getOrDefault(roomType, 0);
            if (count <= 0) {
                throw new RuntimeException("No available rooms for type: " + roomType);
            }
            availableRooms.put(roomType, count - 1);
        }

        public void cancelRoom(String roomType) {
            availableRooms.put(roomType, availableRooms.getOrDefault(roomType, 0) + 1);
        }

        public void displayAvailability() {
            System.out.println("\n=== Room Availability ===");
            for (Map.Entry<String, Integer> entry : availableRooms.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue() + " available");
            }
        }
    }
    static class BookingHistory {
        private Map<String, Reservation> confirmedBookings = new HashMap<>();

        public void addReservation(Reservation res) {
            confirmedBookings.put(res.getReservationID(), res);
            System.out.println("Booking confirmed: " + res);
        }

        public Reservation removeReservation(String reservationID) {
            return confirmedBookings.remove(reservationID);
        }

        public boolean exists(String reservationID) {
            return confirmedBookings.containsKey(reservationID);
        }

        public void displayAll() {
            System.out.println("\n=== Booking History ===");
            for (Reservation res : confirmedBookings.values()) {
                System.out.println(res);
            }
        }
    }
    static class CancellationService {
        private Inventory inventory;
        private BookingHistory history;
        private Stack<String> rollbackStack = new Stack<>();

        public CancellationService(Inventory inventory, BookingHistory history) {
            this.inventory = inventory;
            this.history = history;
        }

        public void cancelBooking(String reservationID) {
            if (!history.exists(reservationID)) {
                System.out.println("Cannot cancel: Reservation " + reservationID + " does not exist.");
                return;
            }
            Reservation res = history.removeReservation(reservationID);
            inventory.cancelRoom(res.getRoomType());
            rollbackStack.push(res.getReservationID());
            System.out.println("Booking cancelled successfully: " + res);
        }

        public void rollbackLastCancellation() {
            if (rollbackStack.isEmpty()) {
                System.out.println("No cancellations to rollback.");
                return;
            }
            String reservationID = rollbackStack.pop();
            System.out.println("Rollback performed for cancelled reservation: " + reservationID);
        }
    }
    public static void main(String[] args) {
        System.out.println("Welcome to Book My Stay App");
        System.out.println("Booking Cancellation & Inventory Rollback v10.1\n");

        Inventory inventory = new Inventory();
        BookingHistory history = new BookingHistory();
        CancellationService cancelService = new CancellationService(inventory, history);

        Reservation r1 = new Reservation("R-101", "Alice", "Single Room");
        Reservation r2 = new Reservation("R-102", "Bob", "Double Room");
        Reservation r3 = new Reservation("R-103", "Charlie", "Suite Room");

        inventory.bookRoom(r1.getRoomType());
        inventory.bookRoom(r2.getRoomType());
        inventory.bookRoom(r3.getRoomType());

        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);

        inventory.displayAvailability();

        System.out.println("\n--- Cancelling R-102 ---");
        cancelService.cancelBooking("R-102");
        inventory.displayAvailability();
        history.displayAll();

        System.out.println("\n--- Attempting invalid cancellation R-999 ---");
        cancelService.cancelBooking("R-999");

        System.out.println("\n--- Rolling back last cancellation ---");
        cancelService.rollbackLastCancellation();
    }
}