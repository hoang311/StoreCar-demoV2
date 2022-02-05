package com.collectiongarage.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.collectiongarage.model.OrderDetail;
import com.collectiongarage.model.RatingAndReview;
import com.collectiongarage.repository.IOrderDetailRepository;
import com.collectiongarage.repository.IRatingAndReviewRepository;
import com.collectiongarage.security.JwtUtil;
import com.collectiongarage.security.UserPrincipal;

@RestController
@CrossOrigin
public class RatingAndReviewController {
	@Autowired
	IOrderDetailRepository orderDetailRepository;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	IRatingAndReviewRepository ratingAndReviewRepository;

	/**
	 * @param token
	 * @param orderdetailid
	 * @param cRatingAndReview
	 * @return orderdetail object create rating review
	 */
	@PostMapping("/customer/orderdetails/{orderdetailid}/ratingreview")
	public ResponseEntity<Object> createOrderDetailRatingNReviewByCustomer(@RequestHeader("Authorization") String token,
			@PathVariable Integer orderdetailid, @RequestBody RatingAndReview cRatingAndReview) {
		try {
			if (cRatingAndReview.getRating() == null) {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}

			String tokenName = "Token ";

			if (null == token || !token.startsWith(tokenName)) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}

			String tokenValue = token.substring(tokenName.length());
			UserPrincipal userPrincipal = jwtUtil.getUserFromToken(tokenValue);

			if (null == userPrincipal) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}

			OrderDetail foundOrderDetail = orderDetailRepository.selectOrderDetailOfUser(userPrincipal.getUserId(),
					orderdetailid);

			if (null == foundOrderDetail) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			RatingAndReview newRatingAndReview = new RatingAndReview();
			newRatingAndReview.setCreatedAt(new Date());
			newRatingAndReview.setCreatedBy(userPrincipal.getUserId());
			newRatingAndReview.setRating(cRatingAndReview.getRating());
			newRatingAndReview.setReview(cRatingAndReview.getReview());
			if (cRatingAndReview.getReview() != null) {
				newRatingAndReview.setReview(cRatingAndReview.getReview().trim());
			}
			newRatingAndReview.setOrderdetail(foundOrderDetail);
			ratingAndReviewRepository.save(newRatingAndReview);

			return new ResponseEntity<>(orderDetailRepository.findById(orderdetailid), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param token
	 * @param ratingreviewid
	 * @param cRatingAndReview
	 * @return orderdetail object update rating review
	 */
	@PutMapping("/customer/orderdetails/ratingreview/{ratingreviewid}")
	public ResponseEntity<Object> updateOrderDetailRatingNReviewByCustomer(@RequestHeader("Authorization") String token,
			@PathVariable Long ratingreviewid, @RequestBody RatingAndReview cRatingAndReview) {
		try {
			if (cRatingAndReview.getRating() == null) {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}

			String tokenName = "Token ";

			if (null == token || !token.startsWith(tokenName)) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}

			String tokenValue = token.substring(tokenName.length());
			UserPrincipal userPrincipal = jwtUtil.getUserFromToken(tokenValue);

			if (null == userPrincipal) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}

			OrderDetail foundOrderDetail = orderDetailRepository.selectRatingNReviewOfUser(userPrincipal.getUserId(),
					ratingreviewid);

			if (null == foundOrderDetail) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			RatingAndReview updateRatingAndReview = foundOrderDetail.getRatingAndReview();
			updateRatingAndReview.setUpdatedAt(new Date());
			updateRatingAndReview.setUpdatedBy(userPrincipal.getUserId());
			updateRatingAndReview.setRating(cRatingAndReview.getRating());
			updateRatingAndReview.setReview(cRatingAndReview.getReview());
			if (cRatingAndReview.getReview() != null) {
				updateRatingAndReview.setReview(cRatingAndReview.getReview().trim());
			}
			ratingAndReviewRepository.save(updateRatingAndReview);

			return new ResponseEntity<>(orderDetailRepository.findById(foundOrderDetail.getId()), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param ratingreviewid
	 * @return null NO_CONTENT
	 */
	@DeleteMapping("/ratingreview/{ratingreviewid}")
	public ResponseEntity<Object> deleteRatingNReviewById(@PathVariable Long ratingreviewid) {
		try {
			if (!ratingAndReviewRepository.existsById(ratingreviewid)) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			ratingAndReviewRepository.deleteById(ratingreviewid);

			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}

	}
}
