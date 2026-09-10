
import java.util.ArrayList;
import java.util.List;

// ============================================================
// PRODUCT
// ============================================================

class Product {

    private String name;
    private double price;
    private ProductType type;
    private Size size;

    public Product(String name, double price,
                   ProductType type, Size size) {

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name is invalid");
        }

        if (price < 0) {
            throw new IllegalArgumentException("Product price is invalid");
        }

        if (type == null) {
            throw new IllegalArgumentException("Product type is invalid");
        }

        if (size == null) {
            throw new IllegalArgumentException("Product size is invalid");
        }

        this.name = name;
        this.price = price;
        this.type = type;
        this.size = size;
    }

    // Encapsulation - Getters

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public ProductType getType() {
        return type;
    }

    public Size getSize() {
        return size;
    }
}


// ============================================================
// ENUMS
// ============================================================

enum ProductType {
    FOOD,
    CLOTHES,
    TECH
}

enum Size {
    SMALL,
    MEDIUM,
    LARGE
}

enum Zone {
    CAIRO,
    GIZA,
    ALEXANDRIA,
    Demietta
}


// ============================================================
// ABSTRACT DELIVERY
// ============================================================

abstract class Delivery {

    // Encapsulation
    private String customerName;
    private Zone zone;
    private double weight;

    public Delivery(String customerName,
                    Zone zone,
                    double weight) {

        if (customerName == null ||
            customerName.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Customer name cannot be empty"
            );
        }

        if (zone == null) {
            throw new IllegalArgumentException(
                    "Delivery zone cannot be null"
            );
        }

        if (weight <= 0) {
            throw new IllegalArgumentException(
                    "Weight must be greater than zero"
            );
        }

        this.customerName = customerName;
        this.zone = zone;
        this.weight = weight;
    }

    // Getters

    public String getCustomerName() {
        return customerName;
    }

    public Zone getZone() {
        return zone;
    }

    public double getWeight() {
        return weight;
    }

    // ========================================================
    // COMMON SHIPPING CALCULATION
    // ========================================================

    protected double calculateBasicPrice() {

        double zonePrice;

        switch (zone) {

            case CAIRO:
                zonePrice = 20;
                break;

            case GIZA:
                zonePrice = 25;
                break;

            case ALEXANDRIA:
                zonePrice = 40;
                break;
            case Demietta:
                zonePrice = 50;
                break;

            default:
                // Invalid zone should never return a wrong price
                throw new IllegalArgumentException(
                        "Invalid delivery zone"
                );
        }

        double weightPrice;

        if (weight <= 2) {

            weightPrice = 0;

        } else if (weight <= 5) {

            weightPrice = 10;

        } else {

            weightPrice = 20;
        }

        return zonePrice + weightPrice;
    }

    // ========================================================
    // ABSTRACTION
    // ========================================================

    public abstract double calculatePrice();
}


// ============================================================
// FOOD DELIVERY
// ============================================================

class FoodDelivery extends Delivery {

    private static final double FOOD_HANDLING_FEE = 10;

    public FoodDelivery(String customerName,
                        Zone zone,
                        double weight) {

        super(customerName, zone, weight);
    }

    // Polymorphism

    @Override
    public double calculatePrice() {

        return calculateBasicPrice()
                + FOOD_HANDLING_FEE;
    }
}


// ============================================================
// CLOTHES DELIVERY
// ============================================================

class ClothesDelivery extends Delivery {

    public ClothesDelivery(String customerName,
                           Zone zone,
                           double weight) {

        super(customerName, zone, weight);
    }

    // Polymorphism

    @Override
    public double calculatePrice() {

        return calculateBasicPrice();
    }
}


// ============================================================
// TECH DELIVERY
// ============================================================

class TechDelivery extends Delivery {

    private static final double TECH_INSURANCE_FEE = 15;

    public TechDelivery(String customerName,
                        Zone zone,
                        double weight) {

        super(customerName, zone, weight);
    }

    // Polymorphism

    @Override
    public double calculatePrice() {

        // Basic shipping + Electronics insurance
        return calculateBasicPrice()
                + TECH_INSURANCE_FEE;
    }
}


// ============================================================
// CART
// ============================================================

class Cart {

    private List<Product> products;

    public Cart() {

        products = new ArrayList<>();
    }

    // ========================================================
    // Add Product
    // ========================================================

    public void addProduct(Product product) {

        if (product == null) {
            throw new IllegalArgumentException(
                    "Cannot add null product"
            );
        }

        products.add(product);
    }

    // ========================================================
    // Get Products
    // ========================================================

    public List<Product> getProducts() {

        return products;
    }

    // ========================================================
    // Check Product Type
    // ========================================================

    public boolean containsType(ProductType type) {

        for (Product product : products) {

            if (product.getType() == type) {
                return true;
            }
        }

        return false;
    }

    // ========================================================
    // Calculate Products Total
    // ========================================================

    public double calculateProductsTotal() {

        double total = 0;

        for (Product product : products) {

            total += product.getPrice();
        }

        return total;
    }

    // ========================================================
    // Calculate Total Weight
    // ========================================================

    public double calculateTotalWeight() {

        double totalWeight = 0;

        for (Product product : products) {

            switch (product.getSize()) {

                case SMALL:
                    totalWeight += 1;
                    break;

                case MEDIUM:
                    totalWeight += 2;
                    break;

                case LARGE:
                    totalWeight += 4;
                    break;

                default:
                    // Do not continue with incorrect price
                    throw new IllegalArgumentException(
                            "Invalid product size"
                    );
            }
        }

        return totalWeight;
    }

    // ========================================================
    // AUTOMATIC DELIVERY SELECTION
    // ========================================================

    public Delivery createDelivery(String customerName,
                                   Zone zone) {

        if (products.isEmpty()) {

            throw new IllegalStateException(
                    "Cannot create delivery for an empty cart"
            );
        }

        double totalWeight =
                calculateTotalWeight();

        /*
         * DELIVERY PRIORITY
         *
         * 1. FOOD
         * 2. TECH
         * 3. CLOTHES
         *
         * Example:
         * Food + Clothes
         * => FoodDelivery
         *
         * Food + Tech
         * => FoodDelivery
         *
         * Tech + Clothes
         * => TechDelivery
         */

        if (containsType(ProductType.FOOD)) {

            return new FoodDelivery(
                    customerName,
                    zone,
                    totalWeight
            );
        }

        if (containsType(ProductType.TECH)) {

            return new TechDelivery(
                    customerName,
                    zone,
                    totalWeight
            );
        }

        if (containsType(ProductType.CLOTHES)) {

            return new ClothesDelivery(
                    customerName,
                    zone,
                    totalWeight
            );
        }

        throw new IllegalStateException(
                "No supported product type found"
        );
    }
}


// ============================================================
// RECEIPT GENERATOR
// ============================================================

class ReceiptGenerator {

    /*
     * Important:
     * Printing is ONLY here.
     *
     * Delivery classes do not contain System.out.println().
     */

    public static void printReceipt(Cart cart,
                                    Delivery delivery) {

        double productsTotal =
                cart.calculateProductsTotal();

        double shippingPrice =
                delivery.calculatePrice();

        double finalTotal =
                productsTotal + shippingPrice;

        System.out.println();
        System.out.println("========================================");
        System.out.println("           DELIVERY RECEIPT");
        System.out.println("========================================");

        System.out.println(
                "Customer: "
                + delivery.getCustomerName()
        );

        System.out.println(
                "Zone: "
                + delivery.getZone()
        );

        System.out.println(
                "Weight: "
                + delivery.getWeight()
                + " kg"
        );

        System.out.println("----------------------------------------");
        System.out.println("Products:");

        for (Product product : cart.getProducts()) {

            System.out.println(
                    "- "
                    + product.getName()
                    + " | "
                    + product.getPrice()
                    + " EGP"
            );
        }

        System.out.println("----------------------------------------");

        System.out.println(
                "Products Total: "
                + productsTotal
                + " EGP"
        );

        System.out.println(
                "Shipping Fee: "
                + shippingPrice
                + " EGP"
        );

        System.out.println("----------------------------------------");

        System.out.println(
                "FINAL TOTAL: "
                + finalTotal
                + " EGP"
        );

        System.out.println("========================================");
    }
}


// ============================================================
// MAIN
// ============================================================

public class Main {

    public static void main(String[] args) {

        try {

            // ==================================================
            // 1. Create Cart
            // ==================================================

            Cart cart = new Cart();


            // ==================================================
            // 2. Add Products
            // ==================================================

            cart.addProduct(
                    new Product(
                            "Chicken Meal",
                            150,
                            ProductType.FOOD,
                            Size.MEDIUM
                    )
            );

            cart.addProduct(
                    new Product(
                            "T-Shirt",
                            300,
                            ProductType.CLOTHES,
                            Size.SMALL
                    )
            );


            // ==================================================
            // 3. SYSTEM AUTOMATICALLY SELECTS DELIVERY
            // ==================================================

            Delivery delivery =
                    cart.createDelivery(
                            "Ahmed",
                            Zone.CAIRO
                    );


            // ==================================================
            // 4. Generate Receipt
            // ==================================================

            ReceiptGenerator.printReceipt(
                    cart,
                    delivery
            );

        } catch (IllegalArgumentException e) {

            System.out.println(
                    "Input Error: " + e.getMessage()
            );

        } catch (IllegalStateException e) {

            System.out.println(
                    "System Error: " + e.getMessage()
            );
        }
    }
}
```