package com.collectiongarage.model;

import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "products")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotBlank(message = "Product Code must be filled")
	private String productCode;

	@NotBlank(message = "Product Name must be filled")
	private String productName;

	private String productDescription;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_line_id")
	private ProductLine productLine;

	@Transient
	private Integer prodLineId;

	@Transient
	private String productLineName;

	@NotBlank(message = "Product Scale must be filled")
	private String productScale;

	@NotBlank(message = "Product Vendor must be filled")
	private String productVendor;

	@NotNull(message = "Quantity In Stock must be filled")
	@Min(value = 0, message = "Quantity In Stock invalid")
	private Integer quantityInStock;

	@NotNull(message = "Buy Price must be filled")
	@Min(value = 0, message = "Buy Price invalid")
	private BigDecimal buyPrice;

	@Transient
	private BigDecimal highestPriceEach;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "product")
	private Set<OrderDetail> orderDetails;

	public Product() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Product(Integer id, @NotBlank(message = "Product Code must be filled") String productCode,
			@NotBlank(message = "Product Name must be filled") String productName, String productDescription,
			ProductLine productLine, @NotBlank(message = "Product Scale must be filled") String productScale,
			@NotBlank(message = "Product Vendor must be filled") String productVendor,
			@NotNull(message = "Quantity In Stock must be filled") @Min(value = 0, message = "Quantity In Stock invalid") Integer quantityInStock,
			@NotNull(message = "Buy Price must be filled") @Min(value = 0, message = "Buy Price invalid") BigDecimal buyPrice,
			Set<OrderDetail> orderDetails) {
		super();
		this.id = id;
		this.productCode = productCode;
		this.productName = productName;
		this.productDescription = productDescription;
		this.productLine = productLine;
		this.productScale = productScale;
		this.productVendor = productVendor;
		this.quantityInStock = quantityInStock;
		this.buyPrice = buyPrice;
		this.orderDetails = orderDetails;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public ProductLine getProductLine() {
		return productLine;
	}

	public void setProductLine(ProductLine productLine) {
		this.productLine = productLine;
	}

	public Integer getProdLineId() {
		prodLineId = productLine.getId();
		return prodLineId;
	}

	public String getProductLineName() {
		productLineName = productLine.getProductLine();
		return productLineName;
	}

	public String getProductScale() {
		return productScale;
	}

	public void setProductScale(String productScale) {
		this.productScale = productScale;
	}

	public String getProductVendor() {
		return productVendor;
	}

	public void setProductVendor(String productVendor) {
		this.productVendor = productVendor;
	}

	public Integer getQuantityInStock() {
		return quantityInStock;
	}

	public void setQuantityInStock(Integer quantityInStock) {
		this.quantityInStock = quantityInStock;
	}

	public BigDecimal getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(BigDecimal buyPrice) {
		this.buyPrice = buyPrice;
	}

	public BigDecimal getHighestPriceEach() {
		BigDecimal maxPrice = new BigDecimal(0);

		for (OrderDetail orderDetail : orderDetails) {
			if (maxPrice.compareTo(orderDetail.getPriceEach()) == -1) {
				maxPrice = orderDetail.getPriceEach();
			}
		}

		highestPriceEach = maxPrice;

		return highestPriceEach;
	}

	public Set<OrderDetail> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(Set<OrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}

}
