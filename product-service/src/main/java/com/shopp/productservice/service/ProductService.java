package com.shopp.productservice.service;

import com.shopp.productservice.dto.ProductRequest;
import com.shopp.productservice.dto.ProductResponse;
import com.shopp.productservice.model.Product;
import com.shopp.productservice.repository.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

  private final ProductRepository productRepository;

  public void createProduct(ProductRequest productRequest) {
    Product product = Product
      .builder()
      .name(productRequest.getName())
      .description(productRequest.getDescription())
      .price(productRequest.getPrice())
      .build();

    if (product == null) return;
    productRepository.save(product);
    log.info("Product {} saved successfully!", product.getId());
  }

  public List<ProductResponse> getAllProduct() {
    List<Product> products = productRepository.findAll();

    return products.stream().map(this::mapToProductResponse).toList();
  }

  private ProductResponse mapToProductResponse(Product product) {
    return ProductResponse
      .builder()
      .id(product.getId())
      .name(product.getName())
      .description(product.getDescription())
      .price(product.getPrice())
      .build();
  }
}
