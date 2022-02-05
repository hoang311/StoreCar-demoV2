package com.collectiongarage.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.collectiongarage.model.Office;

public interface IOfficeRepository extends JpaRepository<Office, Integer> {

}
