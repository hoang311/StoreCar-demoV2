package com.collectiongarage.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "t_token")
public class Token extends BaseEntity {

	@Column(length = 1000)
	private String token;

	private Date tokenExpDate;

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * @return the tokenExpDate
	 */
	public Date getTokenExpDate() {
		return tokenExpDate;
	}

	/**
	 * @param tokenExpDate the tokenExpDate to set
	 */
	public void setTokenExpDate(Date tokenExpDate) {
		this.tokenExpDate = tokenExpDate;
	}

}
