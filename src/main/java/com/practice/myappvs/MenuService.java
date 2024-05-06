package com.practice.myappvs;

import java.util.List;

public interface MenuService {
    List<Menu> getMenuItems();
    Menu getMenuItemByName(String itemName);
    void placeOrder(String itemName);
    List<CustomerOrder> getCustomerOrders();
    
    // Add other methods if needed
    
    void clearCustomerOrders();
}
