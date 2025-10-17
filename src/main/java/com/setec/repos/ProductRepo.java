package com.setec.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.setec.entities.Product;

public interface ProductRepo extends JpaRepository<Product, Integer> {

	List<Product> findByName(String name);

	Optional<Product> findById(long id);
}
