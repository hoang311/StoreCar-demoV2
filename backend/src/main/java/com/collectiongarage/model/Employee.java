package com.collectiongarage.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "employees")
public class Employee {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotBlank(message = "Last Name must be filled")
	private String lastName;

	@NotBlank(message = "First Name must be filled")
	private String firstName;

	@NotBlank(message = "Extension must be filled")
	private String extension;

	@NotBlank(message = "Email must be filled")
	@Email(message = "Email invalid")
	private String email;

	@NotNull(message = "Office Code must be filled")
	@Min(value = 0, message = "Office Code invalid")
	private Integer officeCode;

	@Min(value = 0, message = "Report To invalid")
	private Integer reportTo;

	@NotBlank(message = "Job Title must be filled")
	private String jobTitle;

	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	public Employee() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Employee(Integer id, @NotBlank(message = "Last Name must be filled") String lastName,
			@NotBlank(message = "First Name must be filled") String firstName,
			@NotBlank(message = "Extension must be filled") String extension,
			@NotBlank(message = "Email must be filled") @Email(message = "Email invalid") String email,
			@NotBlank(message = "Office Code must be filled") Integer officeCode, Integer reportTo,
			@NotBlank(message = "Job Title must be filled") String jobTitle) {
		super();
		this.id = id;
		this.lastName = lastName;
		this.firstName = firstName;
		this.extension = extension;
		this.email = email;
		this.officeCode = officeCode;
		this.reportTo = reportTo;
		this.jobTitle = jobTitle;
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

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getOfficeCode() {
		return officeCode;
	}

	public void setOfficeCode(Integer officeCode) {
		this.officeCode = officeCode;
	}

	public Integer getReportTo() {
		return reportTo;
	}

	public void setReportTo(Integer reportTo) {
		this.reportTo = reportTo;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
