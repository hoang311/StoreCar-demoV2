package com.collectiongarage.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.collectiongarage.model.ProductLine;

public interface IProductLineRepository extends JpaRepository<ProductLine, Integer> {

}
