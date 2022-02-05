package com.collectiongarage.repository;

import java.math.BigDecimal;

public interface IPopularProductsWithProductLineOrderDetailInfo {
	Integer getProductLineId();

	String getProductLine();

	Integer getProductId();

	String getProductCode();
	
	String getProductName();

	String getProductDescription();

	String getProductVendor();

	BigDecimal getBuyPrice();

	Integer getOrderId();

	BigDecimal getPriceEach();

	Integer getOrderDetailId();
}
