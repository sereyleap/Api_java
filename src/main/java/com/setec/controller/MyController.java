package com.setec.controller;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.setec.DAO.PostProductDAO;
import com.setec.DAO.PutProductDAO;
import com.setec.ProjectApiApplication;
import com.setec.entities.Product;
import com.setec.repos.ProductRepo;

@RestController
@RequestMapping("/api/product")
public class MyController {

    private final ProjectApiApplication projectApiApplication;
	@Autowired
	private ProductRepo productRepo;

    MyController(ProjectApiApplication projectApiApplication) {
        this.projectApiApplication = projectApiApplication;
    }
	
	@GetMapping
	public Object getAll() {
		var products = productRepo.findAll();
		if(products.size()==0) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message","Empty product"));
		}
		else {
			return products;
		}
	}
	@GetMapping("{id}")
	public Object getById(@PathVariable("id") long id) {
		var pro = productRepo.findById(id);
		if(pro.isPresent()) {
			return pro.get();
		}else {
			return ResponseEntity.status(404).body(Map.of("message","product id = "+id+"not found"));
		}
	}
	
	@GetMapping("name/{name}")
	public Object getByName(@PathVariable("name") String name) {
		List<Product> pros = productRepo.findByName(name);
		if(pros.size()==0) {
			return ResponseEntity.status(404).body(Map.of("message","product name ="+name+"not found"));
		}else {
			return pros;
		}
	}
	
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Object postProduct(@ModelAttribute PostProductDAO product)throws Exception {
		var file = product.getImageFile();
		String uploadDir = new File("myapp/static").getAbsolutePath();		
		File dir = new File(uploadDir);
		if(!dir.exists()) {
			dir.mkdirs();
		}
		String extension = Objects.requireNonNull(file.getOriginalFilename());
		String fileName = UUID.randomUUID()+"_"+extension;
		String filePath = Paths.get(uploadDir,fileName).toString();
		file.transferTo(new File(filePath));
		
		Product pro = new Product();
		pro.setName(product.getName());
		pro.setQty(product.getQty());
		pro.setPrice(product.getPrice());
		pro.setImageUrl("/static/"+fileName);
		productRepo.save(pro);
		
		return ResponseEntity.status(201).body(pro);
	}
	
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteById(@PathVariable("id") Integer id) {
	    var p = productRepo.findById(id);
	    if (p.isPresent()) {
	        // Get the product
	        var product = p.get();

	        // Delete image file if it exists
	        File file = new File("myapp/" + product.getImageUrl());
	        if (file.exists()) {
	            file.delete();
	        }

	        // Delete product from database
	        productRepo.delete(product);

	        return ResponseEntity
	                .status(HttpStatus.ACCEPTED)
	                .body(Map.of("Message", "Product id = " + id + " has been deleted"));
	    }

	    return ResponseEntity
	            .status(HttpStatus.NOT_FOUND)
	            .body(Map.of("Message", "Product id = " + id + " not found"));
	}
	
	
	@PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Object putProduct(@ModelAttribute PutProductDAO product)throws Exception{
	
		Integer id = product.getId();
		var p = productRepo.findById(id);
		if(p.isPresent()) {
			var update = p.get();
			update.setName(product.getName());
			update.setPrice(product.getPrice());
			update.setQty(product.getQty());
			if(product.getImageFile() !=null) {
				var file = product.getImageFile();
				String uploadDir = new File("myapp/static").getAbsolutePath();		
				File dir = new File(uploadDir);
				if(!dir.exists()) {
					dir.mkdirs();
				}
				String extension = Objects.requireNonNull(file.getOriginalFilename());
				String fileName = UUID.randomUUID()+"_"+extension;
				String filePath = Paths.get(uploadDir,fileName).toString();
				
				new File("myapp/"+update.getImageUrl()).delete();
				file.transferTo(new File(filePath));
				update.setImageUrl("/static/"+fileName);
			}
			
			productRepo.save(update);
			return ResponseEntity.status(HttpStatus.ACCEPTED)
					.body(Map.of("message","product update successfully=","product",update));
		}
		return ResponseEntity.status(404).body(Map.of("Message","product id ="+id+"not found"));
	}

}




