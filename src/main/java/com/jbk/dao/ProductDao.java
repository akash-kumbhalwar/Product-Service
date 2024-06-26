package com.jbk.dao;

import java.util.List;

import com.jbk.entity.ProductEntity;
import com.jbk.model.ProductModel;

public interface ProductDao {
	
	public boolean addProduct(ProductEntity productEntity);
	
	public ProductEntity getProductById(long productId);
	
	public boolean deleteProductById(long productId);
	
	public boolean updateProduct(ProductEntity productEntity);
	
	
	
	// Criteria n Restriction starts from here
	public List<ProductEntity> getAllProducts();
	public List<ProductEntity> sortProduct(String orderType, String property);
	public double getMaxProductPrice();
	public List<ProductEntity> getMaxPriceProduct();
	public ProductEntity getProductByName(String productName);
	public List<ProductEntity> getAllProducts(double lowPrice, double highPrice);
	public List<ProductEntity> getProductStartWith(String expression);
	public double productPriceAverage();
	public double countOfTotalProducts();
	public List<ProductEntity> getAllProducts(long category, long supplier);
	public List<ProductEntity> getAllProducts(String supplier);
	public ProductEntity getDetailsOfProduct(long productId);
	
	
	

	

}
