package com.jbk.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class CategoryModel {
	@Id
	@Min(value = 1, message = "Invalid Category Id")
	private long categoryId;
	
	@NotBlank(message = "Category name should not be blank")
	@Pattern(regexp = "^[A-Za-z ]+$", message = "Supplier name should contain only alphabets and space")
	private String categoryName;
	
	@NotBlank(message = "Description should not be blank")
	@Pattern(regexp = "^[A-Za-z ]+$", message = "City name should contain only alphabets and space")
	private String description;
	
	@Min(value = 1, message = "Invalid discount")
	private int discount;
	
	@Min(value = 1, message = "Invalid gst")
	private int gst;
	
	public CategoryModel() {
		super();
		
	}

	public CategoryModel(long categoryId, String categoryName, String description, int discount, int gst) {
		super();
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.description = description;
		this.discount = discount;
		this.gst = gst;
	}

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

	public int getGst() {
		return gst;
	}

	public void setGst(int gst) {
		this.gst = gst;
	}

	@Override
	public String toString() {
		return "CategoryModel [categoryId=" + categoryId + ", categoryName=" + categoryName + ", description="
				+ description + ", discount=" + discount + ", gst=" + gst + "]";
	}
	
	
	
	

}
