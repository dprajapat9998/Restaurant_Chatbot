package com.practice.myappvs;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByItemName(String itemName);
    List<Menu> findAll(); // Add this method to retrieve all menu items
}
