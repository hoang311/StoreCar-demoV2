package com.collectiongarage.model;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "order_details")
public class OrderDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	@Transient
	public Integer productId;

	@Transient
	private String productName;

	@Transient
	private String productCode;

	@Transient
	private String customerFullName;

	@NotNull(message = "Quantity must be filled")
	@Min(value = 0, message = "Quantity invalid")
	private Integer quantityOrder;

	@NotNull(message = "Price must be filled")
	@Min(value = 0, message = "Price invalid")
	private BigDecimal priceEach;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "orderdetail")
	private RatingAndReview ratingAndReview;

	public OrderDetail() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OrderDetail(Integer id, Order order, Product product,
			@NotNull(message = "Quantity must be filled") @Min(value = 0, message = "Quantity invalid") Integer quantityOrder,
			@NotNull(message = "Price must be filled") @Min(value = 0, message = "Price invalid") BigDecimal priceEach) {
		super();
		this.id = id;
		this.order = order;
		this.product = product;
		this.quantityOrder = quantityOrder;
		this.priceEach = priceEach;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Integer getProductId() {
		productId = product.getId();
		return productId;
	}

	public String getProductName() {
		productName = product.getProductName();
		return productName;
	}

	public String getProductCode() {
		productCode = product.getProductCode();
		return productCode;
	}

	public String getCustomerFullName() {
		customerFullName = order.getCustomerFullName();
		return customerFullName;
	}

	public Integer getQuantityOrder() {
		return quantityOrder;
	}

	public void setQuantityOrder(Integer quantityOrder) {
		this.quantityOrder = quantityOrder;
	}

	public BigDecimal getPriceEach() {
		return priceEach;
	}

	public void setPriceEach(BigDecimal priceEach) {
		this.priceEach = priceEach;
	}

	public RatingAndReview getRatingAndReview() {
		return ratingAndReview;
	}

	public void setRatingAndReview(RatingAndReview ratingAndReview) {
		this.ratingAndReview = ratingAndReview;
	}

}
