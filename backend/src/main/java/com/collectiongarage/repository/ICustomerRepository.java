package com.collectiongarage.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.collectiongarage.model.Customer;

public interface ICustomerRepository extends JpaRepository<Customer, Integer> {
	Customer findByPhoneNumber(String phoneNumber);

	@Query(value = "SELECT c.id customerId,c.last_name lastName,c.first_name firstName,CONCAT(c.first_name, ' ', c.last_name) customerFullName,c.phone_number phoneNumber,o.id orderId,o.order_date orderDate,o.required_date requiredDate,o.shipped_date shippedDate,o.status orderStatus,od.id orderDetailId,od.quantity_order quantityOrder,od.price_each priceEach, od.quantity_order*od.price_each total, SUM(od.quantity_order) totalOrder, SUM(od.quantity_order*od.price_each) totalRevenue FROM customers c INNER JOIN orders o ON o.customer_id = c.id INNER JOIN order_details od ON od.order_id = o.id GROUP BY customerId ORDER BY customerId", nativeQuery = true)
	Collection<IReport> selectReportCustomers();

	@Query(value = "SELECT c.id customerId,c.last_name lastName,c.first_name firstName,CONCAT(c.first_name, ' ', c.last_name) customerFullName,c.phone_number phoneNumber,o.id orderId,o.order_date orderDate,o.required_date requiredDate,o.shipped_date shippedDate,o.status orderStatus,od.id orderDetailId,od.quantity_order quantityOrder,od.price_each priceEach, od.quantity_order*od.price_each total, SUM(od.quantity_order) totalOrder, SUM(od.quantity_order*od.price_each) totalRevenue FROM customers c INNER JOIN orders o ON o.customer_id = c.id INNER JOIN order_details od ON od.order_id = o.id GROUP BY customerId HAVING totalRevenue > :revenue1 AND totalRevenue <= :revenue2 ORDER BY customerId", nativeQuery = true)
	Collection<IReport> selectReportCustomersWithCustomerType(@Param("revenue1") Integer revenue1,
			@Param("revenue2") Integer revenue2);

	@Query(value = "SELECT c.id customerId,c.last_name lastName,c.first_name firstName,CONCAT(c.first_name, ' ', c.last_name) customerFullName,c.phone_number phoneNumber,o.id orderId,o.order_date orderDate,o.required_date requiredDate,o.shipped_date shippedDate,o.status orderStatus,od.id orderDetailId,od.quantity_order quantityOrder,od.price_each priceEach, od.quantity_order*od.price_each total, SUM(od.quantity_order) totalOrder, SUM(od.quantity_order*od.price_each) totalRevenue FROM customers c INNER JOIN orders o ON o.customer_id = c.id INNER JOIN order_details od ON od.order_id = o.id GROUP BY customerId HAVING totalRevenue > 500000 ORDER BY customerId", nativeQuery = true)
	Collection<IReport> selectReportCustomersPlatinum();

	@Query(value = "SELECT c.id customerId,c.last_name lastName,c.first_name firstName,CONCAT(c.first_name, ' ', c.last_name) customerFullName,c.phone_number phoneNumber,o.id orderId,o.order_date orderDate,o.required_date requiredDate,o.shipped_date shippedDate,o.status orderStatus,od.id orderDetailId,od.quantity_order quantityOrder,od.price_each priceEach, od.quantity_order*od.price_each total, SUM(od.quantity_order) totalOrder, SUM(od.quantity_order*od.price_each) totalRevenue FROM customers c INNER JOIN orders o ON o.customer_id = c.id INNER JOIN order_details od ON od.order_id = o.id GROUP BY customerId HAVING totalOrder >= :totalorder ORDER BY customerId", nativeQuery = true)
	Collection<IReport> selectReportCustomersWithOrder(@Param("totalorder") Integer totalorder);

	@Query(value = "SELECT c.id customerId,c.last_name lastName,c.first_name firstName,CONCAT(c.first_name, ' ', c.last_name) customerFullName,c.phone_number phoneNumber,o.id orderId,o.order_date orderDate,o.required_date requiredDate,o.shipped_date shippedDate,o.status orderStatus,od.id orderDetailId,od.quantity_order quantityOrder,od.price_each priceEach, od.quantity_order*od.price_each total, SUM(od.quantity_order) totalOrder, SUM(od.quantity_order*od.price_each) totalRevenue FROM customers c INNER JOIN orders o ON o.customer_id = c.id INNER JOIN order_details od ON od.order_id = o.id GROUP BY customerId HAVING totalRevenue > :revenue1 AND totalRevenue <= :revenue2 AND totalOrder >= :totalorder ORDER BY customerId", nativeQuery = true)
	Collection<IReport> selectReportCustomersWithCustomerTypeNOrder(@Param("revenue1") Integer revenue1,
			@Param("revenue2") Integer revenue2, @Param("totalorder") Integer totalorder);

	@Query(value = "SELECT c.id customerId,c.last_name lastName,c.first_name firstName,CONCAT(c.first_name, ' ', c.last_name) customerFullName,c.phone_number phoneNumber,o.id orderId,o.order_date orderDate,o.required_date requiredDate,o.shipped_date shippedDate,o.status orderStatus,od.id orderDetailId,od.quantity_order quantityOrder,od.price_each priceEach, od.quantity_order*od.price_each total, SUM(od.quantity_order) totalOrder, SUM(od.quantity_order*od.price_each) totalRevenue FROM customers c INNER JOIN orders o ON o.customer_id = c.id INNER JOIN order_details od ON od.order_id = o.id GROUP BY customerId HAVING totalRevenue > 500000 AND totalOrder >= :totalorder ORDER BY customerId", nativeQuery = true)
	Collection<IReport> selectReportCustomersPlatinumWithOrder(@Param("totalorder") Integer totalorder);
}
