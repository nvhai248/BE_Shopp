package com.shopp.productservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.assertions.Assertions;
import com.shopp.productservice.dto.ProductRequest;
import com.shopp.productservice.repository.ProductRepository;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

  @Container
  static MongoDBContainer mongoDBContainer = new MongoDBContainer(
    "mongo:4.4.2"
  );

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private ProductRepository productRepository;

  static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
    dynamicPropertyRegistry.add(
      "spring.data.mongodb.uri",
      mongoDBContainer::getReplicaSetUrl
    );
  }

  @SuppressWarnings("null")
  @Test
  void shouldCreateProduct() throws JsonProcessingException {
    ProductRequest productRequest = getProductRequest();
    String productRequestString = objectMapper.writeValueAsString(
      productRequest
    );
    try {
      mockMvc
        .perform(
          MockMvcRequestBuilders
            .post("/api/product")
            .contentType(MediaType.APPLICATION_JSON)
            .content(productRequestString)
        )
        .andExpect(MockMvcResultMatchers.status().isCreated());

      Assertions.assertTrue(productRepository.findAll().size() == 1);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private ProductRequest getProductRequest() {
    return ProductRequest
      .builder()
      .name("Iphone 13")
      .description("iPhone 13")
      .price(BigDecimal.valueOf(1200))
      .build();
  }
}
