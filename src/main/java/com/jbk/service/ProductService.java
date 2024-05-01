package com.jbk.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.jbk.entity.ProductEntity;
import com.jbk.model.ProductModel;
import com.jbk.model.Product_Supplier_Category;

public interface ProductService {

	public boolean addProduct(ProductModel productModel);

	public ProductModel getProductById(long productId);
	
	public Product_Supplier_Category getProductWithSCByPId(long productId);

	public boolean deleteProductById(long productId);

	public boolean updateProduct(ProductModel productModel);

	// Criteria n Restriction starts from here
	public List<ProductModel> getAllProducts();
	public List<ProductModel> sortProduct(String orderType, String property);
	public double getMaxProductPrice();
	public List<ProductModel> getMaxPriceProduct();
	public ProductModel getProductByName(String productName);
	public List<ProductModel> getAllProducts(double lowPrice, double highPrice);// all products whose supplier=? and category=?
	public List<ProductModel> getProductStartWith(String expression);// all products whose supplier=? and category=?
	public double productPriceAverage();
	public double countOfTotalProducts();
	public List<ProductModel> getAllProducts(long category, long supplier);
	public List<ProductModel> getAllProducts(String supplier);
	public ProductModel getDetailsOfProduct(long productId);
	
	//Read write excel file data i.e. file handling
	public Map<String, Object> uploadSheet(MultipartFile myfile);
	





}
