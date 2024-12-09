package Entity;

import java.text.DecimalFormat;
import java.time.LocalDate;

public class Order {
    private int orderId;
    private LocalDate orderDate;
    private String foodName;
    private double unitPrice;
    private int quantity;
    
     private static final DecimalFormat df = new DecimalFormat("0.00");

    public Order(String foodName) {
        this.foodName = foodName;
    }

    public Order(){
        orderId = 0;
        orderDate = null;
        foodName = null;
        quantity = 0;
    }
    
    public Order(int id){
        orderId = id;
        orderDate = null;
        foodName = null;
        quantity = 0;
    }
    
    public Order(LocalDate orderDate){
        orderId = 0;
        orderDate = orderDate;
        foodName = null;
        quantity = 0;
    }
    
    public Order(int orderId, LocalDate orderDate, String foodName, double foodPrice, int qty) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.foodName = foodName;
        this.unitPrice = foodPrice;
        this.quantity = qty;
    }
    
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }
    
    public double getUnitPrice(){
        return unitPrice;
    }
    
    public void setUnitPrice(double price){
        this.unitPrice = price;
    }
    
    public void setQuantity(int qty){
        this.quantity = qty;
    }
    
    public int getQuantity(){
        return quantity;
    }

    public String toString(){
        return orderId + "|" + orderDate + "|" +  foodName + "|" + unitPrice  + "|" + quantity + "|" + df.format(unitPrice * quantity);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (obj == null) {
            return false;
        }
        
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        final Order other = (Order) obj;
        
        if (this.orderId != other.orderId) {
            return false;
        }
        return true;
    }
}
