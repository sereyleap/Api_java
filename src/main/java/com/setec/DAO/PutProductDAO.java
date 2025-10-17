package com.setec.DAO;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PutProductDAO {

    private Integer id;
    private String name;
    private Double price;
    private int qty;
    private MultipartFile imageFile;
}
