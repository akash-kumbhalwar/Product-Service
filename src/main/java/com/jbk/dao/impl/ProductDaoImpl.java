package com.jbk.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.RollbackException;
import javax.transaction.Transaction;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.AggregateProjection;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.jbk.dao.ProductDao;
import com.jbk.entity.ProductEntity;
import com.jbk.exception.ResourceAlreadyExistException;
import com.jbk.exception.ResourceNotExistException;
import com.jbk.exception.SomethingWentWrongException;
import com.jbk.model.ProductModel;

//@Component
@Repository
public class ProductDaoImpl implements ProductDao {

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public boolean addProduct(ProductEntity productEntity) {
		boolean isAdded=false;

		try (Session session = sessionFactory.openSession()){


			ProductEntity dbProduct = getProductByName(productEntity.getProductName());
			if(dbProduct==null) {
				session.save(productEntity);
				session.beginTransaction().commit();
				isAdded=true;
				//id session is null 
			}else {
				throw new ResourceAlreadyExistException("Product already exist with Id:-"+productEntity.getProductId());
			}

		} catch (RollbackException e) {
			e.printStackTrace();
			throw new SomethingWentWrongException("Something went wrong during adding product");
		}

		return isAdded;

	}

	@Override
	public ProductEntity getProductById(long productId) {
		ProductEntity productEntity=null;
		try {
			Session session = sessionFactory.openSession();
			productEntity = session.get(ProductEntity.class, productId);
			session.beginTransaction().commit();

		} catch (Exception e) {
			e.printStackTrace();
			throw new SomethingWentWrongException("Something went wrong during get product");
		}
		return productEntity;

	}

	@Override
	public boolean deleteProductById(long productId) {
		boolean isDeleted=false;
		try {
			Session session = sessionFactory.openSession();
			ProductEntity productEntity = session.get(ProductEntity.class, productId);
			if(productEntity != null) {
				session.delete(productEntity);
				session.beginTransaction().commit();
				isDeleted=true;
			}else {
				throw new ResourceNotExistException("Product nor found with Id:-"+productId);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new SomethingWentWrongException("Something went wrong during delete");
		}
		return isDeleted;

	}

	@Override
	public boolean updateProduct(ProductEntity productEntity) {
		boolean isUpdated=false;
		try {
			Session session = sessionFactory.openSession();
			ProductEntity dbProduct = getProductById(productEntity.getProductId());
			if(dbProduct != null) {
				session.update(productEntity);
				session.beginTransaction().commit();
				isUpdated=true;
			}else {
				throw new ResourceNotExistException("Product not found");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SomethingWentWrongException("Something went wrong during update product");

		}
		return isUpdated;

	}

	//get all the products by using criteria
	@Override
	public List<ProductEntity> getAllProducts() {
		List<ProductEntity> list=null;
		try {
			Session session = sessionFactory.openSession();
			//criteria
			Criteria criteria = session.createCriteria(ProductEntity.class);
			list = criteria.list();   //select * from product

		} catch (Exception e) {
			e.printStackTrace();
			throw new SomethingWentWrongException("Something went wrong during retrive all product");
		}

		return list;

	}

	@Override
	public List<ProductEntity> sortProduct(String orderType, String property) {
		List<ProductEntity> list=null;
		try {
			Session session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(ProductEntity.class);
			if(orderType.equalsIgnoreCase("desc")) {
				criteria.addOrder(Order.desc(property));
			}else {
				criteria.addOrder(Order.asc(property));
			}
			list=criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
			throw new SomethingWentWrongException("Something went wrong during sort Product");
		}
		return list;
	}

	@Override
	public double getMaxProductPrice() {
		double maxPrice=0;
		try {
			Session session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(ProductEntity.class);
			Projection productPriceProjection = Projections.max("productPrice");
			criteria.setProjection(productPriceProjection); //select max(productPrice) from productEntity;
			List list = criteria.list();
			if(!list.isEmpty()) {
				maxPrice=(double)list.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SomethingWentWrongException("Something went wrong during max product price");
		}
		return maxPrice;
	}

	@Override
	public List<ProductEntity> getMaxPriceProduct() {
		double maxProductPrice = getMaxProductPrice();
		List<ProductEntity> list = null;
		try {
			if(maxProductPrice > 0) {
				//find max price product
				Session session = sessionFactory.openSession();
				Criteria criteria = session.createCriteria(ProductEntity.class);
				//select * from product where productPrice=?
				criteria.add(Restrictions.eq("productPrice", maxProductPrice)); // filter query
				list=criteria.list();
			}else {
				throw new ResourceNotExistException("Product not found");
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new SomethingWentWrongException("Something went wrong during max price product");
		}
		return list;
	}


	@Override
	public ProductEntity getProductByName(String productName) {

		//This is normal query//
		//select * from product where productName=?  we can use here Restrictions

		//This is H query i.e. HSL//
		//HQL: from ProductEntity where productNam= :parametername (parametername : pname)
		List<ProductEntity> list=null;
		ProductEntity productEntity=null;
		try (Session session = sessionFactory.openSession()) {
			Query<ProductEntity> query = session.createQuery("FROM ProductEntity WHERE productName=:name");
			query.setParameter("name", productName);
			list = query.list();
			if(!list.isEmpty()) {
				productEntity = list.get(0);

			}else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SomethingWentWrongException("Something went wrong during get product by name");
		}
		return productEntity;

	}

	@Override
	public List<ProductEntity> getAllProducts(double lowPrice, double highPrice) {
		List<ProductEntity> list = null;
		try {
			Session session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(ProductEntity.class);
			Criteria productEntity = criteria.add(Restrictions.between("productPrice", lowPrice, highPrice));
			list=productEntity.list();
			//System.out.println(list);

		} catch (Exception e) {
			e.printStackTrace();
			throw new SomethingWentWrongException("Something went wrong during low and high price");
		}
		return list;


	}
	

	@Override
	public List<ProductEntity> getProductStartWith(String expression) {
		List <ProductEntity> list=null;
		try {
			Session session = sessionFactory.openSession();
//			Query<ProductEntity> query= session.createQuery("FROM product WHERE productName LIKE : name%");
//			query.setParameter("name%", expression);
//			list=query.list();
//			if(!list.isEmpty()) {
//				return list;
//			}else {
//				throw new ResourceNotExistException("Products not found");
//			}
			Criteria criteria = session.createCriteria(ProductEntity.class);
			Criteria productEntity = criteria.add(Restrictions.like("productName", "expression%", MatchMode.START));
			list=productEntity.list();
			

		} catch (Exception e) {
			e.printStackTrace();
			throw new SomethingWentWrongException("Something went wrong during product starts with");
		}
		return list;


	}

	@Override
	public double productPriceAverage() {
		double avgPrice=0;
		try {
			Session session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(ProductEntity.class);
			Criteria setProjection = criteria.setProjection(Projections.avg("productPrice"));
			List list = setProjection.list();
			if(!list.isEmpty()) {
				avgPrice=(double)list.get(0);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new SomethingWentWrongException("Something went wrong during average price");
		}
		return avgPrice;
	}

	@Override
	public double countOfTotalProducts() {
		double totalCount=0;
		try {
			Session session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(ProductEntity.class);
			Criteria rowCount = criteria.setProjection(Projections.rowCount());
			List list=rowCount.list();
			if(!list.isEmpty()) {
				totalCount=(double)list.get(0);
				System.out.println(totalCount);
			}
			
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			throw new SomethingWentWrongException("Something went wrong during total count of products");
		}

		return totalCount;
	}

	@Override
	public List<ProductEntity> getAllProducts(long category, long supplier) {
		List<ProductEntity> list=null;
		try {
			Session session = sessionFactory.openSession();
			Query<ProductEntity> query = session.createQuery("FROM ProductEntity WHERE category.categoryId = :categoryId AND supplier.supplierId = :supplierId");
			query.setParameter("categoryId", category);
			query.setParameter("supplierId", supplier);
			list=query.list();
			System.out.println(list);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SomethingWentWrongException("Something went wrong during get products by category and supplier");
		}

		return list;
	}

	@Override
	public List<ProductEntity> getAllProducts(String supplier) {
		List<ProductEntity> list=null;
		try {
			Session session = sessionFactory.openSession();
			Query<ProductEntity> query = session.createQuery("FROM ProductEntity WHERE supplier.supplierId = (SELECT supplierId FROM SupplierEntity WHERE supplierName = :name)");
			query.setParameter("name", supplier);
			list=query.list();

		} catch (Exception e) {
			e.printStackTrace();
			throw new SomethingWentWrongException("Something went wrong during get all supplier");
		}
		return list;

	}

	@Override
	public ProductEntity getDetailsOfProduct(long productId) {
		ProductEntity productEntity=null;
		try {
			Session session = sessionFactory.openSession();
			Query<ProductEntity> query = session.createQuery("productId, productName, productPrice FROM ProductEntity WHERE ProductEntity.productId=:id");
			query.setParameter("id", productId);
			List list=query.list();
			if(!list.isEmpty()) {
				productEntity=(ProductEntity)list.get(0);
				System.out.println(productEntity);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new SomethingWentWrongException("Something went wrong during details of product");
		}
		return productEntity;
	}

}




