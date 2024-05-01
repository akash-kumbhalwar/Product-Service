package com.jbk.service.impl;

import com.jbk.dao.impl.ProductDaoImpl;
import com.jbk.entity.ProductEntity;
import com.jbk.exception.ResourceAlreadyExistException;
import com.jbk.exception.ResourceNotExistException;
import com.jbk.exception.SomethingWentWrongException;
import com.jbk.model.CategoryModel;
import com.jbk.model.ProductModel;
import com.jbk.model.Product_Supplier_Category;
import com.jbk.model.SupplierModel;
import com.jbk.service.ProductService;
import com.jbk.utility.ObjectValidator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.jbk.dao.ProductDao;

//ProductService service = new ProductServiceImpl();
//@Component

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	ProductDao dao;

	@Autowired
	ModelMapper mapper;

	@Autowired
	ObjectValidator validator;

	Map<Integer, Map<String, String>> rowError = new HashMap<Integer, Map<String,String>>();
	Map<String, Object> finalMap = new LinkedHashMap<String, Object>();

	List<Integer> rowNumList = new ArrayList<Integer>();

	Map<String, String> validationMap = new HashMap<String, String>();

	int totalRecords=0;
	@Override
	public boolean addProduct(ProductModel productModel) {
		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
		productModel.setProductId(Long.parseLong(timeStamp));
		ProductEntity productEntity = mapper.map(productModel, ProductEntity.class);

		return dao.addProduct(productEntity);
	}

	@Override
	public ProductModel getProductById(long productId) {
		ProductEntity productEntity = dao.getProductById(productId);

		if(productEntity != null) {
			ProductModel map = mapper.map(productEntity, ProductModel.class);
			return map;
		}else {
			throw new ResourceNotExistException("Product not found with Id:-"+productId);
		}

	}

	@Override
	public boolean deleteProductById(long productId) {

		boolean productEntity = dao.deleteProductById(productId);

		ProductModel map = mapper.map(productEntity, ProductModel.class);

		return productEntity;

	}

	@Override
	public boolean updateProduct(ProductModel productModel) {
		ProductEntity productEntity = mapper.map(productModel, ProductEntity.class);
		boolean updateProduct = dao.updateProduct(productEntity);
		return updateProduct;

	}

	@Override
	public List<ProductModel> getAllProducts() {
		List<ProductEntity> entityList = dao.getAllProducts();
		List<ProductModel> modelList = new ArrayList<ProductModel>();
		if(!entityList.isEmpty()) {
			for (ProductEntity  productEntity: entityList) {
				ProductModel productModel = mapper.map(productEntity, ProductModel.class);
				modelList.add(productModel);
			}
			return modelList;
		}else {
			throw new ResourceNotExistException("Product not exist");
		}

	}

	@Override
	public List<ProductModel> sortProduct(String orderType, String property) {
		List<ProductEntity> entityList = dao.sortProduct(orderType, property);
		List<ProductModel> modelList = new ArrayList<ProductModel>();
		if(!entityList.isEmpty()) {
			for (ProductEntity productEntity : entityList) {
				ProductModel productModel = mapper.map(productEntity, ProductModel.class);
				modelList.add(productModel);
			}
		}else {
			throw new ResourceNotExistException("Product not exist");
		}
		return modelList;
	}

	@Override
	public double getMaxProductPrice() {
		double maxProductPrice = dao.getMaxProductPrice();
		if(maxProductPrice > 0) {
			return maxProductPrice;
		}else {
			throw new ResourceNotExistException("Product not found");
		}
	}

	@Override
	public List<ProductModel> getMaxPriceProduct() {
		List<ProductEntity> entityList = dao.getMaxPriceProduct();
		List<ProductModel> modelList = new ArrayList<ProductModel>();

		for (ProductEntity productEntity : entityList) {
			ProductModel productModel = mapper.map(productEntity, ProductModel.class);
			modelList.add(productModel);
		}
		// java 8 feature lambda expression
		//		modelList=entityList.stream().map(productEntity-> mapper.map(productEntity, ProductModel.class)).collect(Collectors.toList());
		return modelList;
	}

	@Override
	public ProductModel getProductByName(String productName) {
		ProductEntity dbProduct = dao.getProductByName(productName);
		if(dbProduct!=null) {
			return mapper.map(dao.getProductByName(productName), ProductModel.class);
		}else {
			return null;
		}
	}
	@Override
	public List<ProductModel> getAllProducts(double lowPrice, double highPrice) {
		List<ProductEntity> entityList = dao.getAllProducts(lowPrice, highPrice);
		//System.out.println(entityList);
		List <ProductModel> modelList = new ArrayList <ProductModel>();
		if(!entityList.isEmpty()) {
			for (ProductEntity productEntity : entityList) {
				ProductModel productModel = mapper.map(productEntity, ProductModel.class);
				modelList.add(productModel);

				//System.out.println(modelList);
			}

		}else {
			throw new ResourceNotExistException("product not found");
		}
		return modelList;

	}

	@Override
	public List<ProductModel> getProductStartWith(String expression) {
		List<ProductEntity> entityList = dao.getProductStartWith(expression);
		List<ProductModel> modelList = new ArrayList<ProductModel>();
		for (ProductEntity productEntity : entityList) {
			ProductModel productModel = mapper.map(productEntity, ProductModel.class);
			modelList.add(productModel);
		}
		return modelList;
	}

	@Override
	public double productPriceAverage() {
		double productEntity = dao.productPriceAverage();
		if(productEntity > 0) {
			return productEntity;
		}else {
			throw new ResourceNotExistException("Product not found");
		}

	}

	@Override
	public double countOfTotalProducts() {
		double totalCount = dao.countOfTotalProducts();
		if(totalCount > 0) {
			return totalCount;
		}else {
			throw new ResourceNotExistException("Product not found");
		}


	}

	@Override
	public List<ProductModel> getAllProducts(long category, long supplier) {

		List<ProductEntity> entityList = dao.getAllProducts(category, supplier);
		List<ProductModel> modelList = new ArrayList<ProductModel>();
		if(!entityList.isEmpty()) {
			for (ProductEntity productEntity : entityList) {
				ProductModel productModel = mapper.map(productEntity, ProductModel.class);
				modelList.add(productModel);
			}
		}else {
			throw new ResourceNotExistException("Products not found");
		}
		return modelList;
	}

	@Override
	public List<ProductModel> getAllProducts(String supplier) {
		List<ProductEntity> entityList = dao.getAllProducts(supplier);
		List<ProductModel> modelList = new ArrayList <ProductModel>();
		if(!entityList.isEmpty()) {
			for (ProductEntity productEntity : entityList) {
				ProductModel productModel = mapper.map(productEntity, ProductModel.class);
				modelList.add(productModel);
			}

		}else {
			throw new ResourceNotExistException("Product not found");
		}
		return modelList;

	}

	@Override
	public ProductModel getDetailsOfProduct(long productId) {
		ProductEntity productEntity = dao.getDetailsOfProduct(productId);
		ProductModel map = mapper.map(productEntity, ProductModel.class);
		return map;
	}

	private List<ProductModel> readExcel(String filePath) {
		List<ProductModel> list = new ArrayList<>();
		try {
			//pointOut file
			//FileInputStream fis = new FileInputStream(new File(filePath));  OR

			FileInputStream fis = new FileInputStream(filePath);

			//workbook
			Workbook workbook = WorkbookFactory.create(fis);

			//Sheet
			Sheet sheet = workbook.getSheetAt(0);
			totalRecords = sheet.getLastRowNum();

			//Rows
			Iterator<Row> rows = sheet.rowIterator();

			//iterate rows and pointout Row
			while (rows.hasNext()) {
				Row row = rows.next();
				int rowNum = row.getRowNum();
				if(rowNum==0) {
					continue;
				}
				ProductModel productModel = new ProductModel();
				String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
				productModel.setProductId(Long.parseLong(timeStamp)+rowNum);

				//iterate row and pointout every cell
				Iterator<Cell> cells = row.cellIterator();

				while (cells.hasNext()) {
					Cell cell = cells.next();

					//get data
					//					CellType cellType = cell.getCellType();
					//					
					//					if(cellType==cellType.STRING) {
					//						System.out.println(cell.getStringCellValue()); 
					//					}else {
					//					 System.out.println(cell.getNumericCellValue()); 

					int columnIndex = cell.getColumnIndex();
					switch (columnIndex) {
					case 0:
						String productName = cell.getStringCellValue();
						productModel.setProductName(productName);
						break;
					case 1:
						double supplierId = cell.getNumericCellValue();

						productModel.setSupplierId((long)cell.getNumericCellValue());
						break;
					case 2:
						double categoryId = cell.getNumericCellValue();

						productModel.setCategoryId((long)cell.getNumericCellValue());
						break;
					case 3:
						double productQty = cell.getNumericCellValue();
						productModel.setProductQty((int)productQty);

						break;
					case 4:
						double productPrice = cell.getNumericCellValue();
						productModel.setProductPrice(productPrice);
						break;
					case 5:
						double deliveryCharges = cell.getNumericCellValue();
						productModel.setDeliveryCharges((int)deliveryCharges);
						break;

					}
				}
				//check data is proper or not
				validationMap = validator.validateProduct(productModel);
				if(!validationMap.isEmpty()) {
					//add validation map with row number
					rowError.put(rowNum+1, validationMap);
				}else {
					//check it is exist or not in DB
					ProductModel dbProduct = getProductByName(productModel.getProductName());
					if(dbProduct!=null) {
						rowNumList.add(rowNum+1);
					}else {
						//add product into list
						list.add(productModel);
					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;

	}

	@Override
	public Map<String, Object> uploadSheet(MultipartFile myfile) {
		String msg = " ";
		int isAddedCounter=0;
		int alreadyExistCounter=0;
		int issueCounter=0;
		try {
			String path = "uploaded";
			String fileName = myfile.getOriginalFilename();
			FileOutputStream fos = new FileOutputStream(path+File.separator+fileName);
			byte[] data = myfile.getBytes();
			fos.write(data);

			//Read excel file
			List<ProductModel> list = readExcel(path+File.separator+fileName);

			for (ProductModel productModel : list) {
				ProductEntity productEntity = mapper.map(productModel, ProductEntity.class);
				try {
					boolean isAdded = dao.addProduct(productEntity);
					if(isAdded) {
						isAddedCounter=isAddedCounter+1;
					}
				} catch (ResourceAlreadyExistException e) {
					alreadyExistCounter=alreadyExistCounter+1;

				}catch(SomethingWentWrongException e) {
					issueCounter=issueCounter+1;
				}
			}

			finalMap.put("Total Records In Sheet", totalRecords);
			finalMap.put("Uploaded Records In BD", isAddedCounter);
			finalMap.put("Total Exist Records In DB", rowNumList.size());
			finalMap.put("Row num, Exist Records In DB", rowNumList);
			finalMap.put("Total Excluded Record count", rowError.size());
			finalMap.put("Bad Record Row Number", rowError);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return finalMap;
	}

	@Override
	public Product_Supplier_Category getProductWithSCByPId(long productId) {
		
		//This is a normal code
		
		//		ProductModel productModel = getProductById(productId);
		//		
		//		SupplierModel supplierModel = restTemplate.getForObject("http://localhost:8082/supplier/get-supplier-by-id/"+productModel.getSupplierId(), SupplierModel.class);
		//		
		//		CategoryModel catgoryModel = restTemplate.getForObject("http://localhost:8083/category/get-category-by-id/"+productModel.getCategoryId(), CategoryModel.class);
		//		
		//		
		//		Product_Supplier_Category psc = new Product_Supplier_Category(productModel, supplierModel, catgoryModel);
		//		return psc;
		
		// This is the code of exception handling 
		Product_Supplier_Category psc = null;
		ProductModel productModel = getProductById(productId);
		if(productModel != null) {
			psc = new Product_Supplier_Category();
			psc.setProductModel(productModel);
			try {
				SupplierModel supplierModel = restTemplate.getForObject("http://localhost:8082/supplier/get-supplier-by-id/"+productModel.getSupplierId(), SupplierModel.class);
				if(supplierModel.getSupplierId() <= 0) {
					psc.setSupplierModel(null);
				}else {
				psc.setSupplierModel(supplierModel);
				}
			} catch (ResourceAccessException e) {
				psc.setSupplierModel(null);
			}
			try {
				CategoryModel categoryModel = restTemplate.getForObject("http://localhost:8083/category/get-category-by-id/"+productModel.getCategoryId(), CategoryModel.class);
				if(categoryModel.getCategoryId() <= 0) {
					psc.setCategoryModel(null);
				}else {
				psc.setCategoryModel(categoryModel);
				}
			} catch (ResourceAccessException e) {
				psc.setCategoryModel(null);
			}
		}else {

		}
		return psc;
	}






}
