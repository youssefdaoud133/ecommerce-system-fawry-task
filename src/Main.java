import model.Cheese;
import model.MobileScratchCard;
import model.TV;
import service.ShippingService;
import user.Customer;

import java.util.Date;

public class Main {
    public static void main(String[] args) {
        // Create products
        Cheese freshCheese = new Cheese("Fresh Cheese", 5.0, 10, futureDate(), 1.5);
        Cheese expiredCheese = new Cheese("Expired Cheese", 4.0, 5, pastDate(), 1.2);
        TV samsungTV = new TV("Samsung TV", 500.0, 1, 10.0);
        TV lgTV = new TV("LG TV", 450.0, 0, 9.0); // Out of stock
        MobileScratchCard card = new MobileScratchCard("Scratch Card", 2.0, 50);

        // === Customer 1: Successful Checkout ===
        Customer c1 = new Customer(700.0);
        try {
            c1.getCart().addItem(freshCheese, 2);
            c1.getCart().addItem(samsungTV, 1);
            c1.getCart().addItem(card, 10);
            c1.printDetails();
            c1.checkout();
        } catch (RuntimeException e) {
            System.out.println("❌ C1 Checkout failed: " + e.getMessage());
        }

        // Confirm cart is cleared
        c1.printDetails();

        // === Customer 2: Expired Product ===
        Customer c2 = new Customer(300.0);
        try {
            c2.getCart().addItem(expiredCheese, 1);
            c2.checkout();
        } catch (RuntimeException e) {
            System.out.println("❌ C2 Checkout failed (expired product): " + e.getMessage());
        }

        // === Customer 3: Out of Stock ===
        Customer c3 = new Customer(1000.0);
        try {
            c3.getCart().addItem(lgTV, 1); // lgTV quantity = 0
            c3.checkout();
        } catch (RuntimeException e) {
            System.out.println("❌ C3 Checkout failed (out of stock): " + e.getMessage());
        }

        // === Customer 4: Insufficient Balance ===
        Customer c4 = new Customer(100.0);
        try {
            c4.getCart().addItem(samsungTV, 1); // Price = $500
            c4.checkout();
        } catch (RuntimeException e) {
            System.out.println("❌ C4 Checkout failed (insufficient balance): " + e.getMessage());
        }

        // === Customer 5: Just views cart, no checkout ===
        Customer c5 = new Customer(500.0);
        try {
            c5.getCart().addItem(card, 5);
            c5.printDetails();
        } catch (RuntimeException e) {
            System.out.println("❌ C5 Error: " + e.getMessage());
        }

        // === Final: Ship all queued orders ===
        ShippingService.getInstance().shipAll();
    }

    // Helper methods for test dates
    private static Date futureDate() {
        return new Date(System.currentTimeMillis() + 3 * 24 * 60 * 60 * 1000); // +3 days
    }

    private static Date pastDate() {
        return new Date(System.currentTimeMillis() - 3 * 24 * 60 * 60 * 1000); // -3 days
    }
}
