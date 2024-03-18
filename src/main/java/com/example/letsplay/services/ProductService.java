package com.example.letsplay.services;



import com.example.letsplay.model.Product;
import com.example.letsplay.response.ProductResponse;

import java.util.List;


public interface ProductService {

    ProductResponse storeProduct(ProductResponse product);

    ProductResponse getProductById(String productId);

    List<ProductResponse> listAllByUserId(String userId);

    List<ProductResponse> listAll();

    void removeProduct(String productId);

    ProductResponse updateProduct(ProductResponse product, String productId);

}