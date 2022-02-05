package com.collectiongarage.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.collectiongarage.model.Role;

public interface IRoleRepository extends JpaRepository<Role, Long> {

}
