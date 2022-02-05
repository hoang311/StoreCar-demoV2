package com.collectiongarage.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "payments")
public class Payment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;

	@Transient
	private String customerFullName;

	@NotBlank(message = "Check Number must be filled")
	private String checkNumber;

	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date paymentDate;

	@NotNull(message = "Ammount must be filled")
	@Min(value = 0, message = "Ammount invalid")
	private BigDecimal ammount;

	public Payment() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Payment(Integer id, Customer customer, @NotBlank(message = "Check Number must be filled") String checkNumber,
			Date paymentDate,
			@NotNull(message = "Ammount must be filled") @Min(value = 0, message = "Ammount invalid") BigDecimal ammount) {
		super();
		this.id = id;
		this.customer = customer;
		this.checkNumber = checkNumber;
		this.paymentDate = paymentDate;
		this.ammount = ammount;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getCustomerFullName() {
		customerFullName = customer.getCustomerFullName();
		return customerFullName;
	}

	public String getCheckNumber() {
		return checkNumber;
	}

	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public BigDecimal getAmmount() {
		return ammount;
	}

	public void setAmmount(BigDecimal ammount) {
		this.ammount = ammount;
	}

}
