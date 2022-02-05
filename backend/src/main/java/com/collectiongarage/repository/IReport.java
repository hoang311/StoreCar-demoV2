package com.collectiongarage.repository;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

public interface IReport {
	Integer getCustomerId();

	String getLastName();

	String getFirstName();

	String getCustomerFullName();

	String getPhoneNumber();

	Integer getOrderId();

	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "dd/MM/yyyy")
	Date getOrderDate();

	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "dd/MM/yyyy")
	Date getRequiredDate();

	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "dd/MM/yyyy")
	Date getShippedDate();

	String getOrderStatus();

	Integer getOrderDetailId();

	Integer getQuantityOrder();

	BigDecimal getPriceEach();

	BigDecimal getTotal();

	Integer getTotalOrder();

	BigDecimal getTotalRevenue();

}
