package com.example.letsplay.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.example.letsplay.response.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.letsplay.exceptions.DataNotValidatedException;
import com.example.letsplay.services.ProductService;

@RestController
@RequestMapping(value = "/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;


    @GetMapping(value = "/allProducts")
    public ResponseEntity<List<ProductResponse>> ListAllProducts() {
        List<ProductResponse> allProducts = productService.listAll();
        return ResponseEntity.ok().body(allProducts);
    }

    @GetMapping(value = "/productDetails/{id}")
    public ResponseEntity<ProductResponse> getProductDetails(@PathVariable("id") String id) {
        ProductResponse product = productService.getProductById(id);
        return ResponseEntity.ok().body(product);
    }

    @GetMapping(value = "/allProducts/{userId}")
    public ResponseEntity<List<ProductResponse>> getAllProductsByUserId(@PathVariable("userId") String userId) {
        List<ProductResponse> allProducts = productService.listAllByUserId(userId);
        return  ResponseEntity.ok().body(allProducts);
    }


    @PostMapping(value = "/storeProduct")
    public ResponseEntity<ProductResponse> storeProduct(@Validated @RequestBody ProductResponse product, Errors errors) {
        if(errors.hasErrors()) {
            String validationErrors = errors.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining("; "));
            throw new DataNotValidatedException(new Error(validationErrors));
        }
        ProductResponse storedProduct = productService.storeProduct(product);
        return ResponseEntity.ok().body(storedProduct);
    }

    @PutMapping(value = "/updateProduct/{productId}")
    public ResponseEntity<String> updateProduct(@Validated @RequestBody ProductResponse product, Errors errors, @PathVariable("productId") String productId) {
        if(errors.hasErrors()) {
            String validationErrors = errors.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining("; "));
            throw new DataNotValidatedException(new Error(validationErrors));
        }
        ProductResponse updatedProduct = productService.updateProduct(product, productId);
        return ResponseEntity.ok().body("Product has been successfully updated with the id: " + updatedProduct.getId());
    }

    @DeleteMapping(value = "/deleteProduct/{productId}")
    public ResponseEntity<String> removeProduct(@PathVariable("productId") String productId) {
        productService.removeProduct(productId);
        return  ResponseEntity.ok().body("Product has been deleted with the id: " + productId);
    }
}
