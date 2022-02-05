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

import com.collectiongarage.model.Office;
import com.collectiongarage.repository.IOfficeRepository;

@RestController
@CrossOrigin
@RequestMapping("/")
public class OfficeController {
	@Autowired
	IOfficeRepository officeRepository;

	/**
	 * @param page
	 * @return offices list (all or by page)
	 */
	@GetMapping("/offices")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntity<Object> getAllOffices(@RequestParam(required = false) Integer page) {
		try {
			if (page == null) {
				return new ResponseEntity<>(officeRepository.findAll(), HttpStatus.OK);
			}

			return new ResponseEntity<>(officeRepository.findAll(PageRequest.of(page, 10)).getContent(), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param id
	 * @return office object
	 */
	@GetMapping("/offices/{id}")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
	public ResponseEntity<Object> getOfficeById(@PathVariable Integer id) {
		try {
			if (!officeRepository.existsById(id)) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			return new ResponseEntity<>(officeRepository.findById(id), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param cOffice
	 * @return new office object saving
	 */
	@PostMapping("/offices")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
	public ResponseEntity<Object> createOffice(@Valid @RequestBody Office cOffice) {
		try {
			Office newOffice = new Office();
			newOffice.setCity(cOffice.getCity().trim());
			newOffice.setPhone(cOffice.getPhone().trim());
			newOffice.setAddressLine(cOffice.getAddressLine().trim());
			newOffice.setState(cOffice.getState());
			if (cOffice.getState() != null) {
				newOffice.setState(cOffice.getState().trim());
			}
			newOffice.setCountry(cOffice.getCountry().trim());
			newOffice.setTerritory(cOffice.getTerritory());
			if (cOffice.getTerritory() != null) {
				newOffice.setTerritory(cOffice.getTerritory().trim());
			}

			return new ResponseEntity<>(officeRepository.save(newOffice), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param id
	 * @param cOffice
	 * @return office object update saving
	 */
	@PutMapping("/offices/{id}")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
	public ResponseEntity<Object> updateOffice(@PathVariable Integer id, @Valid @RequestBody Office cOffice) {
		try {
			if (!officeRepository.existsById(id)) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			Office updateOffice = officeRepository.findById(id).get();
			updateOffice.setCity(cOffice.getCity().trim());
			updateOffice.setPhone(cOffice.getPhone().trim());
			updateOffice.setAddressLine(cOffice.getAddressLine().trim());
			updateOffice.setState(cOffice.getState());
			if (cOffice.getState() != null) {
				updateOffice.setState(cOffice.getState().trim());
			}
			updateOffice.setCountry(cOffice.getCountry().trim());
			updateOffice.setTerritory(cOffice.getTerritory());
			if (cOffice.getTerritory() != null) {
				updateOffice.setTerritory(cOffice.getTerritory().trim());
			}

			return new ResponseEntity<>(officeRepository.save(updateOffice), HttpStatus.OK);
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
	@DeleteMapping("/offices/{id}")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
	public ResponseEntity<Object> deleteOfficeById(@PathVariable Integer id) {
		try {
			if (!officeRepository.existsById(id)) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			officeRepository.deleteById(id);

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
	@DeleteMapping("/offices")
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
	public ResponseEntity<Object> deleteAllOffices() {
		try {
			officeRepository.deleteAll();

			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

}
