import java.util.*;

public class BookMyStayApp {

    static class InvalidBookingException extends Exception {
        public InvalidBookingException(String message) {
            super(message);
        }
    }
    static class Reservation {
        private String reservationID;
        private String guestName;
        private String roomType;

        public Reservation(String reservationID, String guestName, String roomType) throws InvalidBookingException {
            if (guestName == null || guestName.isBlank()) {
                throw new InvalidBookingException("Guest name cannot be empty.");
            }
            if (!List.of("Single Room", "Double Room", "Suite Room").contains(roomType)) {
                throw new InvalidBookingException("Invalid room type: " + roomType);
            }

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

        public void bookRoom(Reservation res) throws InvalidBookingException {
            int count = availableRooms.getOrDefault(res.getRoomType(), 0);
            if (count <= 0) {
                throw new InvalidBookingException("No available rooms for type: " + res.getRoomType());
            }
            availableRooms.put(res.getRoomType(), count - 1);
            System.out.println("Room booked successfully: " + res);
        }

        public void displayAvailability() {
            System.out.println("\n=== Room Availability ===");
            for (Map.Entry<String, Integer> entry : availableRooms.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue() + " available");
            }
        }
    }
    public static void main(String[] args) {
        System.out.println("Welcome to Book My Stay App");
        System.out.println("Error Handling & Validation v9.1\n");

        Inventory inventory = new Inventory();

        try {
            Reservation r1 = new Reservation("R-101", "Alice", "Single Room");
            Reservation r2 = new Reservation("R-102", "", "Double Room");   // Invalid guest name
            Reservation r3 = new Reservation("R-103", "Charlie", "Penthouse"); // Invalid room type

            inventory.bookRoom(r1);
            inventory.bookRoom(r2);
            inventory.bookRoom(r3);

        } catch (InvalidBookingException e) {
            System.out.println("Booking error: " + e.getMessage());
        }

        inventory.displayAvailability();
        System.out.println("\nSystem remains stable after validation errors.");
    }
}