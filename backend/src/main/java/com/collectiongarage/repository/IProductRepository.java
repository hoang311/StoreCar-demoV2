package com.collectiongarage.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.collectiongarage.model.Product;

public interface IProductRepository extends JpaRepository<Product, Integer> {
	Page<Product> findByProductLineId(Integer productlineid, Pageable pageable);

	@Query(value = "SELECT p.* FROM products p WHERE p.product_line_id IN :productlineids", nativeQuery = true)
	List<Product> selectAllProductsOfProductLines(@Param("productlineids") Collection<Integer> productlineids);

	@Query(value = "SELECT p.* FROM products p WHERE p.product_line_id IN :productlineids", countQuery = "SELECT COUNT(*) FROM products p WHERE p.product_line_id IN :productlineids", nativeQuery = true)
	Page<Product> selectAllProductsOfProductLinesPagination(@Param("productlineids") Collection<Integer> productlineids,
			Pageable pageable);

	@Query(value = "SELECT p.* FROM products p WHERE p.product_name LIKE %:productnamesearch%", countQuery = "SELECT COUNT(*) FROM products p WHERE p.product_name LIKE %:productnamesearch%", nativeQuery = true)
	Page<Product> selectProductNameLikePagination(@Param("productnamesearch") String productnamesearch,
			Pageable pageable);

	@Query(value = "SELECT *, SUM(od.quantity_order) sumquanod FROM products p JOIN order_details od WHERE p.id = od.product_id AND p.product_line_id = :productlineid GROUP BY p.id ORDER BY sumquanod DESC LIMIT 3", nativeQuery = true)
	List<Product> selectProductsHighestQuantityOrderSameProductLineId(@Param("productlineid") Integer productlineid);

	@Query(value = "(SELECT p.product_line_id productLineId,pl.product_line productLine,od.product_id productId,p.product_code productCode,p.product_name productName,p.product_description productDescription,p.product_vendor productVendor,p.buy_price buyPrice,od.order_id orderId,od.price_each priceEach,od.id orderDetailId\n"
			+ "FROM products p \n" + "JOIN product_lines pl ON p.product_line_id = pl.id \n"
			+ "JOIN order_details od ON p.id = od.product_id\n" + "WHERE p.product_name LIKE \"%Ro%\"\n"
			+ "ORDER BY od.price_each DESC\n" + "LIMIT 1)\n" + "UNION\n"
			+ "(SELECT p.product_line_id productLineId,pl.product_line productLine,od.product_id productId,p.product_code productCode,p.product_name productName,p.product_description productDescription,p.product_vendor productVendor,p.buy_price buyPrice,od.order_id orderId,od.price_each priceEach,od.id orderDetailId\n"
			+ "FROM products p \n" + "JOIN product_lines pl ON p.product_line_id = pl.id \n"
			+ "JOIN order_details od ON p.id = od.product_id\n" + "WHERE p.product_name LIKE \"%Renault%\"\n"
			+ "ORDER BY od.price_each DESC\n" + "LIMIT 1)\n" + "UNION\n"
			+ "(SELECT p.product_line_id productLineId,pl.product_line productLine,od.product_id productId,p.product_code productCode,p.product_name productName,p.product_description productDescription,p.product_vendor productVendor,p.buy_price buyPrice,od.order_id orderId,od.price_each priceEach,od.id orderDetailId\n"
			+ "FROM products p \n" + "JOIN product_lines pl ON p.product_line_id = pl.id \n"
			+ "JOIN order_details od ON p.id = od.product_id\n" + "WHERE product_name LIKE \"%Ford Mustang%\"\n"
			+ "ORDER BY price_each DESC\n" + "LIMIT 1)\n" + "UNION\n"
			+ "(SELECT p.product_line_id productLineId,pl.product_line productLine,od.product_id productId,p.product_code productCode,p.product_name productName,p.product_description productDescription,p.product_vendor productVendor,p.buy_price buyPrice,od.order_id orderId,od.price_each priceEach,od.id orderDetailId\n"
			+ "FROM products p \n" + "JOIN product_lines pl ON p.product_line_id = pl.id \n"
			+ "JOIN order_details od ON p.id = od.product_id\n" + "WHERE product_name LIKE \"%ver%\"\n"
			+ "ORDER BY price_each DESC\n" + "LIMIT 1)\n" + "UNION\n"
			+ "(SELECT p.product_line_id productLineId,pl.product_line productLine,od.product_id productId,p.product_code productCode,p.product_name productName,p.product_description productDescription,p.product_vendor productVendor,p.buy_price buyPrice,od.order_id orderId,od.price_each priceEach,od.id orderDetailId\n"
			+ "FROM products p \n" + "JOIN product_lines pl ON p.product_line_id = pl.id \n"
			+ "JOIN order_details od ON p.id = od.product_id\n" + "WHERE product_name LIKE \"%Porsche%\"\n"
			+ "ORDER BY price_each DESC\n" + "LIMIT 2)\n" + "UNION\n"
			+ "(SELECT p.product_line_id productLineId,pl.product_line productLine,od.product_id productId,p.product_code productCode,p.product_name productName,p.product_description productDescription,p.product_vendor productVendor,p.buy_price buyPrice,od.order_id orderId,od.price_each priceEach,od.id orderDetailId\n"
			+ "FROM products p \n" + "JOIN product_lines pl ON p.product_line_id = pl.id \n"
			+ "JOIN order_details od ON p.id = od.product_id\n" + "WHERE product_name LIKE \"%Chevrolet%\"\n"
			+ "ORDER BY price_each DESC\n" + "LIMIT 1)\n" + "UNION\n"
			+ "(SELECT p.product_line_id productLineId,pl.product_line productLine,od.product_id productId,p.product_code productCode,p.product_name productName,p.product_description productDescription,p.product_vendor productVendor,p.buy_price buyPrice,od.order_id orderId,od.price_each priceEach,od.id orderDetailId\n"
			+ "FROM products p \n" + "JOIN product_lines pl ON p.product_line_id = pl.id \n"
			+ "JOIN order_details od ON p.id = od.product_id\n" + "WHERE product_name LIKE \"%Mercedes%\"\n"
			+ "ORDER BY price_each DESC\n" + "LIMIT 1);", nativeQuery = true)
	Collection<IPopularProductsWithProductLineOrderDetailInfo> selectPopularProducts();
}
