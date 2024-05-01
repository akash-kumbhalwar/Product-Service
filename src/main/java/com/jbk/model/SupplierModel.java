package com.jbk.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


public class SupplierModel {
	@Min(value=1, message="Invalid Supplier Id")
	private long supplierId;
	
	@NotBlank(message = "Supplier name should not be blank")
	@Pattern(regexp = "^[A-Za-z ]+$", message = "Supplier name should contain only alphabets and space")
	private String supplierName;
	
	@NotBlank(message = "City should not be blank")
	@Pattern(regexp = "^[A-Za-z ]+$", message = "City name should contain only alphabets and space")
	private String city;
	
	@Min(value = 100000, message = "Invalid Postal code")
	@Max(value = 999999, message = "Invalid Postal Code")
	private int postalCode;
	
	@NotBlank(message = "Country should not be blank")
	@Pattern(regexp = "^[A-Za-z ]+$", message = "Country name should contain only alphabets and space")
	private String country;
	
	
	@Pattern(regexp = "^[1-9][0-9]{9}+$", message = "Mobile number should contain only digits, not start with zero, and be 10 digit long")
	private String mobile;
	
	
	public SupplierModel() {
		
		super();	
	}
	
	
	public SupplierModel(long supplierId, String supplierName, String city, int postalCode, String country,
			String mobile) {
		super();
		this.supplierId = supplierId;
		this.supplierName = supplierName;
		this.city = city;
		this.postalCode = postalCode;
		this.country = country;
		this.mobile = mobile;
	}
	
	
	public long getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public int getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(int postalCode) {
		this.postalCode = postalCode;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	@Override
	public String toString() {
		return "supplierModel [supplierId=" + supplierId + ", supplierName=" + supplierName + ", city=" + city
				+ ", postalCode=" + postalCode + ", country=" + country + ", mobile=" + mobile + "]";
	}
	
	

}
