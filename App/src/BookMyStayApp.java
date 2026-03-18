import java.util.HashMap;
import java.util.Map;

public class BookMyStayApp {

    static class Room {
        private String roomType;
        private int numberOfBeds;
        private double pricePerNight;

        public Room(String roomType, int numberOfBeds, double pricePerNight) {
            this.roomType = roomType;
            this.numberOfBeds = numberOfBeds;
            this.pricePerNight = pricePerNight;
        }

        public void displayRoomDetails() {
            System.out.println("Room Type: " + roomType);
            System.out.println("Beds: " + numberOfBeds);
            System.out.println("Price per night: ₹" + pricePerNight);
        }

        public String getRoomType() {
            return roomType;
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

        public void displayInventory() {
            System.out.println("=== Room Inventory ===");
            for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
                System.out.println(entry.getKey() + " -> Available: " + entry.getValue());
            }
        }
    }

    static class RoomSearchService {
        private RoomInventory inventory;
        private Room[] rooms;

        public RoomSearchService(RoomInventory inventory, Room[] rooms) {
            this.inventory = inventory;
            this.rooms = rooms;
        }

        public void searchAvailableRooms() {
            System.out.println("\n=== Available Rooms ===");
            boolean anyAvailable = false;
            for (Room room : rooms) {
                int available = inventory.getAvailability(room.getRoomType());
                if (available > 0) {
                    room.displayRoomDetails();
                    System.out.println("Available: " + available + "\n");
                    anyAvailable = true;
                }
            }
            if (!anyAvailable) {
                System.out.println("No rooms available at the moment.");
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Welcome to Book My Stay App");
        System.out.println("Hotel Booking System v4.1\n");

        RoomInventory inventory = new RoomInventory();

        Room[] rooms = {
                new Room("Single Room", 1, 2000),
                new Room("Double Room", 2, 3500),
                new Room("Suite Room", 3, 6000)
        };

        RoomSearchService searchService = new RoomSearchService(inventory, rooms);

        searchService.searchAvailableRooms();

        System.out.println("Thank you for using Book My Stay!");
    }
}