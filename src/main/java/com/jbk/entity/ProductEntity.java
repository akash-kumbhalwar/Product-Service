package com.jbk.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.springframework.stereotype.Component;

@Entity
@Table(name="product")
public class ProductEntity {
	@Id
	//@Min(value = 1, message = "Id should not be less than 1")
	private long productId;
	
	@Column(nullable = false, unique = true)
	private String productName;
	
	private long supplierId;
	
	
	private long categoryId;
	
	@Min(value = 1, message = "Should not be less than 1")
	private int productQty;
	
	@Min(value = 1, message = "Should not be less than 1")
	private double productPrice;
	
	@Min(value = 0)
	private int deliveryCharges;
	
	
	public ProductEntity() {
		
		super();
	}


	public ProductEntity(@Min(value = 1, message = "Id should not be less than 1") long productId, String productName,
			long supplierId, long categoryId, @Min(value = 1, message = "Should not be less than 1") int productQty,
			@Min(value = 1, message = "Should not be less than 1") double productPrice, @Min(0) int deliveryCharges) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.supplierId = supplierId;
		this.categoryId = categoryId;
		this.productQty = productQty;
		this.productPrice = productPrice;
		this.deliveryCharges = deliveryCharges;
	}


	public long getProductId() {
		return productId;
	}


	public void setProductId(long productId) {
		this.productId = productId;
	}


	public String getProductName() {
		return productName;
	}


	public void setProductName(String productName) {
		this.productName = productName;
	}


	public long getSupplierId() {
		return supplierId;
	}


	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}


	public long getCategoryId() {
		return categoryId;
	}


	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}


	public int getProductQty() {
		return productQty;
	}


	public void setProductQty(int productQty) {
		this.productQty = productQty;
	}


	public double getProductPrice() {
		return productPrice;
	}


	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}


	public int getDeliveryCharges() {
		return deliveryCharges;
	}


	public void setDeliveryCharges(int deliveryCharges) {
		this.deliveryCharges = deliveryCharges;
	}


	@Override
	public String toString() {
		return "ProductEntity [productId=" + productId + ", productName=" + productName + ", supplierId=" + supplierId
				+ ", categoryId=" + categoryId + ", productQty=" + productQty + ", productPrice=" + productPrice
				+ ", deliveryCharges=" + deliveryCharges + "]";
	}
	

	
	
	


}
