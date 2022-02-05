package com.collectiongarage.controller;

import java.util.Collection;

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

import com.collectiongarage.model.Product;
import com.collectiongarage.repository.IProductLineRepository;
import com.collectiongarage.repository.IProductRepository;

@RestController
@CrossOrigin
@RequestMapping("/")
public class ProductController {
	@Autowired
	IProductLineRepository productLineRepository;

	@Autowired
	IProductRepository productRepository;

	/**
	 * @param page
	 * @return products list (all or by page)
	 */
	@GetMapping("/productlines/products")
	public ResponseEntity<Object> getAllProducts(@RequestParam(required = false) Integer page) {
		try {
			if (page == null) {
				return new ResponseEntity<>(productRepository.findAll(), HttpStatus.OK);
			}

			return new ResponseEntity<>(productRepository.findAll(PageRequest.of(page, 9)), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param productlineid
	 * @param page
	 * @return products list of productlineid
	 */
	@GetMapping("/productlines/{productlineid}/products")
	public ResponseEntity<Object> getAllProductsOfProductline(@PathVariable Integer productlineid,
			@RequestParam(required = false) Integer page) {
		try {
			if (page == null) {
				return new ResponseEntity<>(productLineRepository.findById(productlineid).get().getProducts(),
						HttpStatus.OK);
			}

			return new ResponseEntity<>(productRepository.findByProductLineId(productlineid, PageRequest.of(page, 9)),
					HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param productid
	 * @return product object
	 */
	@GetMapping("/productlines/products/{productid}")
	public ResponseEntity<Object> getProductById(@PathVariable Integer productid) {
		try {
			if (!productRepository.existsById(productid)) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			return new ResponseEntity<>(productRepository.findById(productid), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @return popular products list
	 */
	@GetMapping("/products/popular")
	public ResponseEntity<Object> getPopularProducts() {
		try {
			return new ResponseEntity<>(productRepository.selectPopularProducts(), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param productlineids
	 * @param page
	 * @return products list of many productline
	 */
	@GetMapping("/products/productlines")
	public ResponseEntity<Object> getAllProductsOfManyProductlines(@RequestParam Collection<Integer> productlineids,
			@RequestParam(required = false) Integer page) {
		try {
			if (page == null) {
				return new ResponseEntity<>(productRepository.selectAllProductsOfProductLines(productlineids),
						HttpStatus.OK);
			}

			return new ResponseEntity<>(productRepository.selectAllProductsOfProductLinesPagination(productlineids,
					PageRequest.of(page, 9)), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param productlineids
	 * @param page
	 * @return products list of many productline
	 */
	@GetMapping("/products/productsearch")
	public ResponseEntity<Object> getProductsNameSearch(@RequestParam String productnamesearch,
			@RequestParam(required = false) Integer page) {
		try {

			return new ResponseEntity<>(
					productRepository.selectProductNameLikePagination(productnamesearch, PageRequest.of(page, 9)),
					HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param productlineid
	 * @return 3 related products of productlineid
	 */
	@GetMapping("/products/related")
	public ResponseEntity<Object> getRelatedProductsHighestQuantityOrderSameProductLineId(
			@RequestParam Integer productlineid) {
		try {

			return new ResponseEntity<>(
					productRepository.selectProductsHighestQuantityOrderSameProductLineId(productlineid),
					HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param productlineid
	 * @param cProduct
	 * @return new product object saving
	 */
	@PostMapping(value = { "/productlines/products", "/productlines/{productlineid}/products" })
	@PreAuthorize("hasAnyAuthority('USER_CREATE')")
	public ResponseEntity<Object> createProduct(@PathVariable(required = false) Integer productlineid,
			@Valid @RequestBody Product cProduct) {
		try {
			if (productlineid != null && !productLineRepository.existsById(productlineid)) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			Product newProduct = new Product();
			newProduct.setProductCode(cProduct.getProductCode().trim());
			newProduct.setProductName(cProduct.getProductName().trim());
			newProduct.setProductDescription(cProduct.getProductDescription());
			if (cProduct.getProductDescription() == null) {
				newProduct.setProductDescription(cProduct.getProductDescription().trim());
			}
			if (productlineid != null && productLineRepository.existsById(productlineid)) {
				newProduct.setProductLine(productLineRepository.findById(productlineid).get());
			}
			newProduct.setProductScale(cProduct.getProductScale().trim());
			newProduct.setProductVendor(cProduct.getProductVendor().trim());
			newProduct.setQuantityInStock(cProduct.getQuantityInStock());
			newProduct.setBuyPrice(cProduct.getBuyPrice());

			return new ResponseEntity<>(productRepository.save(newProduct), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param productid
	 * @param cProduct
	 * @param changedproductlineid
	 * @return product object update saving
	 */
	@PutMapping("/productlines/products/{productid}")
	@PreAuthorize("hasAnyAuthority('USER_UPDATE')")
	public ResponseEntity<Object> updateProduct(@PathVariable Integer productid, @Valid @RequestBody Product cProduct,
			@RequestParam(required = false) Integer changedproductlineid) {
		try {
			if (!productRepository.existsById(productid)) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			if (changedproductlineid != null && !productLineRepository.existsById(changedproductlineid)) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			Product updateProduct = productRepository.findById(productid).get();
			updateProduct.setProductCode(cProduct.getProductCode().trim());
			updateProduct.setProductName(cProduct.getProductName().trim());
			updateProduct.setProductDescription(cProduct.getProductDescription());
			if (cProduct.getProductDescription() == null) {
				updateProduct.setProductDescription(cProduct.getProductDescription().trim());
			}
			if (changedproductlineid != null && productLineRepository.existsById(changedproductlineid)) {
				updateProduct.setProductLine(productLineRepository.findById(changedproductlineid).get());
			}
			updateProduct.setProductScale(cProduct.getProductScale().trim());
			updateProduct.setProductVendor(cProduct.getProductVendor().trim());
			updateProduct.setQuantityInStock(cProduct.getQuantityInStock());
			updateProduct.setBuyPrice(cProduct.getBuyPrice());

			return new ResponseEntity<>(productRepository.save(updateProduct), HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

	/**
	 * @param productid
	 * @return null NO_CONTENT
	 */
	@DeleteMapping("/productlines/products/{productid}")
	@PreAuthorize("hasAnyAuthority('USER_DELETE')")
	public ResponseEntity<Object> deleteProductById(@PathVariable Integer productid) {
		try {
			if (!productRepository.existsById(productid)) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}

			productRepository.deleteById(productid);

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
	@DeleteMapping("/productlines/products")
	@PreAuthorize("hasAnyAuthority('USER_DELETE')")
	public ResponseEntity<Object> deleteAllProducts() {
		try {
			productRepository.deleteAll();

			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception details: " + e.getCause().getCause().getMessage());
			return ResponseEntity.unprocessableEntity().body("Failed: " + e.getCause().getCause().getMessage());
		}
	}

}
