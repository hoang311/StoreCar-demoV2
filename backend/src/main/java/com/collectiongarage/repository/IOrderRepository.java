package com.collectiongarage.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.collectiongarage.model.Order;

public interface IOrderRepository extends JpaRepository<Order, Integer> {
	@Query(value = "SELECT c.id customerId,c.last_name lastName,c.first_name firstName,CONCAT(c.first_name, ' ', c.last_name) customerFullName,c.phone_number phoneNumber,o.id orderId,MONTH(o.order_date) month,WEEK(o.order_date) week,o.order_date orderDate,o.required_date requiredDate,o.shipped_date shippedDate,o.status orderStatus,od.id orderDetailId,od.quantity_order quantityOrder,od.price_each priceEach, od.quantity_order*od.price_each total, SUM(od.quantity_order) totalOrder, SUM(od.quantity_order*od.price_each) totalRevenue FROM customers c INNER JOIN orders o ON o.customer_id = c.id INNER JOIN order_details od ON od.order_id = o.id WHERE o.order_date BETWEEN :startdate AND :enddate GROUP BY orderDate ORDER BY orderDate;", nativeQuery = true)
	Collection<IReport> selectReportOrdersByDate(@Param("startdate") String startdate,
			@Param("enddate") String enddate);

	@Query(value = "SELECT c.id customerId,c.last_name lastName,c.first_name firstName,CONCAT(c.first_name, ' ', c.last_name) customerFullName,c.phone_number phoneNumber,o.id orderId,MONTH(o.order_date) month,WEEK(o.order_date) week,o.order_date orderDate,o.required_date requiredDate,o.shipped_date shippedDate,o.status orderStatus,od.id orderDetailId,od.quantity_order quantityOrder,od.price_each priceEach, od.quantity_order*od.price_each total, SUM(od.quantity_order) totalOrder, SUM(od.quantity_order*od.price_each) totalRevenue FROM customers c INNER JOIN orders o ON o.customer_id = c.id INNER JOIN order_details od ON od.order_id = o.id WHERE o.order_date BETWEEN :startdate AND :enddate GROUP BY year(o.order_date),month,week ORDER BY orderDate;", nativeQuery = true)
	Collection<IReport> selectReportOrdersByWeek(@Param("startdate") String startdate,
			@Param("enddate") String enddate);

	@Query(value = "SELECT c.id customerId,c.last_name lastName,c.first_name firstName,CONCAT(c.first_name, ' ', c.last_name) customerFullName,c.phone_number phoneNumber,o.id orderId,MONTH(o.order_date) month,WEEK(o.order_date) week,o.order_date orderDate,o.required_date requiredDate,o.shipped_date shippedDate,o.status orderStatus,od.id orderDetailId,od.quantity_order quantityOrder,od.price_each priceEach, od.quantity_order*od.price_each total, SUM(od.quantity_order) totalOrder, SUM(od.quantity_order*od.price_each) totalRevenue FROM customers c INNER JOIN orders o ON o.customer_id = c.id INNER JOIN order_details od ON od.order_id = o.id WHERE o.order_date BETWEEN :startdate AND :enddate GROUP BY year(o.order_date),month ORDER BY orderDate;", nativeQuery = true)
	Collection<IReport> selectReportOrdersByMonth(@Param("startdate") String startdate,
			@Param("enddate") String enddate);
}
