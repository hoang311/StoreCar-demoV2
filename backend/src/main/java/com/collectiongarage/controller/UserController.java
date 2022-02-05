package com.collectiongarage.controller;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.collectiongarage.model.Customer;
import com.collectiongarage.model.Employee;
import com.collectiongarage.model.Role;
import com.collectiongarage.model.Token;
import com.collectiongarage.model.User;
import com.collectiongarage.repository.ICustomerRepository;
import com.collectiongarage.repository.IEmployeeRepository;
import com.collectiongarage.repository.IRoleRepository;
import com.collectiongarage.repository.UserRepository;
import com.collectiongarage.security.JwtUtil;
import com.collectiongarage.security.UserPrincipal;
import com.collectiongarage.service.TokenService;
import com.collectiongarage.service.UserService;

@RestController
@CrossOrigin
public class UserController {
	@Autowired
	private UserService userService;

	@Autowired
	ICustomerRepository customerRepository;

	@Autowired
	IEmployeeRepository employeeRepository;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	IRoleRepository roleRepository;

	/**
	 * @return users list (all or by page)
	 */
	@GetMapping("/users")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntity<Object> getAllsers(@RequestParam(required = false) Integer page) {
		try {
			if (null == page) {
				return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
			}

			return new ResponseEntity(userRepository.findAll(PageRequest.of(page, 10)), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param id
	 * @return user object
	 */
	@GetMapping("/users/{id}")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntity<Object> getUserById(@PathVariable Long id) {
		try {
			if (!userRepository.existsById(id)) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			return new ResponseEntity(userRepository.findById(id), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param token
	 * @return User object
	 */
	@GetMapping("/users/getfromtoken")
	public ResponseEntity<Object> getUserFromToken(@RequestHeader("Authorization") String token) {
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

			return new ResponseEntity<>(userRepository.findById(userPrincipal.getUserId()), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param user
	 * @return new user object
	 */
	@PostMapping("/users")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntity<Object> createUser(@RequestHeader("Authorization") String token,
			@Valid @RequestBody User user) {
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
			newUser.setUsername(user.getUsername().trim());
			newUser.setPassword(new BCryptPasswordEncoder().encode(user.getPassword().trim()));
			Set<Role> userRole = new HashSet<Role>();
			for (Role role : user.getRoles()) {
				userRole.add(roleRepository.findById(role.getId()).get());
			}
			newUser.setRoles(userRole);
			User saveNewUser = userRepository.save(newUser);

			if (null != user.getCustomer()) {
				Customer newCustomer = new Customer();
				newCustomer.setLastName(user.getCustomer().getLastName().trim());
				newCustomer.setFirstName(user.getCustomer().getFirstName().trim());
				newCustomer.setPhoneNumber(user.getCustomer().getPhoneNumber().trim());
				newCustomer.setUser(saveNewUser);
				customerRepository.save(newCustomer);
			}

			if (null != user.getEmployee()) {
				Employee newEmployee = new Employee();
				newEmployee.setLastName(user.getEmployee().getLastName().trim());
				newEmployee.setFirstName(user.getEmployee().getFirstName().trim());
				newEmployee.setExtension(user.getEmployee().getExtension().trim());
				newEmployee.setEmail(user.getEmployee().getEmail().trim());
				newEmployee.setOfficeCode(user.getEmployee().getOfficeCode());
				newEmployee.setReportTo(user.getEmployee().getReportTo());
				newEmployee.setJobTitle(user.getEmployee().getJobTitle());
				newEmployee.setUser(saveNewUser);
				employeeRepository.save(newEmployee);
			}

			return new ResponseEntity<>(userRepository.findById(saveNewUser.getId()), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param user
	 * @return new token object
	 */
	@PostMapping("/users/customer/signup")
	public ResponseEntity<Object> createUserByCustomerSignUp(@Valid @RequestBody User user) {
		try {
			User newUser = new User();
			newUser.setUsername(user.getUsername().trim());
			newUser.setPassword(new BCryptPasswordEncoder().encode(user.getPassword().trim()));
			User saveNewUser = userService.createUser(newUser);

			if (null != user.getCustomer()) {
				Customer newCustomer = new Customer();
				newCustomer.setLastName(user.getCustomer().getLastName().trim());
				newCustomer.setFirstName(user.getCustomer().getFirstName().trim());
				newCustomer.setPhoneNumber(user.getCustomer().getPhoneNumber().trim());
				newCustomer.setUser(saveNewUser);
				customerRepository.save(newCustomer);
			}

			UserPrincipal userPrincipal = userService.findByUsername(saveNewUser.getUsername());
			Token token = new Token();
			token.setToken(jwtUtil.generateToken(userPrincipal));
			token.setTokenExpDate(jwtUtil.generateExpirationDate());
			token.setCreatedBy(userPrincipal.getUserId());

			return new ResponseEntity<>(tokenService.createToken(token), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param user
	 * @return update user object
	 */
	@PutMapping("/users/{id}")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntity<Object> updateUser(@RequestHeader("Authorization") String token, @PathVariable Long id,
			@Valid @RequestBody User user) {
		try {
			if (!userRepository.existsById(id)) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			String tokenName = "Token ";

			if (null == token || !token.startsWith(tokenName)) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}

			String tokenValue = token.substring(tokenName.length());
			UserPrincipal updatedBy = jwtUtil.getUserFromToken(tokenValue);

			if (null == updatedBy) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}

			User updateUser = userRepository.findById(id).get();
			updateUser.setUpdatedAt(new Date());
			updateUser.setUpdatedBy(updatedBy.getUserId());
			updateUser.setUsername(user.getUsername().trim());
			updateUser.setPassword(new BCryptPasswordEncoder().encode(user.getPassword().trim()));
			Set<Role> userRole = new HashSet<Role>();
			for (Role role : user.getRoles()) {
				userRole.add(roleRepository.findById(role.getId()).get());
			}
			updateUser.setRoles(userRole);

			return new ResponseEntity<>(userRepository.save(updateUser), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param user
	 * @return update user object
	 */
	@PutMapping("/users/customer/edit")
	public ResponseEntity<Object> updateUserByCustomer(@RequestHeader("Authorization") String token,
			@Valid @RequestBody User user) {
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

			User updateUser = userRepository.findById(userPrincipal.getUserId()).get();
			updateUser.setUpdatedAt(new Date());
			updateUser.setUpdatedBy(userPrincipal.getUserId());
			User saveNewUser = userRepository.save(updateUser);

			if (null != user.getCustomer()) {
				Customer updateCustomer = saveNewUser.getCustomer();
				updateCustomer.setLastName(user.getCustomer().getLastName().trim());
				updateCustomer.setFirstName(user.getCustomer().getFirstName().trim());
				updateCustomer.setPhoneNumber(user.getCustomer().getPhoneNumber().trim());
				updateCustomer.setAddress(user.getCustomer().getAddress());
				if (user.getCustomer().getAddress() != null) {
					updateCustomer.setAddress(user.getCustomer().getAddress().trim());
				}
				updateCustomer.setCity(user.getCustomer().getCity());
				if (user.getCustomer().getCity() != null) {
					updateCustomer.setCity(user.getCustomer().getCity().trim());
				}
				updateCustomer.setState(user.getCustomer().getState());
				if (user.getCustomer().getState() != null) {
					updateCustomer.setState(user.getCustomer().getState().trim());
				}
				updateCustomer.setPostalCode(user.getCustomer().getPostalCode());
				if (user.getCustomer().getPostalCode() != null) {
					updateCustomer.setPostalCode(user.getCustomer().getPostalCode().trim());
				}
				updateCustomer.setCountry(user.getCustomer().getCountry());
				if (user.getCustomer().getCountry() != null) {
					updateCustomer.setCountry(user.getCustomer().getCountry().trim());
				}
				customerRepository.save(updateCustomer);
			}

			return new ResponseEntity<>(userRepository.findById(userPrincipal.getUserId()), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param user
	 * @return user update password object
	 */
	@PutMapping("/users/customer/changeusername")
	public ResponseEntity<Object> changeUsernameByCustomer(@RequestHeader("Authorization") String token,
			@Valid @RequestBody User user) {
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

			if (!new BCryptPasswordEncoder().matches(user.getPassword().trim(), userPrincipal.getPassword())) {
				return new ResponseEntity<>("Password not correct !!!", HttpStatus.BAD_REQUEST);
			}

			User updateUser = userRepository.findById(userPrincipal.getUserId()).get();
			updateUser.setUpdatedAt(new Date());
			updateUser.setUpdatedBy(userPrincipal.getUserId());
			updateUser.setUsername(user.getUsername().trim());

			return new ResponseEntity<>(userRepository.save(updateUser), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param user
	 * @return user update password object
	 */
	@PutMapping("/users/customer/changepassword")
	public ResponseEntity<Object> changePasswordByCustomer(@RequestHeader("Authorization") String token,
			@Valid @RequestBody User user) {
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

			if (!new BCryptPasswordEncoder().matches(user.getPassword().trim(), userPrincipal.getPassword())) {
				return new ResponseEntity<>("Current password not correct !!!", HttpStatus.BAD_REQUEST);
			}

			if (user.getPassword().trim().equals(user.getNewPassword().trim())) {
				return new ResponseEntity<>("Password not change !!!", HttpStatus.BAD_REQUEST);
			}

			User updateUser = userRepository.findById(userPrincipal.getUserId()).get();
			updateUser.setUpdatedAt(new Date());
			updateUser.setUpdatedBy(userPrincipal.getUserId());
			updateUser.setPassword(new BCryptPasswordEncoder().encode(user.getNewPassword().trim()));

			return new ResponseEntity<>(userRepository.save(updateUser), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param token
	 * @param id
	 * @return null NO_CONTENT
	 */
	@DeleteMapping("/users/{id}")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntity<Object> deleteUser(@RequestHeader("Authorization") String token, @PathVariable Long id) {
		try {
			userRepository.deleteById(id);

			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param user
	 * @return new token object
	 */
	@PostMapping("/users/customer/signin")
	public ResponseEntity<Object> signin(@Valid @RequestBody User user) {
		try {
			UserPrincipal userPrincipal = userService.findByUsername(user.getUsername().trim());
			if (null == userPrincipal
					|| !new BCryptPasswordEncoder().matches(user.getPassword().trim(), userPrincipal.getPassword())) {
				return new ResponseEntity<>("Tài khoản hoặc mật khẩu không chính xác", HttpStatus.BAD_REQUEST);
			}

			Token token = new Token();
			token.setToken(jwtUtil.generateToken(userPrincipal));
			token.setTokenExpDate(jwtUtil.generateExpirationDate());
			token.setCreatedBy(userPrincipal.getUserId());

			return new ResponseEntity<>(tokenService.createToken(token), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param token
	 * @return UserPrincipal object
	 */
	@GetMapping("/users/getuserprincipal")
	public ResponseEntity<Object> getUserPrincipalFromToken(@Valid @RequestBody Token token) {
		try {
			return new ResponseEntity<>(jwtUtil.getUserFromToken(token.getToken()), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param token
	 * @return User object
	 */
	@GetMapping("/users/getuserhttpservletrequest")
	public ResponseEntity<Object> getUserFromTokenByHttpServletRequest(HttpServletRequest request) {
		try {
			String token = request.getHeader("Authorization");
			String tokenName = "Token ";

			if (null == token || !token.startsWith(tokenName)) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}

			String tokenValue = token.substring(tokenName.length());
			UserPrincipal userPrincipal = jwtUtil.getUserFromToken(tokenValue);

			if (null == userPrincipal) {
				return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			}

			return new ResponseEntity<>(userRepository.findById(userPrincipal.getUserId()), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

}
