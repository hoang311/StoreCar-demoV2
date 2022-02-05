package com.collectiongarage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.collectiongarage.repository.IRoleRepository;

@RestController
@CrossOrigin
public class RoleController {
	@Autowired
	IRoleRepository roleRepository;

	/**
	 * @return roles list (all or by page)
	 */
	@GetMapping("/roles")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntity<Object> getAllRoles() {
		try {
			return new ResponseEntity<>(roleRepository.findAll(), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

}
