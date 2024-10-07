package com.hmdrinks.Repository;

import com.hmdrinks.Entity.Category;
import com.hmdrinks.Entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Integer> {

    Product findByProId(Integer proId);

    Product findByProName(String proName);

    Product findByProNameAndProIdNot(String proName,Integer proId);
    Page<Product> findAll(Pageable pageable);

    Page<Product> findByProNameContaining(String proName, Pageable pageable);

    List<Product> findByCategory_CateId(int cateId);
}
