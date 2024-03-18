package com.example.letsplay.services.impl;


import com.example.letsplay.converter.TempConverter;
import com.example.letsplay.exceptions.ExistingProductException;
import com.example.letsplay.exceptions.InstanceUndefinedException;
import com.example.letsplay.exceptions.PermissionDeniedException;
import com.example.letsplay.model.Product;
import com.example.letsplay.repository.ProductRepository;
import com.example.letsplay.response.ProductResponse;
import com.example.letsplay.response.UserResponse;
import com.example.letsplay.services.ProductService;
import com.example.letsplay.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TempConverter tempConverter;

    @Override
    public ProductResponse storeProduct(ProductResponse product) {
        Optional<Product> productOptional = productRepository.findByName(product.getName());
        if (productOptional.isPresent()) {
            Error error = new Error("Product exists already.");
            throw new ExistingProductException(error);
        }
        UserResponse currentUser = userService.getCurrentUser();
        product.setUserId(currentUser.getId());
        Product storedProduct = productRepository.save(tempConverter.responseToProduct(product));
        return tempConverter.productToResponse(storedProduct);
    }

    @Override
    public ProductResponse getProductById(String productId) {
        ProductResponse returnValue;
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            returnValue = tempConverter.productToResponse(optionalProduct.get());
        } else {
            Error error = new Error("Product not found with the ID:" + productId);
            throw new InstanceUndefinedException(error);
        }
        return returnValue;
    }

    @Override
    public List<ProductResponse> listAllByUserId(String userId) {
        List<ProductResponse> returnValue = new ArrayList<>();
        List<Product> allProducts = productRepository.findByUserId(userId);
        allProducts.forEach((product) -> {
            ProductResponse response = tempConverter.productToResponse(product);
            returnValue.add(response);
        });

        return returnValue;
    }

    @Override
    public List<ProductResponse> listAll() {
        List<ProductResponse> returnValue = new ArrayList<>();
        List<Product> allProducts = productRepository.findAll();
        allProducts.forEach((product) -> {
            ProductResponse response = tempConverter.productToResponse(product);
            returnValue.add(response);
        });

        return returnValue;
    }

    @Override
    public void removeProduct(String productId) {
        ProductResponse product = getProductById(productId);
        UserResponse currentUser = userService.getCurrentUser();

        if (product.getUserId().equalsIgnoreCase(currentUser.getId())) {
            productRepository.deleteById(productId);
        } else {
            throw new PermissionDeniedException(new Error("This product can be only removed by the owner."));
        }

    }

    @Override
    public ProductResponse updateProduct(ProductResponse product, String productId) {
        ProductResponse currentProduct = getProductById(productId);
        UserResponse currentUser = userService.getCurrentUser();
        if (!currentProduct.getUserId().equalsIgnoreCase(currentUser.getId())) {
            throw new PermissionDeniedException(new Error("This product can be only updated by the owner."));
        }

        Optional<Product> productOptional = productRepository.findByName(product.getName());
        if (productOptional.isPresent()) {
            Error error = new Error("Product exists already. Use another name.");
            throw new ExistingProductException(error);
        }

        product.setId(currentProduct.getId());
        product.setUserId(currentUser.getId());
        Product updatedProduct = productRepository.save(tempConverter.responseToProduct(product));
        return tempConverter.productToResponse(updatedProduct);
    }

}
