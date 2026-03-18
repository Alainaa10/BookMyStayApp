import java.util.LinkedList;
import java.util.Queue;

public class BookMyStayApp {

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

        public Reservation peekNext() {
            return queue.peek();
        }

        public Reservation processNext() {
            return queue.poll();
        }
        public void displayQueue() {
            if(queue.isEmpty()) {
                System.out.println("No booking requests in the queue.");
                return;
            }
            System.out.println("\nCurrent Booking Queue:");
            for(Reservation res : queue) {
                System.out.println(res.getGuestName() + " -> " + res.getRoomType());
            }
        }

        public boolean isEmpty() {
            return queue.isEmpty();
        }
    }

    public static void main(String[] args) {
        System.out.println("Welcome to Book My Stay App");
        System.out.println("Booking Request Queue Simulation v5.1\n");


        BookingQueue bookingQueue = new BookingQueue();

        bookingQueue.submitRequest(new Reservation("Alice", "Single Room"));
        bookingQueue.submitRequest(new Reservation("Bob", "Double Room"));
        bookingQueue.submitRequest(new Reservation("Charlie", "Suite Room"));
        bookingQueue.submitRequest(new Reservation("Diana", "Single Room"));


        bookingQueue.displayQueue();

        System.out.println("\nProcessing requests in order (simulation)...");
        while(!bookingQueue.isEmpty()) {
            Reservation next = bookingQueue.processNext();
            System.out.println("Next request: " + next.getGuestName() + " -> " + next.getRoomType());
        }

        bookingQueue.displayQueue();

        System.out.println("\nAll booking requests have been processed (queue is now empty).");
        System.out.println("Thank you for using Book My Stay!");
    }
}