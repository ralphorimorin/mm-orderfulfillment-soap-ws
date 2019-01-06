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

import io.mm.meetup.vendor_ws.GetVendorsRequest;
import io.mm.meetup.vendor_ws.GetVendorsResponse;
import io.mm.meetup.vendor_ws.Vendor;

@Endpoint
public class VendorEndpoint {
	private static final String NAMESPACE_URI = "http://mm.io/meetup/vendor-ws";

	private VendorRepository vendorRepository;

	@Autowired
	public VendorEndpoint(VendorRepository vendorRepository) {
		this.vendorRepository = vendorRepository;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "getVendorsRequest")
	@ResponsePayload
	public GetVendorsResponse getVendors(@RequestPayload GetVendorsRequest request) {
		GetVendorsResponse response = new GetVendorsResponse();
		List<Vendor> vendorList = new CopyOnWriteArrayList<Vendor>();
		if(request.getSearchBy()==null || request.getSearchBy().isEmpty()) {
			vendorList.addAll(vendorRepository.getAllVendors());
		}
		if(request.getSearchBy()!=null && !request.getSearchBy().isEmpty()) {
			if(request.getSearchBy().contains(",")) {
				String[] splittedSearchStr = request.getSearchBy().split("\\,");
				if(splittedSearchStr.length == 2) {
					List<String> toList = new ArrayList<>(Arrays.asList(splittedSearchStr));
					if(toList.contains("id") && toList.contains("name") 
							&& request.getId()!=null && !request.getId().isEmpty()
							&& request.getName()!= null && !request.getName().isEmpty()) {
						vendorList.addAll(vendorRepository.findVendorByIdAndCompanyName(request.getId(),request.getName()));
					} else if(toList.contains("id") && toList.contains("phone") 
							&& request.getId()!=null && !request.getId().isEmpty()
							&& request.getPhone()>0) {
						vendorList.addAll(vendorRepository.findVendorByIdAndPhone(request.getId(),request.getPhone()));
					} else if(toList.contains("name") && toList.contains("phone") 
							&& request.getName()!=null && !request.getName().isEmpty()
							&& request.getPhone()>0) {
						vendorList.addAll(vendorRepository.findVendorByCompanyNameAndPhone(request.getName(),request.getPhone()));
					}
				} else if(splittedSearchStr.length > 2) {
					List<String> toList = new ArrayList<>(Arrays.asList(splittedSearchStr));
					if(toList.contains("id") && toList.contains("name") && toList.contains("phone") 
							&& request.getId()!=null && !request.getId().isEmpty()
							&& request.getName()!=null && !request.getName().isEmpty()
							&& request.getPhone()>0) {
						vendorList.addAll(vendorRepository.findVendorByIdAndCompanyNameAndPhone(request.getId(),request.getName(),request.getPhone()));
					}
				}
				else {
					if(splittedSearchStr[0].equals("id") && request.getId()!=null && !request.getId().isEmpty()) {
						vendorList.addAll(vendorRepository.findVendorById(request.getId()));
					} else if (splittedSearchStr[0].equals("name") && request.getName()!=null && !request.getName().isEmpty()) {
						vendorList.addAll(vendorRepository.findVendorByName(request.getName()));
					} else if (splittedSearchStr[0].equals("phone") && request.getPhone()!=null && request.getPhone()>0) {
						vendorList.addAll(vendorRepository.findVendorByPhone(request.getPhone()));
					}
				}
					
			} else {
				String searchStr = request.getSearchBy();
				if(searchStr.equals("id") && request.getId()!=null && !request.getId().isEmpty()) {
					vendorList.addAll(vendorRepository.findVendorById(request.getId()));
				} else if (searchStr.equals("companyName") && request.getName()!=null && !request.getName().isEmpty()) {
					vendorList.addAll(vendorRepository.findVendorByName(request.getName()));
				} else if (searchStr.equals("phone") && request.getPhone()!=null && request.getPhone() > 0) {
					vendorList.addAll(vendorRepository.findVendorByPhone(request.getPhone()));
				}
			}
		}
		vendorList.forEach(prod->response.getVendors().add(prod));
		return response;
	}
}
