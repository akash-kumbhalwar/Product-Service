package com.jbk.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jbk.exception.ResourceAlreadyExistException;
import com.jbk.exception.ResourceNotExistException;
import com.jbk.model.ProductModel;
import com.jbk.model.Product_Supplier_Category;
import com.jbk.service.ProductService;
import com.jbk.service.impl.ProductServiceImpl;


@RestController
@RequestMapping("product")
public class ProductController {

	//ProductService service = new ProductServiceImpl();
	@Autowired
	ProductService service;

	@PostMapping("/add-product")
	public ResponseEntity<String> addProduct(@RequestBody @Valid ProductModel product) {
		service.addProduct(product);

		return ResponseEntity.ok("Product Added Successfully..!!");
	}


	@GetMapping("/get-product-by-id/{productId}")
	public ResponseEntity<ProductModel> getProductById(@PathVariable long productId) {
		ProductModel productModel = service.getProductById(productId);
		return ResponseEntity.ok(productModel);

	}
	@GetMapping("/get-product-with-sc/{productId}")
	public ResponseEntity<Product_Supplier_Category> getProductByIdWithSC(@PathVariable long productId){
		Product_Supplier_Category psc = service.getProductWithSCByPId(productId);
		return ResponseEntity.ok(psc);
	}

	@DeleteMapping("/delete-product-by-id")
	public ResponseEntity<String> deleteProductById(@RequestParam("productId") long productId) {
		service.deleteProductById(productId);

		return ResponseEntity.ok("Product delete successfully..!!");
	}

	@PutMapping("/update-product")
	public ResponseEntity<String> updateProduct(@RequestBody ProductModel productModel) {
		service.updateProduct(productModel);
		return ResponseEntity.ok("Product update successfully..!!");

	}

	@GetMapping("/get-all-products")
	public ResponseEntity<List<ProductModel>> getAllProducts(){
		List<ProductModel> allProducts = service.getAllProducts();
		return ResponseEntity.ok(allProducts);

	}
	@GetMapping("/sort-products")
	public ResponseEntity<List<ProductModel>> sortProduct(@RequestParam String orderType, @RequestParam String property) {
		List<ProductModel> sortProduct = service.sortProduct(orderType, property);
		return ResponseEntity.ok(sortProduct);
	}

	@GetMapping("/max-price")
	public ResponseEntity<Double>  getMaxProductPrice(){
		double productModel = service.getMaxProductPrice();
		return ResponseEntity.ok(productModel);
	}
	@GetMapping("/max-price-product")
	public ResponseEntity<List<ProductModel>> getMaxPriceProduct(){
		List<ProductModel> productModel = service.getMaxPriceProduct();
		return ResponseEntity.ok(productModel);

	}

	@GetMapping("/get-product-by-name/{productName}")
	public ResponseEntity<ProductModel> getProductByName(@PathVariable String productName){

		return ResponseEntity.ok(service.getProductByName(productName));
	}
	@GetMapping("/sort-by-price")
	public ResponseEntity<List<ProductModel>> getAllProducts(@RequestParam double lowPrice, @RequestParam double highPrice){

		return ResponseEntity.ok(service.getAllProducts(lowPrice, highPrice));

	}

	@GetMapping("/product-start-with")
	public ResponseEntity<List<ProductModel>> getProductStartWith(@RequestParam("expression")String expression){
		List<ProductModel> productModel = service.getProductStartWith(expression);
		return ResponseEntity.ok(productModel);
	}


	@GetMapping("/product-average-price")
	public  ResponseEntity<Double> productPriceAverage(){
		double avgPrice = service.productPriceAverage();
		return ResponseEntity.ok(avgPrice);
	}
	@GetMapping("/count-of-total-products")
	public ResponseEntity<Double> countOfTotalProducts(){
		double countTotal = service.countOfTotalProducts();
		return ResponseEntity.ok(countTotal);
	}
	@GetMapping("/details-of-product/productId")
	public ResponseEntity<ProductModel> getDetailsOfProduct(@PathVariable long productId){
		ProductModel productModel = service.getDetailsOfProduct(productId);
		return ResponseEntity.ok(productModel);
	}
	
	@GetMapping("/get-all-product")
	public ResponseEntity<List<ProductModel>> getAllProducts(@RequestParam ("category") long category, @RequestParam ("supplier")long supplier){
		List<ProductModel> productModel = service.getAllProducts(category, supplier);
		return ResponseEntity.ok(productModel);
		
	}
	@GetMapping("/products-of-supplier")
	public ResponseEntity<List<ProductModel>> getAllProducts(@RequestParam ("supplier")String supplier){
		List<ProductModel> productModel = service.getAllProducts(supplier);
		return ResponseEntity.ok(productModel);

	}
	@PostMapping("/upload-sheet")
	public ResponseEntity<Map<String, Object>> uploadSheet(@RequestParam  MultipartFile myfile){
		System.out.println(myfile.getOriginalFilename());
	Map<String, Object> finalMap = service.uploadSheet(myfile);
		return ResponseEntity.ok(finalMap);


	}

}
