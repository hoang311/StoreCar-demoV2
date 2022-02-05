package com.collectiongarage.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "rating_and_review")
public class RatingAndReview extends BaseEntity {

	@Transient
	private String customerFullName;

	@Min(value = 1)
	@Max(value = 5)
	private Byte rating;

	private String review;

	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "orderdetail_id", nullable = false)
	private OrderDetail orderdetail;

	public String getCustomerFullName() {
		customerFullName = orderdetail.getCustomerFullName();
		return customerFullName;
	}

	public Byte getRating() {
		return rating;
	}

	public void setRating(Byte rating) {
		this.rating = rating;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public OrderDetail getOrderdetail() {
		return orderdetail;
	}

	public void setOrderdetail(OrderDetail orderdetail) {
		this.orderdetail = orderdetail;
	}

}
