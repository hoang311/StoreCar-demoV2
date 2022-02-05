package com.collectiongarage.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.collectiongarage.model.Payment;

public interface IPaymentRepository extends JpaRepository<Payment, Integer> {

}
