// CustomerOrderRepository.java
package com.practice.myappvs;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {
    // You can add custom query methods if needed
}
