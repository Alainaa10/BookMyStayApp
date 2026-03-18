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

        public String getReservationID() {
            return reservationID;
        }

        public String getGuestName() {
            return guestName;
        }

        public String getRoomType() {
            return roomType;
        }

        @Override
        public String toString() {
            return reservationID + " - " + guestName + " (" + roomType + ")";
        }
    }

    static class BookingHistory {
        private List<Reservation> confirmedBookings;

        public BookingHistory() {
            confirmedBookings = new ArrayList<>();
        }
        public void addReservation(Reservation res) {
            confirmedBookings.add(res);
            System.out.println("Booking added to history: " + res);
        }
        public List<Reservation> getAllBookings() {
            return Collections.unmodifiableList(confirmedBookings);
        }
    }
    static class BookingReportService {
        private BookingHistory history;

        public BookingReportService(BookingHistory history) {
            this.history = history;
        }
        public void generateReport() {
            System.out.println("\n=== Booking Report ===");
            if (history.getAllBookings().isEmpty()) {
                System.out.println("No bookings yet.");
                return;
            }
            for (Reservation res : history.getAllBookings()) {
                System.out.println(res);
            }
        }
        public void generateSummary() {
            System.out.println("\n=== Booking Summary ===");
            Map<String, Integer> summary = new HashMap<>();
            for (Reservation res : history.getAllBookings()) {
                summary.put(res.getRoomType(), summary.getOrDefault(res.getRoomType(), 0) + 1);
            }
            for (Map.Entry<String, Integer> entry : summary.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue() + " bookings");
            }
        }
    }
    public static void main(String[] args) {
        System.out.println("Welcome to Book My Stay App");
        System.out.println("Booking History & Reporting v8.1\n");

        BookingHistory history = new BookingHistory();

        Reservation res1 = new Reservation("R-101", "Alice", "Single Room");
        Reservation res2 = new Reservation("R-102", "Bob", "Double Room");
        Reservation res3 = new Reservation("R-103", "Charlie", "Suite Room");

        history.addReservation(res1);
        history.addReservation(res2);
        history.addReservation(res3);

        BookingReportService reportService = new BookingReportService(history);

        reportService.generateReport();
        reportService.generateSummary();

        System.out.println("\nAll booking data tracked successfully in memory.");
    }
}