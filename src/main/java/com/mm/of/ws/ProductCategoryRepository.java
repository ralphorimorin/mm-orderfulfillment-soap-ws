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

import io.mm.meetup.productcategory_ws.ProductCategory;

@Component
public class ProductCategoryRepository {
	private static final List<ProductCategory> productCategories = new LinkedList<>();

	@PostConstruct
	public void initData() throws FileNotFoundException, IOException {
		Resource resource = new ClassPathResource("productcategories.csv");
		try (Reader reader = new InputStreamReader(resource.getInputStream())) {
			//Skip Headers
			CsvParser.stream(reader).skip(1).forEach(line -> {
				ProductCategory productCategory = new ProductCategory();
				productCategory.setCategoryId(line[PRODUCTCATEGORIESCSVCOLS.categoryId.ordinal()]);
				productCategory.setCategoryName(line[PRODUCTCATEGORIESCSVCOLS.categoryName.ordinal()]);
				productCategory.setDescription(line[PRODUCTCATEGORIESCSVCOLS.description.ordinal()]);
				productCategory.setPicture(line[PRODUCTCATEGORIESCSVCOLS.picture.ordinal()]);
				
				productCategories.add(productCategory);
			});
		}

	}

	public List<ProductCategory> findProductCategoryByName(String name) {
		Assert.notNull(name, "The product category name must not be null");
		return productCategories.stream().filter(res -> name.equals(res.getCategoryName())).collect(Collectors.toList());
	}
	
	public List<ProductCategory> findProductCategoryById(String id) {
		Assert.notNull(id, "The product category id must not be null");
		return productCategories.stream().filter(res -> id.equals(res.getCategoryId())).collect(Collectors.toList());
	}
	
	public List<ProductCategory> findProductCategoryByIdAndName(String id,String name) {
		Assert.notNull(id, "The product category id must not be null");
		return productCategories.stream().filter(res -> id.equals(res.getCategoryId()) && name.equals(res.getCategoryName())).collect(Collectors.toList());
	}
	
	public List<ProductCategory> getAllProductCategories() {
		return productCategories;
	}
}

enum PRODUCTCATEGORIESCSVCOLS {
	categoryId, categoryName, description, picture
}