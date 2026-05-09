package com.flashsale.orderservice.repository;

import com.flashsale.orderservice.model.UserCart;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends CrudRepository<UserCart, String> {
}
