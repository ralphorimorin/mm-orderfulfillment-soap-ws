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

import io.mm.meetup.product_ws.GetProductListRequest;
import io.mm.meetup.product_ws.GetProductListResponse;
import io.mm.meetup.product_ws.Product;

@Endpoint
public class ProductEndpoint {
	private static final String NAMESPACE_URI = "http://mm.io/meetup/product-ws";

	private ProductRepository productRepository;

	@Autowired
	public ProductEndpoint(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "getProductListRequest")
	@ResponsePayload
	public GetProductListResponse getProductList(@RequestPayload GetProductListRequest request) {
		GetProductListResponse response = new GetProductListResponse();
		List<Product> productList = new CopyOnWriteArrayList<Product>();
		if(request.getSearchBy()==null || request.getSearchBy().isEmpty()) {
			productList.addAll(productRepository.getAllProducts());
		}
		if(request.getSearchBy()!=null && !request.getSearchBy().isEmpty()) {
			if(request.getSearchBy().contains(",")) {
				String[] splittedSearchStr = request.getSearchBy().split("\\,");
				if(splittedSearchStr.length>1) {
					List<String> toList = new ArrayList<>(Arrays.asList(splittedSearchStr));
					if(toList.contains("id") && toList.contains("name") 
							&& request.getId()!=null && !request.getId().isEmpty()
							&& request.getName()!=null && !request.getName().isEmpty()) {
						productList.addAll(productRepository.findProductByIdAndName(request.getId(),request.getName()));
					}
				}
				else {
					if(splittedSearchStr[0].equals("id") && request.getId()!=null && !request.getId().isEmpty()) {
						productList.addAll(productRepository.findProductById(request.getId()));
					} else if (splittedSearchStr[0].equals("name") && request.getName()!=null && !request.getName().isEmpty()) {
						productList.addAll(productRepository.findProductByName(request.getName()));
					}
				}
					
			} else {
				String searchStr = request.getSearchBy();
				if(searchStr.equals("id") && request.getId()!=null && !request.getId().isEmpty()) {
					productList.addAll(productRepository.findProductById(request.getId()));
				} else if (searchStr.equals("name") && request.getName()!=null && !request.getName().isEmpty()) {
					productList.addAll(productRepository.findProductByName(request.getName()));
				}
			}
		}
		productList.forEach(prod->response.getProduct().add(prod));
		return response;
	}
}
