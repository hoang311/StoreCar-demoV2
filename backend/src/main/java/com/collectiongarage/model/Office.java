package com.collectiongarage.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "offices")
public class Office {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotBlank(message = "City must be filled")
	private String city;

	@NotBlank(message = "Phone must be filled")
	private String phone;

	@NotBlank(message = "Address Line must be filled")
	private String addressLine;

	private String state;

	@NotBlank(message = "Country must be filled")
	private String country;

	private String territory;

	public Office() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Office(Integer id, @NotBlank(message = "City must be filled") String city,
			@NotBlank(message = "Phone must be filled") String phone,
			@NotBlank(message = "Address Line must be filled") String addressLine, String state,
			@NotBlank(message = "Country must be filled") String country, String territory) {
		super();
		this.id = id;
		this.city = city;
		this.phone = phone;
		this.addressLine = addressLine;
		this.state = state;
		this.country = country;
		this.territory = territory;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddressLine() {
		return addressLine;
	}

	public void setAddressLine(String addressLine) {
		this.addressLine = addressLine;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getTerritory() {
		return territory;
	}

	public void setTerritory(String territory) {
		this.territory = territory;
	}

}
