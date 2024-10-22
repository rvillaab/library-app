package com.rjvilla.catalog_service.domain;

import com.rjvilla.catalog_service.ApplicationProperties;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ApplicationProperties applicationProperties;

    public PagedResult<Product> getProducts(int pageNumber) {
        Sort sort = Sort.by("name").ascending();
        pageNumber = pageNumber <= 1 ? 0 : pageNumber - 1;
        Pageable pageable = PageRequest.of(pageNumber, 10, sort);
        Page<Product> productsPage = productRepository.findAll(pageable).map(ProductMapper::toProduct);
        return new PagedResult<>(
                productsPage.getContent(),
                productsPage.getTotalElements(),
                productsPage.getNumber() + 1,
                productsPage.getTotalPages(),
                productsPage.isFirst(),
                productsPage.isLast(),
                productsPage.hasNext(),
                productsPage.hasPrevious());
    }

    public Optional<Product> getProductByCode(String code) {
        return productRepository.findByCode(code).map(ProductMapper::toProduct);
    }
}
