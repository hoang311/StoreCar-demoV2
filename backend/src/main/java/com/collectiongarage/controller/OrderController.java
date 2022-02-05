package com.collectiongarage.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.collectiongarage.model.Order;
import com.collectiongarage.model.TimeDuration;
import com.collectiongarage.repository.ICustomerRepository;
import com.collectiongarage.repository.IOrderRepository;
import com.collectiongarage.service.ExcelExporterOrders;

@RestController
@CrossOrigin
@RequestMapping("/")
public class OrderController {
	@Autowired
	ICustomerRepository customerRepository;

	@Autowired
	IOrderRepository orderRepository;

	/**
	 * @param page
	 * @return orders list (all or by page)
	 */
	@GetMapping("/customers/orders")
	@PreAuthorize("hasAnyAuthority('USER_READ')")
	public ResponseEntity<Object> getAllOrders(@RequestParam(required = false) Integer page) {
		try {
			if (page == null) {
				return new ResponseEntity<>(orderRepository.findAll(), HttpStatus.OK);
			}

			return new ResponseEntity<>(orderRepository.findAll(PageRequest.of(page, 10)).getContent(), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param orderid
	 * @return order object
	 */
	@GetMapping("/customers/orders/{orderid}")
	@PreAuthorize("hasAnyAuthority('USER_READ')")
	public ResponseEntity<Object> getOrderById(@PathVariable Integer orderid) {
		try {
			if (!orderRepository.existsById(orderid)) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			return new ResponseEntity<>(orderRepository.findById(orderid), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param customerid
	 * @return orders list (by customerid)
	 */
	@GetMapping("/customers/{customerid}/orders")
	@PreAuthorize("hasAnyAuthority('USER_READ')")
	public ResponseEntity<Object> getAllOrdersOfCustomerId(@PathVariable Integer customerid) {
		try {
			if (!customerRepository.existsById(customerid)) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			return new ResponseEntity<>(customerRepository.findById(customerid).get().getOrders(), HttpStatus.OK);
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
	@PostMapping("/customers/{customerid}/orders")
	@PreAuthorize("hasAnyAuthority('USER_CREATE')")
	public ResponseEntity<Object> createOrder(@PathVariable Integer customerid, @Valid @RequestBody Order cOrder) {
		try {
			if (!customerRepository.existsById(customerid)) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			Order newOrder = new Order();
			newOrder.setOrderDate(new Date());
			newOrder.setRequiredDate(cOrder.getRequiredDate());
			newOrder.setShippedDate(cOrder.getShippedDate());
			newOrder.setStatus(cOrder.getStatus());
			if (cOrder.getStatus() != null) {
				newOrder.setStatus(cOrder.getStatus().trim());
			}
			newOrder.setComments(cOrder.getComments());
			if (cOrder.getComments() != null) {
				newOrder.setComments(cOrder.getComments().trim());
			}
			newOrder.setStatus(cOrder.getStatus());
			newOrder.setCustomer(customerRepository.findById(customerid).get());

			return new ResponseEntity<>(orderRepository.save(newOrder), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param orderid
	 * @param cOrder
	 * @param changedcustomerid
	 * @return order object update saving
	 */
	@PutMapping("/customers/orders/{orderid}")
	@PreAuthorize("hasAnyAuthority('USER_UPDATE')")
	public ResponseEntity<Object> updateOrder(@PathVariable Integer orderid, @Valid @RequestBody Order cOrder,
			@RequestParam(required = false) Integer changedcustomerid) {
		try {
			if (!orderRepository.existsById(orderid)) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			if (changedcustomerid != null && !customerRepository.existsById(changedcustomerid)) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			Order updateOrder = orderRepository.findById(orderid).get();
			updateOrder.setRequiredDate(cOrder.getRequiredDate());
			updateOrder.setShippedDate(cOrder.getShippedDate());
			updateOrder.setStatus(cOrder.getStatus());
			if (cOrder.getStatus() != null) {
				updateOrder.setStatus(cOrder.getStatus().trim());
			}
			updateOrder.setComments(cOrder.getComments());
			if (cOrder.getComments() != null) {
				updateOrder.setComments(cOrder.getComments().trim());
			}
			updateOrder.setStatus(cOrder.getStatus());
			if (changedcustomerid != null && customerRepository.existsById(changedcustomerid)) {
				updateOrder.setCustomer(customerRepository.findById(changedcustomerid).get());
			}

			return new ResponseEntity<>(orderRepository.save(updateOrder), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param orderid
	 * @return null NO_CONTENT
	 */
	@DeleteMapping("/customers/orders/{orderid}")
	@PreAuthorize("hasAnyAuthority('USER_DELETE')")
	public ResponseEntity<Object> deleteOrderById(@PathVariable Integer orderid) {
		try {
			if (!orderRepository.existsById(orderid)) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			orderRepository.deleteById(orderid);

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
	@DeleteMapping("/customers/orders")
	@PreAuthorize("hasAnyAuthority('USER_DELETE')")
	public ResponseEntity<Object> deleteAllOrders() {
		try {
			orderRepository.deleteAll();

			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @return orders list
	 */
	@PostMapping("/orders/report")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntity<Object> getReportOrders(@RequestBody TimeDuration timeduration) {
		try {
			if ("week".equals(timeduration.getDuration())) {
				return new ResponseEntity<>(orderRepository.selectReportOrdersByWeek(timeduration.getStartdate(),
						timeduration.getEnddate()), HttpStatus.OK);
			}

			if ("month".equals(timeduration.getDuration())) {
				return new ResponseEntity<>(orderRepository.selectReportOrdersByMonth(timeduration.getStartdate(),
						timeduration.getEnddate()), HttpStatus.OK);
			}

			return new ResponseEntity<>(
					orderRepository.selectReportOrdersByDate(timeduration.getStartdate(), timeduration.getEnddate()),
					HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * trả về file excel
	 * 
	 * @param response
	 * @throws IOException
	 */
	@GetMapping("/orders/report/exportexcel")
//	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
	public void exportToExcel(HttpServletResponse response, @RequestBody TimeDuration timeduration) throws IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/octet-stream");

		LocalDateTime today = LocalDateTime.now(ZoneId.systemDefault());
		DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
		String currentDateTime = today.format(dateTimeFormat);

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=reportorders_" + currentDateTime + ".xlsx";

		response.setHeader(headerKey, headerValue);

		ExcelExporterOrders excelFile = new ExcelExporterOrders();

		if ("week".equals(timeduration.getDuration())) {
			excelFile = new ExcelExporterOrders(
					orderRepository.selectReportOrdersByWeek(timeduration.getStartdate(), timeduration.getEnddate()));
		}

		if ("month".equals(timeduration.getDuration())) {
			excelFile = new ExcelExporterOrders(
					orderRepository.selectReportOrdersByMonth(timeduration.getStartdate(), timeduration.getEnddate()));
		}

		excelFile = new ExcelExporterOrders(
				orderRepository.selectReportOrdersByDate(timeduration.getStartdate(), timeduration.getEnddate()));

		excelFile.exportExcel(response);
	}
}
