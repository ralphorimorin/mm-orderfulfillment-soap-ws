package com.mm.of.ws;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import io.mm.meetup.productcategory_ws.GetProductCategoriesRequest;
import io.mm.meetup.productcategory_ws.GetProductCategoriesResponse;
import io.mm.meetup.productcategory_ws.ProductCategory;

@Endpoint
public class ProductCategoryEndpoint {
	private static final String NAMESPACE_URI = "http://mm.io/meetup/productcategory-ws";

	private ProductCategoryRepository productCategoryRepository;

	@Autowired
	public ProductCategoryEndpoint(ProductCategoryRepository productCategoryRepository) {
		this.productCategoryRepository = productCategoryRepository;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "getProductCategoriesRequest")
	@ResponsePayload
	public GetProductCategoriesResponse getProductCategories(@RequestPayload GetProductCategoriesRequest request) {
		GetProductCategoriesResponse response = new GetProductCategoriesResponse();
		List<ProductCategory> productCategories = new CopyOnWriteArrayList<ProductCategory>();
		if(request.getSearchBy()==null || request.getSearchBy().isEmpty()) {
			productCategories.addAll(productCategoryRepository.getAllProductCategories());
		}
		if(request.getSearchBy()!=null && !request.getSearchBy().isEmpty()) {
			if(request.getSearchBy().contains(",")) {
				String[] splittedSearchStr = request.getSearchBy().split("\\,");
				if(splittedSearchStr.length>1) {
					List<String> toList = new ArrayList<>(Arrays.asList(splittedSearchStr));
					if(toList.contains("id") && toList.contains("name") 
							&& request.getId()!=null && !request.getId().isEmpty()
							&& request.getName()!=null && !request.getName().isEmpty()) {
						productCategories.addAll(productCategoryRepository.findProductCategoryByIdAndName(request.getId(),request.getName()));
					}
				}
				else {
					if(splittedSearchStr[0].equals("id") && request.getId()!=null && !request.getId().isEmpty()) {
						productCategories.addAll(productCategoryRepository.findProductCategoryById(request.getId()));
					} else if (splittedSearchStr[0].equals("name") && request.getName()!=null && !request.getName().isEmpty()) {
						productCategories.addAll(productCategoryRepository.findProductCategoryByName(request.getName()));
					}
				}
					
			} else {
				String searchStr = request.getSearchBy();
				if(searchStr.equals("id") && request.getId()!=null && !request.getId().isEmpty()) {
					productCategories.addAll(productCategoryRepository.findProductCategoryById(request.getId()));
				} else if (searchStr.equals("name") && request.getName()!=null && !request.getName().isEmpty()) {
					productCategories.addAll(productCategoryRepository.findProductCategoryByName(request.getName()));
				}
			}
		}
		productCategories.forEach(prod->response.getProductCategories().add(prod));
		return response;
	}
}
