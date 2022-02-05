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

import com.collectiongarage.model.ProductLine;
import com.collectiongarage.repository.IProductLineRepository;

@RestController
@CrossOrigin
@RequestMapping("/")
public class ProductLineController {
	@Autowired
	IProductLineRepository productLineRepository;

	/**
	 * @param page
	 * @return productlines list (all or by page)
	 */
	@GetMapping("/productlines")
	@PreAuthorize("hasAnyAuthority('USER_READ')")
	public ResponseEntity<Object> getAllProductLines(@RequestParam(required = false) Integer page) {
		try {
			if (page == null) {
				return new ResponseEntity<>(productLineRepository.findAll(), HttpStatus.OK);
			}

			return new ResponseEntity<>(productLineRepository.findAll(PageRequest.of(page, 10)), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param id
	 * @return productline object
	 */
	@GetMapping("/productlines/{id}")
	@PreAuthorize("hasAnyAuthority('USER_READ')")
	public ResponseEntity<Object> getProductLineById(@PathVariable Integer id) {
		try {
			if (!productLineRepository.existsById(id)) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			return new ResponseEntity<>(productLineRepository.findById(id), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param cProductLine
	 * @return new productline object saving
	 */
	@PostMapping("/productlines")
	@PreAuthorize("hasAnyAuthority('USER_CREATE')")
	public ResponseEntity<Object> createProductLine(@Valid @RequestBody ProductLine cProductLine) {
		try {
			ProductLine newProductLine = new ProductLine();
			newProductLine.setProductLine(cProductLine.getProductLine().trim());
			newProductLine.setDescription(cProductLine.getDescription().trim());

			return new ResponseEntity<>(productLineRepository.save(newProductLine), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param id
	 * @param cProductLine
	 * @return productline object update saving
	 */
	@PutMapping("/productlines/{id}")
	@PreAuthorize("hasAnyAuthority('USER_UPDATE')")
	public ResponseEntity<Object> updateProductLine(@PathVariable Integer id,
			@Valid @RequestBody ProductLine cProductLine) {
		try {
			if (!productLineRepository.existsById(id)) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			ProductLine updateProductLine = productLineRepository.findById(id).get();
			updateProductLine.setProductLine(cProductLine.getProductLine().trim());
			updateProductLine.setDescription(cProductLine.getDescription().trim());

			return new ResponseEntity<>(productLineRepository.save(updateProductLine), HttpStatus.OK);
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
	@DeleteMapping("/productlines/{id}")
	@PreAuthorize("hasAnyAuthority('USER_DELETE')")
	public ResponseEntity<Object> deleteProductLineById(@PathVariable Integer id) {
		try {
			if (!productLineRepository.existsById(id)) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			productLineRepository.deleteById(id);

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
	@DeleteMapping("/productlines")
	@PreAuthorize("hasAnyAuthority('USER_DELETE')")
	public ResponseEntity<Object> deleteAllProductLines() {
		try {
			productLineRepository.deleteAll();

			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

}
