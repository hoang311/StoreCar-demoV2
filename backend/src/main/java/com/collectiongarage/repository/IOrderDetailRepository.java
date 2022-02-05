package com.collectiongarage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.collectiongarage.model.OrderDetail;

public interface IOrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
	@Query(value = "SELECT od.* FROM t_user tu JOIN customers c ON c.user_id = tu.id JOIN orders o ON o.customer_id = c.id JOIN order_details od ON od.order_id = o.id WHERE tu.id=:userid AND od.id=:orderdetailid", nativeQuery = true)
	OrderDetail selectOrderDetailOfUser(@Param("userid") Long userid, @Param("orderdetailid") Integer orderdetailid);

	@Query(value = "SELECT od.* FROM t_user tu JOIN customers c ON c.user_id = tu.id JOIN orders o ON o.customer_id = c.id JOIN order_details od ON od.order_id = o.id JOIN rating_and_review rnr ON rnr.orderdetail_id = od.id WHERE tu.id=:userid AND rnr.id=:ratingnreviewid", nativeQuery = true)
	OrderDetail selectRatingNReviewOfUser(@Param("userid") Long userid, @Param("ratingnreviewid") Long ratingnreviewid);
}
