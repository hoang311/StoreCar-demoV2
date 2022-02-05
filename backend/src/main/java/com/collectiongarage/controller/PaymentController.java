package com.collectiongarage.controller;

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

import com.collectiongarage.model.Payment;
import com.collectiongarage.repository.ICustomerRepository;
import com.collectiongarage.repository.IPaymentRepository;

@RestController
@CrossOrigin
@RequestMapping("/")
public class PaymentController {
	@Autowired
	ICustomerRepository customerRepository;

	@Autowired
	IPaymentRepository paymentRepository;

	/**
	 * @param page
	 * @return payments list (all or by page)
	 */
	@GetMapping("/customers/payments")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntity<Object> getAllPayments(@RequestParam(required = false) Integer page) {
		try {
			if (page == null) {
				return new ResponseEntity<>(paymentRepository.findAll(), HttpStatus.OK);
			}

			return new ResponseEntity<>(paymentRepository.findAll(PageRequest.of(page, 10)).getContent(),
					HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param paymentid
	 * @return payment object
	 */
	@GetMapping("/customers/payments/{paymentid}")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntity<Object> getPaymentById(@PathVariable Integer paymentid) {
		try {
			if (!paymentRepository.existsById(paymentid)) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			return new ResponseEntity<>(paymentRepository.findById(paymentid), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param customerid
	 * @return payments list (by customerid)
	 */
	@GetMapping("/customers/{customerid}/payments")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntity<Object> getAllPaymentsOfCustomerId(@PathVariable Integer customerid) {
		try {
			if (!customerRepository.existsById(customerid)) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			return new ResponseEntity<>(customerRepository.findById(customerid).get().getPayments(), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param customerid
	 * @param cPayment
	 * @return new payment object saving
	 */
	@PostMapping("/customers/{customerid}/payments")
	@PreAuthorize("hasAnyAuthority('USER_CREATE')")
	public ResponseEntity<Object> createPayment(@PathVariable Integer customerid,
			@Valid @RequestBody Payment cPayment) {
		try {
			if (!customerRepository.existsById(customerid)) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			Payment newPayment = new Payment();
			newPayment.setCustomer(customerRepository.findById(customerid).get());
			newPayment.setCheckNumber(cPayment.getCheckNumber());
			newPayment.setPaymentDate(cPayment.getPaymentDate());
			newPayment.setAmmount(cPayment.getAmmount());

			return new ResponseEntity<>(paymentRepository.save(newPayment), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param paymentid
	 * @param cPayment
	 * @param changedcustomerid
	 * @return payment object update saving
	 */
	@PutMapping("/customers/payments/{paymentid}")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntity<Object> updatePayment(@PathVariable Integer paymentid, @Valid @RequestBody Payment cPayment,
			@RequestParam(required = false) Integer changedcustomerid) {
		try {
			if (!paymentRepository.existsById(paymentid)) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			if (changedcustomerid != null && !customerRepository.existsById(changedcustomerid)) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			Payment updatePayment = paymentRepository.findById(paymentid).get();
			if (changedcustomerid != null && customerRepository.existsById(changedcustomerid)) {
				updatePayment.setCustomer(customerRepository.findById(changedcustomerid).get());
			}
			updatePayment.setCheckNumber(cPayment.getCheckNumber());
			updatePayment.setPaymentDate(cPayment.getPaymentDate());
			updatePayment.setAmmount(cPayment.getAmmount());

			return new ResponseEntity<>(paymentRepository.save(updatePayment), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param paymentid
	 * @return null NO_CONTENT
	 */
	@DeleteMapping("/customers/payments/{paymentid}")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntity<Object> deletePaymentById(@PathVariable Integer paymentid) {
		try {
			if (!paymentRepository.existsById(paymentid)) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			paymentRepository.deleteById(paymentid);

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
	@DeleteMapping("/customers/payments")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntity<Object> deleteAllPayments() {
		try {
			paymentRepository.deleteAll();

			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

}
