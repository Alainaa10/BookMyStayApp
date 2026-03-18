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

    static class Service {
        private String name;
        private double cost;

        public Service(String name, double cost) {
            this.name = name;
            this.cost = cost;
        }

        public String getName() {
            return name;
        }

        public double getCost() {
            return cost;
        }

        @Override
        public String toString() {
            return name + " ($" + cost + ")";
        }
    }

    static class AddOnServiceManager {
        private Map<String, List<Service>> reservationServices;

        public AddOnServiceManager() {
            reservationServices = new HashMap<>();
        }

        public void addServices(Reservation res, Service... services) {
            reservationServices.computeIfAbsent(res.getReservationID(), k -> new ArrayList<>());
            reservationServices.get(res.getReservationID()).addAll(Arrays.asList(services));

            System.out.println("Services added for " + res.getGuestName() + ": " + Arrays.toString(services));
        }

        public double calculateTotalCost(Reservation res) {
            List<Service> services = reservationServices.getOrDefault(res.getReservationID(), Collections.emptyList());
            return services.stream().mapToDouble(Service::getCost).sum();
        }
        public void displayServices() {
            System.out.println("\n=== Reservation Add-On Services ===");
            for (Map.Entry<String, List<Service>> entry : reservationServices.entrySet()) {
                System.out.println("Reservation ID: " + entry.getKey() + " -> " + entry.getValue());
            }
        }
    }
    public static void main(String[] args) {
        System.out.println("Welcome to Book My Stay App");
        System.out.println("Add-On Service Selection v7.1\n");

        Reservation res1 = new Reservation("R-101", "Alice", "Single Room");
        Reservation res2 = new Reservation("R-102", "Bob", "Double Room");


        AddOnServiceManager serviceManager = new AddOnServiceManager();

        Service breakfast = new Service("Breakfast", 15.0);
        Service airportPickup = new Service("Airport Pickup", 25.0);
        Service spa = new Service("Spa Session", 50.0);

        serviceManager.addServices(res1, breakfast, spa);
        serviceManager.addServices(res2, breakfast, airportPickup);

        serviceManager.displayServices();

        System.out.println("\nTotal add-on cost:");
        System.out.println(res1.getGuestName() + ": $" + serviceManager.calculateTotalCost(res1));
        System.out.println(res2.getGuestName() + ": $" + serviceManager.calculateTotalCost(res2));

        System.out.println("\nAdd-on services assigned successfully. Core booking and inventory remain unchanged.");
    }
}