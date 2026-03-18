import java.util.*;


public class BookMyStayApp {

    static class Room {
        private String roomType;
        private double pricePerNight;

        public Room(String roomType, double pricePerNight) {
            this.roomType = roomType;
            this.pricePerNight = pricePerNight;
        }

        public String getRoomType() {
            return roomType;
        }

        public double getPrice() {
            return pricePerNight;
        }
    }
    static class RoomInventory {
        private Map<String, Integer> inventory;

        public RoomInventory() {
            inventory = new HashMap<>();
            inventory.put("Single Room", 5);
            inventory.put("Double Room", 3);
            inventory.put("Suite Room", 2);
        }

        public int getAvailability(String roomType) {
            return inventory.getOrDefault(roomType, 0);
        }

        public boolean allocateRoom(String roomType) {
            int available = getAvailability(roomType);
            if (available > 0) {
                inventory.put(roomType, available - 1);
                return true;
            }
            return false;
        }

        public void displayInventory() {
            System.out.println("=== Current Inventory ===");
            for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
                System.out.println(entry.getKey() + " -> Available: " + entry.getValue());
            }
        }
    }

    static class Reservation {
        private String guestName;
        private String roomType;

        public Reservation(String guestName, String roomType) {
            this.guestName = guestName;
            this.roomType = roomType;
        }

        public String getGuestName() {
            return guestName;
        }

        public String getRoomType() {
            return roomType;
        }
    }
    static class BookingQueue {
        private Queue<Reservation> queue;

        public BookingQueue() {
            queue = new LinkedList<>();
        }

        public void submitRequest(Reservation res) {
            queue.add(res);
            System.out.println("Booking request submitted by " + res.getGuestName() + " for " + res.getRoomType());
        }

        public Reservation pollRequest() {
            return queue.poll();
        }

        public boolean isEmpty() {
            return queue.isEmpty();
        }
    }

    static class BookingService {
        private RoomInventory inventory;
        private Map<String, Set<String>> allocatedRooms;
        private int roomCounter = 1;

        public BookingService(RoomInventory inventory) {
            this.inventory = inventory;
            allocatedRooms = new HashMap<>();
        }

        public void processReservation(Reservation res) {
            String type = res.getRoomType();

            if (inventory.getAvailability(type) <= 0) {
                System.out.println("No available rooms for " + type + ". Cannot allocate for " + res.getGuestName());
                return;
            }
m
            boolean success = inventory.allocateRoom(type);
            if (success) {
                // Generate unique room ID
                String roomID = type.substring(0, 2).toUpperCase() + "-" + roomCounter++;
                allocatedRooms.computeIfAbsent(type, k -> new HashSet<>());
                allocatedRooms.get(type).add(roomID);

                System.out.println("Reservation confirmed for " + res.getGuestName() +
                        ". Assigned Room ID: " + roomID + " (" + type + ")");
            }
        }

        public void displayAllocatedRooms() {
            System.out.println("\n=== Allocated Rooms ===");
            for (Map.Entry<String, Set<String>> entry : allocatedRooms.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Welcome to Book My Stay App");
        System.out.println("Reservation & Room Allocation Service v6.1\n");

        RoomInventory inventory = new RoomInventory();
        BookingQueue bookingQueue = new BookingQueue();

        bookingQueue.submitRequest(new Reservation("Alice", "Single Room"));
        bookingQueue.submitRequest(new Reservation("Bob", "Double Room"));
        bookingQueue.submitRequest(new Reservation("Charlie", "Suite Room"));
        bookingQueue.submitRequest(new Reservation("Diana", "Single Room"));
        bookingQueue.submitRequest(new Reservation("Eve", "Single Room"));
        bookingQueue.submitRequest(new Reservation("Frank", "Suite Room")); // may exceed inventory

        BookingService service = new BookingService(inventory);

        System.out.println("\nProcessing reservations...\n");
        while (!bookingQueue.isEmpty()) {
            Reservation res = bookingQueue.pollRequest();
            service.processReservation(res);
        }
        service.displayAllocatedRooms();
        System.out.println();
        inventory.displayInventory();

        System.out.println("\nAll reservations processed. Thank you for using Book My Stay!");
    }
}