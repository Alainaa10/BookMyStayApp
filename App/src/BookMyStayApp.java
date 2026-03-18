import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class BookMyStayApp {

    static class Reservation implements Serializable {
        private static final long serialVersionUID = 1L;
        private String reservationID;
        private String guestName;
        private String roomType;

        public Reservation(String reservationID, String guestName, String roomType) {
            this.reservationID = reservationID;
            this.guestName = guestName;
            this.roomType = roomType;
        }

        @Override
        public String toString() {
            return reservationID + " - " + guestName + " (" + roomType + ")";
        }
    }

    static class Inventory implements Serializable {
        private static final long serialVersionUID = 1L;
        private Map<String, Integer> availableRooms = new HashMap<>();

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

    static class BookingHistory implements Serializable {
        private static final long serialVersionUID = 1L;
        private List<Reservation> confirmedBookings = new ArrayList<>();

        public synchronized void addReservation(Reservation res) {
            confirmedBookings.add(res);
            System.out.println("Booking confirmed: " + res);
        }

        public synchronized void displayAll() {
            System.out.println("\n=== Booking History ===");
            for (Reservation res : confirmedBookings) {
                System.out.println(res);
            }
        }

        public List<Reservation> getConfirmedBookings() {
            return confirmedBookings;
        }

        public void setConfirmedBookings(List<Reservation> bookings) {
            this.confirmedBookings = bookings;
        }
    }

    static class PersistenceService {
        private static final String FILE_NAME = "booking_system_state.ser";

        public static void saveState(Inventory inventory, BookingHistory history) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
                oos.writeObject(inventory);
                oos.writeObject(history);
                System.out.println("\n[System State Saved Successfully]");
            } catch (IOException e) {
                System.out.println("Error saving system state: " + e.getMessage());
            }
        }

        public static Object[] loadState() {
            File file = new File(FILE_NAME);
            if (!file.exists()) {
                System.out.println("[No saved state found. Starting fresh.]");
                return null;
            }

            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
                Inventory inventory = (Inventory) ois.readObject();
                BookingHistory history = (BookingHistory) ois.readObject();
                System.out.println("[System State Restored Successfully]");
                return new Object[]{inventory, history};
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error restoring system state: " + e.getMessage());
                return null;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Welcome to Book My Stay App");
        System.out.println("Data Persistence & Recovery v12.1\n");

        Inventory inventory;
        BookingHistory history;

        Object[] restored = PersistenceService.loadState();
        if (restored != null) {
            inventory = (Inventory) restored[0];
            history = (BookingHistory) restored[1];
        } else {
            inventory = new Inventory();
            history = new BookingHistory();
        }
        Reservation[] requests = {
                new Reservation("R-201", "Alice", "Single Room"),
                new Reservation("R-202", "Bob", "Double Room"),
                new Reservation("R-203", "Charlie", "Suite Room")
        };

        ExecutorService executor = Executors.newFixedThreadPool(2);
        for (Reservation r : requests) {
            executor.submit(() -> {
                if (inventory.bookRoom(r.roomType)) {
                    history.addReservation(r);
                } else {
                    System.out.println("Failed to book room for: " + r);
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        inventory.displayAvailability();
        history.displayAll();

        PersistenceService.saveState(inventory, history);
    }
}
