package com.smartinventory.inventory.controller;

import com.smartinventory.inventory.model.Product;
import com.smartinventory.inventory.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class ProductController {

    private final ProductRepository repository;

    public ProductController(ProductRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return repository.save(product);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        product.setId(id);
        return repository.save(product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @PostMapping("/{id}/increase")
    public Product increase(@PathVariable Long id, @RequestParam int qty) {
        Product p = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        p.setQuantity(p.getQuantity() + qty);
        return repository.save(p);
    }

    @PostMapping("/{id}/decrease")
    public Product decrease(@PathVariable Long id, @RequestParam int qty) {
        Product p = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (p.getQuantity() < qty) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        p.setQuantity(p.getQuantity() - qty);
        return repository.save(p);
    }
}

