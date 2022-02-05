package com.collectiongarage.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "customers")
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotBlank(message = "Last Name must be filled")
	private String lastName;

	@NotBlank(message = "First Name must be filled")
	private String firstName;

	@Transient
	private String customerFullName;

	@NotBlank(message = "Phone Number must be filled")
	@Column(nullable = false, unique = true)
	private String phoneNumber;

	private String address;

	private String city;

	private String state;

	private String postalCode;

	private String country;

	@Min(value = 0, message = "Number of Sales Rep Employee invalid")
	private Integer salesRepEmployeeNumber;

	@Min(value = 0, message = "Credit Limit invalid")
	private Integer creditLimit;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "customer")
	private Set<Payment> payments;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "customer")
	private Set<Order> orders;

	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	public Customer() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Customer(Integer id, @NotBlank(message = "Last Name must be filled") String lastName,
			@NotBlank(message = "First Name must be filled") String firstName,
			@NotBlank(message = "Phone Number must be filled") String phoneNumber, String address, String city,
			String state, String postalCode, String country,
			@Min(value = 0, message = "Number of Sales Rep Employee invalid") Integer salesRepEmployeeNumber,
			@Min(value = 0, message = "Credit Limit invalid") Integer creditLimit, Set<Payment> payments,
			Set<Order> orders) {
		super();
		this.id = id;
		this.lastName = lastName;
		this.firstName = firstName;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.city = city;
		this.state = state;
		this.postalCode = postalCode;
		this.country = country;
		this.salesRepEmployeeNumber = salesRepEmployeeNumber;
		this.creditLimit = creditLimit;
		this.payments = payments;
		this.orders = orders;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getCustomerFullName() {
		customerFullName = firstName + lastName;
		return customerFullName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Integer getSalesRepEmployeeNumber() {
		return salesRepEmployeeNumber;
	}

	public void setSalesRepEmployeeNumber(Integer salesRepEmployeeNumber) {
		this.salesRepEmployeeNumber = salesRepEmployeeNumber;
	}

	public Integer getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(Integer creditLimit) {
		this.creditLimit = creditLimit;
	}

	public Set<Payment> getPayments() {
		return payments;
	}

	public void setPayments(Set<Payment> payments) {
		this.payments = payments;
	}

	public Set<Order> getOrders() {
		return orders;
	}

	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
