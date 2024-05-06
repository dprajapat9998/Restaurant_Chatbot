package com.practice.myappvs;


import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final CustomerOrderRepository customerOrderRepository;

    public MenuServiceImpl(MenuRepository menuRepository, CustomerOrderRepository customerOrderRepository) {
        this.menuRepository = menuRepository;
        this.customerOrderRepository = customerOrderRepository;
    }
     @Override
    public void clearCustomerOrders() {
        customerOrderRepository.deleteAll();
    }

    @Override
    public List<Menu> getMenuItems() {
        return menuRepository.findAll();
    }

    @Override
    public Menu getMenuItemByName(String itemName) {
        Optional<Menu> optionalMenu = menuRepository.findByItemName(itemName).stream().findFirst();
        return optionalMenu.orElse(null);
    }

    @Override
    public void placeOrder(String itemName) {
        Menu menuItem = getMenuItemByName(itemName);
        if (menuItem != null) {
            CustomerOrder customerOrder = new CustomerOrder();
            customerOrder.setItemName(menuItem.getItemName());
            customerOrder.setPrice(menuItem.getPrice());
            customerOrderRepository.save(customerOrder);
        }
    }

    @Override
    public List<CustomerOrder> getCustomerOrders() {
        return customerOrderRepository.findAll();
    }

    // Add other methods if needed
}
