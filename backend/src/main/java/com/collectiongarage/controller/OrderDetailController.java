package com.collectiongarage.controller;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.collectiongarage.model.Order;
import com.collectiongarage.model.OrderDetail;
import com.collectiongarage.repository.ICustomerRepository;
import com.collectiongarage.repository.IOrderDetailRepository;
import com.collectiongarage.repository.IOrderRepository;
import com.collectiongarage.repository.IProductRepository;
import com.collectiongarage.repository.UserRepository;
import com.collectiongarage.security.JwtUtil;
import com.collectiongarage.security.UserPrincipal;

@RestController
@CrossOrigin
@RequestMapping("/")
public class OrderDetailController {
	@Autowired
	IProductRepository productRepository;

	@Autowired
	IOrderRepository orderRepository;

	@Autowired
	IOrderDetailRepository orderDetailRepository;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	ICustomerRepository customerRepository;

	@Autowired
	private UserRepository userRepository;

	/**
	 * @param page
	 * @return orderdetails list (all or by page)
	 */
	@GetMapping("/orderdetails")
	@PreAuthorize("hasAnyAuthority('USER_READ')")
	public ResponseEntity<Object> getAllOrderDetails(@RequestParam(required = false) Integer page) {
		try {
			if (null == page) {
				return new ResponseEntity<>(orderDetailRepository.findAll(), HttpStatus.OK);
			}

			return new ResponseEntity<>(orderDetailRepository.findAll(PageRequest.of(page, 10)).getContent(),
					HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param orderdetailid
	 * @return orderdetail object
	 */
	@GetMapping("/orderdetails/{orderdetailid}")
	@PreAuthorize("hasAnyAuthority('USER_READ')")
	public ResponseEntity<Object> getOrderDetailById(@PathVariable Integer orderdetailid) {
		try {
			if (!orderDetailRepository.existsById(orderdetailid)) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			return new ResponseEntity<>(orderDetailRepository.findById(orderdetailid), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param orderid
	 * @return orderdetails list (by orderid)
	 */
	@GetMapping("/orders/{orderid}/orderdetails")
	@PreAuthorize("hasAnyAuthority('USER_READ')")
	public ResponseEntity<Object> getAllOrderDetailsOfOrderId(@PathVariable Integer orderid) {
		try {
			if (!orderRepository.existsById(orderid)) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			return new ResponseEntity<>(orderRepository.findById(orderid).get().getOrderDetails(), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param productid
	 * @return orderdetails list (by orderid)
	 */
	@GetMapping("/products/{productid}/orderdetails")
	@PreAuthorize("hasAnyAuthority('USER_READ')")
	public ResponseEntity<Object> getAllOrderDetailsOfProductId(@PathVariable Integer productid) {
		try {
			if (!productRepository.existsById(productid)) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			return new ResponseEntity<>(productRepository.findById(productid).get().getOrderDetails(), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param orderid
	 * @param productid
	 * @param cOrderDetail
	 * @return new orderdetail object saving
	 */
	@PostMapping("/orderdetails")
	@PreAuthorize("hasAnyAuthority('USER_CREATE')")
	public ResponseEntity<Object> createOrderDetail(@RequestParam Integer orderid, @RequestParam Integer productid,
			@Valid @RequestBody OrderDetail cOrderDetail) {
		try {
			if (!orderRepository.existsById(orderid)) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			if (!productRepository.existsById(productid)) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			OrderDetail newOrderDetail = new OrderDetail();
			newOrderDetail.setOrder(orderRepository.findById(orderid).get());
			newOrderDetail.setProduct(productRepository.findById(productid).get());
			newOrderDetail.setQuantityOrder(cOrderDetail.getQuantityOrder());
			newOrderDetail.setPriceEach(cOrderDetail.getPriceEach());

			return new ResponseEntity<>(orderDetailRepository.save(newOrderDetail), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param customerid
	 * @param cOrder
	 * @return new order object saving
	 */
	@PostMapping("/customers/orders/orderdetails")
	public ResponseEntity<Object> createOrderDetailByCustomer(@RequestHeader("Authorization") String token,
			@Valid @RequestBody Order cOrder) {
		try {
			String tokenName = "Token ";

			if (null == token || !token.startsWith(tokenName)) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}

			String tokenValue = token.substring(tokenName.length());
			UserPrincipal userPrincipal = jwtUtil.getUserFromToken(tokenValue);

			if (null == userPrincipal) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}

			Order newOrder = new Order();
			newOrder.setOrderDate(new Date());
			newOrder.setRequiredDate(cOrder.getRequiredDate());
			newOrder.setCustomer(userRepository.findById(userPrincipal.getUserId()).get().getCustomer());
			Order saveNewOrder = orderRepository.save(newOrder);

			for (OrderDetail orderDetailElement : cOrder.getOrderDetails()) {
				OrderDetail newOrderDetail = new OrderDetail();
				newOrderDetail.setOrder(saveNewOrder);
				newOrderDetail.setProduct(productRepository.findById(orderDetailElement.productId).get());
				newOrderDetail.setQuantityOrder(orderDetailElement.getQuantityOrder());
				newOrderDetail.setPriceEach(orderDetailElement.getPriceEach());
				orderDetailRepository.save(newOrderDetail);
			}

			return new ResponseEntity<>(orderRepository.findById(saveNewOrder.getId()), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param orderdetailid
	 * @param cOrderDetail
	 * @param changedorderid
	 * @param changedproductid
	 * @return orderdetail object update saving
	 */
	@PutMapping("/orderdetails/{orderdetailid}")
	@PreAuthorize("hasAnyAuthority('USER_UPDATE')")
	public ResponseEntity<Object> updateOrderDetail(@PathVariable Integer orderdetailid,
			@Valid @RequestBody OrderDetail cOrderDetail, @RequestParam(required = false) Integer changedorderid,
			@RequestParam(required = false) Integer changedproductid) {
		try {
			if (!orderDetailRepository.existsById(orderdetailid)) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			if (changedorderid != null && !orderRepository.existsById(changedorderid)) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			if (changedproductid != null && !productRepository.existsById(changedproductid)) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			OrderDetail updateOrderDetail = orderDetailRepository.findById(orderdetailid).get();
			if (changedorderid != null && orderRepository.existsById(changedorderid)) {
				updateOrderDetail.setOrder(orderRepository.findById(changedorderid).get());
			}
			if (changedproductid != null && productRepository.existsById(changedproductid)) {
				updateOrderDetail.setProduct(productRepository.findById(changedproductid).get());
			}
			updateOrderDetail.setQuantityOrder(cOrderDetail.getQuantityOrder());
			updateOrderDetail.setPriceEach(cOrderDetail.getPriceEach());

			return new ResponseEntity<>(orderDetailRepository.save(updateOrderDetail), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param orderdetailid
	 * @return null NO_CONTENT
	 */
	@DeleteMapping("/orderdetails/{orderdetailid}")
	@PreAuthorize("hasAnyAuthority('USER_DELETE')")
	public ResponseEntity<Object> deleteOrderDetailById(@PathVariable Integer orderdetailid) {
		try {
			if (!orderDetailRepository.existsById(orderdetailid)) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			orderDetailRepository.deleteById(orderdetailid);

			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @return null NO_CONTENT
	 */
	@DeleteMapping("/orderdetails")
	@PreAuthorize("hasAnyAuthority('USER_DELETE')")
	public ResponseEntity<Object> deleteAllOrderDetails() {
		try {
			orderDetailRepository.deleteAll();

			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

}
