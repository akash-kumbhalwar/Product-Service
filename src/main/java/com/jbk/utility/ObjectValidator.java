package com.jbk.utility;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jbk.exception.ResourceNotExistException;
import com.jbk.exception.SomethingWentWrongException;
import com.jbk.model.ProductModel;

@Component
public class ObjectValidator {
	
	public Map<String, String> validateProduct(ProductModel productModel) {
		Map<String, String> validationMap = new HashMap<String, String>();
		
		String productName = productModel.getProductName();
		long supplierId = productModel.getSupplierId();
		long categoryId = productModel.getCategoryId();
		int productQty = productModel.getProductQty();
		double productPrice = productModel.getProductPrice();
		int deliveryCharges = productModel.getDeliveryCharges();
		
		if(productName==null || productName.trim().equals("")) {
			validationMap.put("Product Name", "Invalid product name");
		}
		if(supplierId > 0) {
			try {
				//supplierService.getSupplierById(supplierId);
			} catch (ResourceNotExistException e) {
				validationMap.put("Supplier", e.getMessage());
			}catch (SomethingWentWrongException e) {
				validationMap.put("Supplier", e.getMessage());
			}
		}else {
			validationMap.put("Supplier", "Invalid Supplier Id");
		}
		
		if(categoryId > 0) {
			try {
				//categoryService.getCategoryById(categoryId);
			} catch (ResourceNotExistException e) {
				validationMap.put("Category", e.getMessage());
			}catch (SomethingWentWrongException e) {
				validationMap.put("Category", e.getMessage());
			}
		}else {
			validationMap.put("Category", "Invalid Category Id");
		}
		
		if(productQty<=0) {
			validationMap.put("Product Qty", "Product quanity must be less than 0");
		}
		
		if(productPrice<=0) {
			validationMap.put("Product Price", "Product price must be Greater than 0");
		}
		
		if(deliveryCharges<0) {
			validationMap.put("Delivery charges", "Delivery charges should not be Negative");
		}
		
		return validationMap;
		
	}

}
