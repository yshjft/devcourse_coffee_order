package com.devcourse.coffeeorder.domain.product.service;

import com.devcourse.coffeeorder.domain.product.dao.ProductRepository;
import com.devcourse.coffeeorder.domain.product.dto.*;
import com.devcourse.coffeeorder.domain.product.entity.Category;
import com.devcourse.coffeeorder.domain.product.entity.Product;
import com.devcourse.coffeeorder.global.common.MetaData;
import com.devcourse.coffeeorder.global.exception.notfound.ProductNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * 상품 생성
     **/
    public ProductCreateResDto createProduct(ProductReqDto productReqDto) {
        Product newProduct = productRepository.create(productReqDto.toEntity());
        return new ProductCreateResDto(newProduct.getProductId(),
                newProduct.getProductName(),
                newProduct.getCreatedAt());
    }

    /**
     * 전체 상품 조회
     **/
    public ProductsResDto findAllProducts() {
        List<ProductResDto> productList = productRepository.findAll().stream()
                .map(product -> ProductResDto.builder()
                        .productId(product.getProductId())
                        .productName(product.getProductName())
                        .category(product.getCategory())
                        .price(product.getPrice())
                        .description(product.getDescription())
                        .createdAt(product.getCreatedAt())
                        .updatedAt(product.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());
        MetaData metaData = new MetaData(productList.size());

        return new ProductsResDto(metaData, productList);
    }

    /**
     * 카테고리별 전체 상품 조회
     **/
    public ProductsResDto findAllProductsByCategory(Category category) {
        List<ProductResDto> productList = productRepository.findByCategory(category).stream()
                .map(product -> ProductResDto.builder()
                        .productId(product.getProductId())
                        .productName(product.getProductName())
                        .category(product.getCategory())
                        .price(product.getPrice())
                        .description(product.getDescription())
                        .createdAt(product.getCreatedAt())
                        .updatedAt(product.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());

        MetaData metaData = new MetaData(productList.size());

        return new ProductsResDto(metaData, productList);
    }

    /**
     * ID를 통한 상품 상세 조회
     **/
    public ProductResDto findProduct(UUID productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId.toString()));

        return ProductResDto.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .category(product.getCategory())
                .price(product.getPrice())
                .description(product.getDescription())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    /**
     * 상품 수정: 상품의 카테고리, 가격, 설명을 수정
     **/
    public ProductUpdateResDto updateProduct(UUID productId, ProductReqDto productReqDto) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId.toString()));

        product.updateProduct(productReqDto.getProductName(),
                productReqDto.getCategory(),
                productReqDto.getPrice(),
                productReqDto.getDescription());

        Product updatedProduct = productRepository.update(product);

        return new ProductUpdateResDto(updatedProduct.getProductId(),
                updatedProduct.getProductName(),
                updatedProduct.getUpdatedAt());
    }

    /**
     * 상품 삭제
     */
    public UUID deleteProduct(UUID productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId.toString()));
        productRepository.delete(product);

        return productId;
    }
}
