package Entity;

import ADT.ListInterface;
import ADT.ArrayList;

public class Customer implements java.io.Serializable {
   private int custId;
    private String custName;
    private String custPhoneNumber;
    private ListInterface<Order> custOrder;
    
    public Customer() {
    }

    public Customer(String custName) {
        this.custName = custName;
    }
    
    public Customer(int custId, String custName, String custPhoneNumber) {
        this.custId = custId;
        this.custName = custName;
        this.custPhoneNumber = custPhoneNumber;
        this.custOrder = new ArrayList<>();
    }

    public Customer(int id) {
        this.custId = id;
        this.custOrder = new ArrayList<>();
    }

    public Customer(int custId, String custName, String custPhoneNumber, ListInterface<Order> order) {
        this.custId = custId;
        this.custName = custName;
        this.custPhoneNumber = custPhoneNumber;
        this.custOrder = order;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustPhoneNumber() {
        return custPhoneNumber;
    }

    public void setCustPhoneNumber(String custPhoneNumber) {
        this.custPhoneNumber = custPhoneNumber;
    }

    public ListInterface<Order> getAllOrder() {
        return custOrder;
    }

    public void addOrderToCust(Order order) {
        custOrder.add(order);
    }
    
    public void modifyOrderFromCust(int position, Order order){
        custOrder.replace(position, order);
    }
    
    public void removeOrderOfCust(int position){
        custOrder.remove(position);
    }
    
    public void clearOrderOfCust(){
        System.out.println(custOrder);
        custOrder.clear();
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

        final Customer other = (Customer) obj;

        if (this.custId != other.custId) {
            return false;
        }

        return true;
    }

    public String toString(){
        return custId + "|" + custName + "|" + custPhoneNumber;
    }
}
