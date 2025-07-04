package user;

import cart.Cart;
import cart.CartItem;
import interfaces.Expirable;
import interfaces.Shippable;
import model.Product;
import service.ShippingService;

import java.util.List;

public class Customer {
    private double balance;
    private Cart cart;

    public Customer(double balance) {
        this.balance = balance;
        this.cart = new Cart();
    }

    public Cart getCart() {
        return cart;
    }


    public void checkout() {
        if (cart.isEmpty()) throw new RuntimeException("Cart is empty");

        double subtotal = cart.calculateSubtotal();
        double shippingFee = cart.getShippableItems().size() * 10.0; 
        double total = subtotal + shippingFee;

        System.out.println("\n🛒 Checkout Details:");
        for (CartItem item : cart.getItems()) {
            Product p = item.getProduct();

            // Check availability and expiration
            if (!p.isAvailable(item.getQuantity())) {
                throw new RuntimeException(p.getName() + " is out of stock");
            }
            if (p instanceof Expirable && ((Expirable) p).isExpired()) {
                throw new RuntimeException(p.getName() + " is expired");
            }

            // Print product details
            System.out.printf("- %-20s | Qty: %d | Unit Price: $%.2f | Total: $%.2f%n",
                    p.getName(),
                    item.getQuantity(),
                    p.getPrice(),
                    item.getTotalPrice()
            );
        }

        if (balance < total) throw new RuntimeException("Insufficient balance");

        // Reduce quantities
        for (CartItem item : cart.getItems()) {
            item.getProduct().reduceQuantity(item.getQuantity());
        }

        balance -= total;

        System.out.println("\n💳 Payment Summary:");
        System.out.printf("Subtotal: $%.2f%n", subtotal);
        System.out.printf("Shipping: $%.2f%n", shippingFee);
        System.out.printf("Total Paid: $%.2f%n", total);
        System.out.printf("Remaining Balance: $%.2f%n", balance);

        List<Shippable> itemsToShip = cart.getShippableItems();
        if (!itemsToShip.isEmpty()) {
            ShippingService.getInstance().queueOrder(itemsToShip);
        }
        cart.clear(); //  Clear cart after successful checkout
    }
    public void printDetails() {
        System.out.println("\n👤 Customer Details");
        System.out.printf("Balance: $%.2f%n", balance);

        if (cart.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }

        System.out.println("\n🛒 Cart Items:");
        for (CartItem item : cart.getItems()) {
            Product p = item.getProduct();
            System.out.printf("- %-20s | Qty: %d | Unit Price: $%.2f | Total: $%.2f%n",
                    p.getName(),
                    item.getQuantity(),
                    p.getPrice(),
                    item.getTotalPrice()
            );
        }

        double subtotal = cart.calculateSubtotal();
        double shippingFee = cart.getShippableItems().size() * 10.0;
        double total = subtotal + shippingFee;

        System.out.println("\n🧾 Cart Summary:");
        System.out.printf("Subtotal: $%.2f%n", subtotal);
        System.out.printf("Estimated Shipping: $%.2f%n", shippingFee);
        System.out.printf("Total Estimate: $%.2f%n", total);
    }


}