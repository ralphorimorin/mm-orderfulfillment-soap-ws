package com.mm.of.ws;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.simpleflatmapper.csv.CsvParser;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import io.mm.meetup.product_ws.Product;

@Component
public class ProductRepository {
	private static final List<Product> products = new LinkedList<>();

	@PostConstruct
	public void initData() throws FileNotFoundException, IOException {
		Resource resource = new ClassPathResource("products.csv");
		try (Reader reader = new InputStreamReader(resource.getInputStream())) {
			//Skip Headers
			CsvParser.stream(reader).skip(1).forEach(line -> {
				Product product = new Product();
				product.setProductId(line[PRODUCTCSVCOLS.productId.ordinal()]);
				product.setSku(line[PRODUCTCSVCOLS.sku.ordinal()]);
				product.setProductName(line[PRODUCTCSVCOLS.productName.ordinal()]);
				product.setDescription(line[PRODUCTCSVCOLS.description.ordinal()]);
				product.setVendorId(line[PRODUCTCSVCOLS.vendorId.ordinal()]);
				product.setSalePrice(Double.parseDouble(line[PRODUCTCSVCOLS.salePrice.ordinal()]));
				product.setQtyInStock(Integer.parseInt(line[PRODUCTCSVCOLS.qtyInStock.ordinal()]));
				product.setQtyInOrder(Integer.parseInt(line[PRODUCTCSVCOLS.qtyInOrder.ordinal()]));
				product.setActive(Boolean.parseBoolean(line[PRODUCTCSVCOLS.active.ordinal()]));
				
				products.add(product);
			});
		}

	}

	public List<Product> findProductByName(String name) {
		Assert.notNull(name, "The product name must not be null");
		return products.stream().filter(res -> name.equals(res.getProductName())).collect(Collectors.toList());
	}
	
	public List<Product> findProductById(String id) {
		Assert.notNull(id, "The product id must not be null");
		return products.stream().filter(res -> id.equals(res.getProductId())).collect(Collectors.toList());
	}
	
	public List<Product> findProductByIdAndName(String id,String name) {
		Assert.notNull(id, "The product id must not be null");
		return products.stream().filter(res -> id.equals(res.getProductId()) && name.equals(res.getProductName())).collect(Collectors.toList());
	}
	
	public List<Product> getAllProducts() {
		return products;
	}
}

enum PRODUCTCSVCOLS {
	productId, sku, productName, description, vendorId, listPrice, salePrice, category, categoryTree, qtyInStock,
	qtyInOrder, active, averageProductRating, productUrl, productImageUrls, brand, totalNumberReviews, reviews
}