package com.setec.DAO;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostProductDAO {
	private String name;
	private double price;
	private int qty;
	private MultipartFile imageFile;
}
