package service;
import java.util.List;
import interfaces.Shippable;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ShippingService {
    private static ShippingService instance;
    private final Queue<List<Shippable>> shipmentQueue;

    private ShippingService() {
        shipmentQueue = new LinkedList<>();
    }

    public static synchronized ShippingService getInstance() {
        if (instance == null) {
            instance = new ShippingService();
        }
        return instance;
    }

    public void queueOrder(List<Shippable> items) {
        if (items != null && !items.isEmpty()) {
            shipmentQueue.add(items);
            System.out.println("Added order to shipping queue.");
        }
    }

    // Process all queued shipments
    public void shipAll() {
        System.out.println("\n🚚 Shipping All Queued Orders:");

        int count = 1;
        while (!shipmentQueue.isEmpty()) {
            List<Shippable> order = shipmentQueue.poll();
            System.out.println("Order #" + count++);
            for (Shippable item : order) {
                System.out.println("- " + item.getName() + " | Weight: " + item.getWeight()+ " KG");
            }
        }

        System.out.println("✅ All orders shipped.\n");
    }
}
