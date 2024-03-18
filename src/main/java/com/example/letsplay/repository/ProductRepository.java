package com.example.letsplay.repository;

import com.example.letsplay.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByUserId(String userId);
    Optional<Product> findByName(String name);
}
