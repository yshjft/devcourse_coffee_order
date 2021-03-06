package com.devcourse.coffeeorder.domain.product.service;


import static org.mockito.Mockito.*;

import com.devcourse.coffeeorder.domain.order.dao.orderitem.OrderItemRepository;
import com.devcourse.coffeeorder.domain.product.dao.product.ProductRepository;
import com.devcourse.coffeeorder.domain.product.dto.product.ProductReqDto;
import com.devcourse.coffeeorder.domain.product.entity.Product;
import com.devcourse.coffeeorder.global.exception.customexception.notfound.ProductNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;


@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @InjectMocks
    ProductService productService;

    @Mock
    ProductRepository productRepository;

    @Mock
    OrderItemRepository orderItemRepository;

    private UUID productId = UUID.randomUUID();

    private Product product = Product.builder()
            .productId(UUID.randomUUID())
            .build();

    private ProductReqDto productReqDto = ProductReqDto.builder()
            .productName("test")
            .category("커피")
            .price(1000)
            .description("커피 커피")
            .build();

    @Test
    @DisplayName("상품 수정 ProductNotFoundException 행위 테스트")
    void testUpdateProductNotFoundException() {
        try{
            when(productRepository.findById(productId)).thenThrow(new ProductNotFoundException());

            productService.updateProduct(productId, productReqDto);
        }catch(ProductNotFoundException e) {
            verify(productRepository, never()).update(any());
        }
    }

    @Test
    @DisplayName("상품 수정 테스트")
    void testUpdateProduct() {
        when(productRepository.findById(product.getProductId())).thenReturn(Optional.of(product));
        when(productRepository.update(product)).thenReturn(product);

        productService.updateProduct(product.getProductId(), productReqDto);

        verify(productRepository).findById(product.getProductId());
        verify(productRepository).update(product);
    }

    @Test
    @DisplayName("상품 삭제 ProductNotFoundException 테스트")
    void testDeleteProductNotFoundException() {
        try {
            when(productRepository.findById(productId)).thenThrow(new ProductNotFoundException(productId.toString()));

            productService.deleteProduct(productId);
        }catch (ProductNotFoundException e) {
            verify(productRepository, never()).delete(any());
        }
    }

    @Test
    @DisplayName("상품 삭제 테스트")
    void testDeleteProduct() {
        when(productRepository.findById(product.getProductId())).thenReturn(Optional.of(product));
        when(orderItemRepository.findByProductId(product.getProductId())).thenReturn(Arrays.asList());

        productService.deleteProduct(product.getProductId());

        verify(productRepository).findById(product.getProductId());
        verify(productRepository).delete(product);
    }
}