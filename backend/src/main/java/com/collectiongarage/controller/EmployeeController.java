package com.collectiongarage.controller;

import java.util.Date;

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

import com.collectiongarage.model.Employee;
import com.collectiongarage.model.User;
import com.collectiongarage.repository.IEmployeeRepository;
import com.collectiongarage.repository.UserRepository;
import com.collectiongarage.security.JwtUtil;
import com.collectiongarage.security.UserPrincipal;

@RestController
@CrossOrigin
@RequestMapping("/")
public class EmployeeController {
	@Autowired
	IEmployeeRepository employeeRepository;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	UserRepository userRepository;

	/**
	 * @param page
	 * @return employees list (all or by page)
	 */
	@GetMapping("/employees")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntity<Object> getAllEmployees(@RequestParam(required = false) Integer page) {
		try {
			if (page == null) {
				return new ResponseEntity<>(employeeRepository.findAll(), HttpStatus.OK);
			}

			return new ResponseEntity<>(employeeRepository.findAll(PageRequest.of(page, 10)).getContent(),
					HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param id
	 * @return employee object
	 */
	@GetMapping("/employees/{id}")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntity<Object> getEmployeeById(@PathVariable Integer id) {
		try {
			if (!employeeRepository.existsById(id)) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			return new ResponseEntity<>(employeeRepository.findById(id), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param cEmployee
	 * @return new employee object saving
	 */
	@PostMapping("/employees")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntity<Object> createEmployee(@RequestHeader("Authorization") String token,
			@Valid @RequestBody Employee cEmployee) {
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
			newUser.setUsername(cEmployee.getEmail().trim());
			newUser.setPassword(new BCryptPasswordEncoder().encode(cEmployee.getEmail().trim()));
			User saveNewUser = userRepository.save(newUser);

			Employee newEmployee = new Employee();
			newEmployee.setLastName(cEmployee.getLastName().trim());
			newEmployee.setFirstName(cEmployee.getFirstName().trim());
			newEmployee.setExtension(cEmployee.getExtension().trim());
			newEmployee.setEmail(cEmployee.getEmail().trim());
			newEmployee.setOfficeCode(cEmployee.getOfficeCode());
			newEmployee.setReportTo(cEmployee.getReportTo());
			newEmployee.setJobTitle(cEmployee.getJobTitle());
			newEmployee.setUser(saveNewUser);

			return new ResponseEntity<>(employeeRepository.save(newEmployee), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param id
	 * @param cEmployee
	 * @return employee object update saving
	 */
	@PutMapping("/employees/{id}")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntity<Object> updateEmployee(@PathVariable Integer id, @Valid @RequestBody Employee cEmployee) {
		try {
			if (!employeeRepository.existsById(id)) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			Employee updateEmployee = employeeRepository.findById(id).get();
			updateEmployee.setLastName(cEmployee.getLastName().trim());
			updateEmployee.setFirstName(cEmployee.getFirstName().trim());
			updateEmployee.setExtension(cEmployee.getExtension().trim());
			updateEmployee.setEmail(cEmployee.getEmail().trim());
			updateEmployee.setOfficeCode(cEmployee.getOfficeCode());
			updateEmployee.setReportTo(cEmployee.getReportTo());
			updateEmployee.setJobTitle(cEmployee.getJobTitle());

			return new ResponseEntity<>(employeeRepository.save(updateEmployee), HttpStatus.OK);
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
	@DeleteMapping("/employees/{id}")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntity<Object> deleteEmployeeById(@PathVariable Integer id) {
		try {
			if (!employeeRepository.existsById(id)) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			employeeRepository.deleteById(id);

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
	@DeleteMapping("/employees")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntity<Object> deleteAllEmployees() {
		try {
			employeeRepository.deleteAll();

			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

}
