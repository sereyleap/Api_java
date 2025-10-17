package com.setec.entities;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "tbl_product")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

	//http://localhost:8080/swagger-ui/index.html#/my-controller/getAll
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private double price;
    private int qty;
    @JsonIgnore
    private String imageUrl;

    public double getAmount() {
        return price * qty;
    }
    
    public String getFullImageUrl() {
    		return ServletUriComponentsBuilder.fromCurrentContextPath().build().toString()+imageUrl;
    }
}
