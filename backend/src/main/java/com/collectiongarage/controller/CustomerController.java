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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

import com.collectiongarage.model.Customer;
import com.collectiongarage.model.User;
import com.collectiongarage.repository.ICustomerRepository;
import com.collectiongarage.repository.UserRepository;
import com.collectiongarage.security.JwtUtil;
import com.collectiongarage.security.UserPrincipal;
import com.collectiongarage.service.ExcelExporterCustomers;

@RestController
@CrossOrigin
@RequestMapping("/")
public class CustomerController {
	@Autowired
	ICustomerRepository customerRepository;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	UserRepository userRepository;

	/**
	 * @param page
	 * @return customers list (all or by page)
	 */
	@GetMapping("/customers")
	@PreAuthorize("hasAnyAuthority('USER_READ')")
	public ResponseEntity<Object> getAllCustomers(@RequestParam(required = false) Integer page) {
		try {
			if (page == null) {
				return new ResponseEntity<>(customerRepository.findAll(), HttpStatus.OK);
			}

			return new ResponseEntity<>(customerRepository.findAll(PageRequest.of(page, 10)).getContent(),
					HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param id
	 * @return customer object
	 */
	@GetMapping("/customers/{id}")
	@PreAuthorize("hasAnyAuthority('USER_READ')")
	public ResponseEntity<Object> getCustomerById(@PathVariable Integer id) {
		try {
			if (!customerRepository.existsById(id)) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			return new ResponseEntity<>(customerRepository.findById(id), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param phonenumber
	 * @return customer object
	 */
	@GetMapping("/customers/phonenumber/{phonenumber}")
	@PreAuthorize("hasAnyAuthority('USER_READ')")
	public ResponseEntity<Object> getCustomerByPhoneNumber(@PathVariable String phonenumber) {
		try {

			return new ResponseEntity<>(customerRepository.findByPhoneNumber(phonenumber), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param cCustomer
	 * @return new customer object saving
	 */
	@PostMapping("/customers")
	@PreAuthorize("hasAnyAuthority('USER_CREATE')")
	public ResponseEntity<Object> createCustomer(@RequestHeader("Authorization") String token,
			@Valid @RequestBody Customer cCustomer) {
		try {
			String tokenName = "Token ";

			if (null == token || !token.startsWith(tokenName)) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}

			String tokenValue = token.substring(tokenName.length());
			UserPrincipal createdBy = jwtUtil.getUserFromToken(tokenValue);

			if (null == createdBy) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}

			User newUser = new User();
			newUser.setCreatedAt(new Date());
			newUser.setCreatedBy(createdBy.getUserId());
			newUser.setUsername(cCustomer.getPhoneNumber().trim());
			newUser.setPassword(new BCryptPasswordEncoder().encode(cCustomer.getPhoneNumber().trim()));
			User saveNewUser = userRepository.save(newUser);

			Customer newCustomer = new Customer();
			newCustomer.setLastName(cCustomer.getLastName().trim());
			newCustomer.setFirstName(cCustomer.getFirstName().trim());
			newCustomer.setPhoneNumber(cCustomer.getPhoneNumber().trim());
			newCustomer.setAddress(cCustomer.getAddress());
			if (cCustomer.getAddress() != null) {
				newCustomer.setAddress(cCustomer.getAddress().trim());
			}
			newCustomer.setCity(cCustomer.getCity());
			if (cCustomer.getCity() != null) {
				newCustomer.setCity(cCustomer.getCity().trim());
			}
			newCustomer.setState(cCustomer.getState());
			if (cCustomer.getState() != null) {
				newCustomer.setState(cCustomer.getState().trim());
			}
			newCustomer.setPostalCode(cCustomer.getPostalCode());
			if (cCustomer.getPostalCode() != null) {
				newCustomer.setPostalCode(cCustomer.getPostalCode().trim());
			}
			newCustomer.setCountry(cCustomer.getCountry());
			if (cCustomer.getCountry() != null) {
				newCustomer.setCountry(cCustomer.getCountry().trim());
			}
			newCustomer.setSalesRepEmployeeNumber(cCustomer.getSalesRepEmployeeNumber());
			newCustomer.setCreditLimit(cCustomer.getCreditLimit());
			newCustomer.setUser(saveNewUser);

			return new ResponseEntity<>(customerRepository.save(newCustomer), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param id
	 * @param cCustomer
	 * @return customer object update saving
	 */
	@PutMapping("/customers/{id}")
	@PreAuthorize("hasAnyAuthority('USER_UPDATE')")
	public ResponseEntity<Object> updateCustomer(@PathVariable Integer id, @Valid @RequestBody Customer cCustomer) {
		try {
			if (!customerRepository.existsById(id)) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			Customer updateCustomer = customerRepository.findById(id).get();
			updateCustomer.setLastName(cCustomer.getLastName().trim());
			updateCustomer.setFirstName(cCustomer.getFirstName().trim());
			updateCustomer.setPhoneNumber(cCustomer.getPhoneNumber().trim());
			updateCustomer.setAddress(cCustomer.getAddress());
			if (cCustomer.getAddress() != null) {
				updateCustomer.setAddress(cCustomer.getAddress().trim());
			}
			updateCustomer.setCity(cCustomer.getCity());
			if (cCustomer.getCity() != null) {
				updateCustomer.setCity(cCustomer.getCity().trim());
			}
			updateCustomer.setState(cCustomer.getState());
			if (cCustomer.getState() != null) {
				updateCustomer.setState(cCustomer.getState().trim());
			}
			updateCustomer.setPostalCode(cCustomer.getPostalCode());
			if (cCustomer.getPostalCode() != null) {
				updateCustomer.setPostalCode(cCustomer.getPostalCode().trim());
			}
			updateCustomer.setCountry(cCustomer.getCountry());
			if (cCustomer.getCountry() != null) {
				updateCustomer.setCountry(cCustomer.getCountry().trim());
			}
			updateCustomer.setSalesRepEmployeeNumber(cCustomer.getSalesRepEmployeeNumber());
			updateCustomer.setCreditLimit(cCustomer.getCreditLimit());

			return new ResponseEntity<>(customerRepository.save(updateCustomer), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param id
	 * @return null NO_CONTENT
	 */
	@DeleteMapping("/customers/{id}")
	@PreAuthorize("hasAnyAuthority('USER_DELETE')")
	public ResponseEntity<Object> deleteCustomerById(@PathVariable Integer id) {
		try {
			if (!customerRepository.existsById(id)) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			customerRepository.deleteById(id);

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
	@DeleteMapping("/customers")
	@PreAuthorize("hasAnyAuthority('USER_DELETE')")
	public ResponseEntity<Object> deleteAllCustomers() {
		try {
			customerRepository.deleteAll();

			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @return customers list
	 */
	@GetMapping("/customers/report")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntity<Object> getReportCustomers() {
		try {
			return new ResponseEntity<>(customerRepository.selectReportCustomers(), HttpStatus.OK);
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
	 * @param totalorder
	 * @param customertype
	 * @throws IOException
	 */
	@GetMapping("/customers/report/exportexcel")
//	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
	public void exportToExcel(HttpServletResponse response, @RequestParam(required = false) Integer totalorder,
			@RequestParam(required = false) String customertype) throws IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/octet-stream");

		LocalDateTime today = LocalDateTime.now(ZoneId.systemDefault());
		DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
		String currentDateTime = today.format(dateTimeFormat);

		String headerKey = "Content-Disposition";
		String headerValue = "";

		headerValue = "attachment; filename=reportcustomers_" + currentDateTime + ".xlsx";

		response.setHeader(headerKey, headerValue);

		ExcelExporterCustomers excelFile = new ExcelExporterCustomers(customerRepository.selectReportCustomers());

		if ("platinum".equals(customertype)) {
			headerValue = "attachment; filename=reportcustomers_platinum_" + currentDateTime + ".xlsx";

			response.setHeader(headerKey, headerValue);

			excelFile = new ExcelExporterCustomers(customerRepository.selectReportCustomersPlatinum());
		}

		if ("gold".equals(customertype)) {
			headerValue = "attachment; filename=reportcustomers_gold_" + currentDateTime + ".xlsx";

			response.setHeader(headerKey, headerValue);

			excelFile = new ExcelExporterCustomers(
					customerRepository.selectReportCustomersWithCustomerType(200000, 500000));
		}

		if ("silver".equals(customertype)) {
			headerValue = "attachment; filename=reportcustomers_silver_" + currentDateTime + ".xlsx";

			response.setHeader(headerKey, headerValue);

			excelFile = new ExcelExporterCustomers(
					customerRepository.selectReportCustomersWithCustomerType(100000, 200000));
		}

		if ("vip".equals(customertype)) {
			headerValue = "attachment; filename=reportcustomers_vip_" + currentDateTime + ".xlsx";

			response.setHeader(headerKey, headerValue);

			excelFile = new ExcelExporterCustomers(
					customerRepository.selectReportCustomersWithCustomerType(50000, 100000));
		}

		if (totalorder != null) {
			headerValue = "attachment; filename=reportcustomers_ordergreater_" + totalorder + "_" + currentDateTime
					+ ".xlsx";

			response.setHeader(headerKey, headerValue);

			excelFile = new ExcelExporterCustomers(customerRepository.selectReportCustomersWithOrder(totalorder));

			if ("platinum".equals(customertype)) {
				headerValue = "attachment; filename=reportcustomers_platinum_ordergreater_" + totalorder + "_"
						+ currentDateTime + ".xlsx";

				response.setHeader(headerKey, headerValue);

				excelFile = new ExcelExporterCustomers(
						customerRepository.selectReportCustomersPlatinumWithOrder(totalorder));
			}

			if ("gold".equals(customertype)) {
				headerValue = "attachment; filename=reportcustomers_gold_ordergreater_" + totalorder + "_"
						+ currentDateTime + ".xlsx";

				response.setHeader(headerKey, headerValue);

				excelFile = new ExcelExporterCustomers(
						customerRepository.selectReportCustomersWithCustomerTypeNOrder(200000, 500000, totalorder));
			}

			if ("silver".equals(customertype)) {
				headerValue = "attachment; filename=reportcustomers_silver_ordergreater_" + totalorder + "_"
						+ currentDateTime + ".xlsx";

				response.setHeader(headerKey, headerValue);

				excelFile = new ExcelExporterCustomers(
						customerRepository.selectReportCustomersWithCustomerTypeNOrder(100000, 200000, totalorder));
			}

			if ("vip".equals(customertype)) {
				headerValue = "attachment; filename=reportcustomers_vip_ordergreater_" + totalorder + "_"
						+ currentDateTime + ".xlsx";

				response.setHeader(headerKey, headerValue);

				excelFile = new ExcelExporterCustomers(
						customerRepository.selectReportCustomersWithCustomerTypeNOrder(50000, 100000, totalorder));
			}
		}

		excelFile.exportExcel(response);
	}
}
