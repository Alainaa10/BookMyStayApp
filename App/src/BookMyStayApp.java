import java.util.HashMap;
import java.util.Map;
public class BookMyStayApp {

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

        public void updateAvailability(String roomType, int count) {
            if (inventory.containsKey(roomType)) {
                inventory.put(roomType, inventory.get(roomType) + count);
            } else {
                System.out.println("Room type not found!");
            }
        }

        public void displayInventory() {
            System.out.println("=== Room Inventory ===");
            for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
                System.out.println(entry.getKey() + " -> Available: " + entry.getValue());
            }
        }
    }

    public static void main(String[] args) {

        System.out.println("Welcome to Book My Stay App");
        System.out.println("Hotel Booking System v3.1\n");

        RoomInventory inventory = new RoomInventory();

        inventory.displayInventory();

        System.out.println("\nUpdating availability...\n");
        inventory.updateAvailability("Single Room", -1);
        inventory.updateAvailability("Suite Room", +1);

        inventory.displayInventory();

        System.out.println("\nThank you for using Book My Stay!");
    }
}