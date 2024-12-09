package Entity;

public class Food {
    private String foodId;
    private String foodName;
    private double unitPrice;

    public Food(){
    }
        
    public Food(String id){
        this.foodId = id;
    }
    
    public Food(String foodId, String foodName, double price){
        this.foodId = foodId;
        this.foodName = foodName;
        this.unitPrice = price;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
    @Override
    public String toString(){
        return foodId + "|" + foodName + "|" + unitPrice;      
    }
}
