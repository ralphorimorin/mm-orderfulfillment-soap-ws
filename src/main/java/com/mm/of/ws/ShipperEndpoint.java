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

import io.mm.meetup.shipper_ws.GetShippersRequest;
import io.mm.meetup.shipper_ws.GetShippersResponse;
import io.mm.meetup.shipper_ws.Shipper;

@Endpoint
public class ShipperEndpoint {
	private static final String NAMESPACE_URI = "http://mm.io/meetup/shipper-ws";

	private ShipperRepository shipperRepository;

	@Autowired
	public ShipperEndpoint(ShipperRepository shipperRepository) {
		this.shipperRepository = shipperRepository;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "getShippersRequest")
	@ResponsePayload
	public GetShippersResponse getShippers(@RequestPayload GetShippersRequest request) {
		GetShippersResponse response = new GetShippersResponse();
		List<Shipper> shipperList = new CopyOnWriteArrayList<Shipper>();
		if(request.getSearchBy()==null || request.getSearchBy().isEmpty()) {
			shipperList.addAll(shipperRepository.getAllShippers());
		}
		if(request.getSearchBy()!=null && !request.getSearchBy().isEmpty()) {
			if(request.getSearchBy().contains(",")) {
				String[] splittedSearchStr = request.getSearchBy().split("\\,");
				if(splittedSearchStr.length == 2) {
					List<String> toList = new ArrayList<>(Arrays.asList(splittedSearchStr));
					if(toList.contains("id") && toList.contains("companyName") 
							&& request.getId()!=null && !request.getId().isEmpty()
							&& request.getCompanyName()!=null && !request.getCompanyName().isEmpty()) {
						shipperList.addAll(shipperRepository.findShipperByIdAndCompanyName(request.getId(),request.getCompanyName()));
					} else if(toList.contains("id") && toList.contains("phone") 
							&& request.getId()!=null && !request.getId().isEmpty()
							&& request.getPhone()>0) {
						shipperList.addAll(shipperRepository.findShipperByIdAndPhone(request.getId(),request.getPhone()));
					} else if(toList.contains("companyName") && toList.contains("phone") 
							&& request.getCompanyName()!=null && !request.getCompanyName().isEmpty()
							&& request.getPhone()>0) {
						shipperList.addAll(shipperRepository.findShipperByCompanyNameAndPhone(request.getCompanyName(),request.getPhone()));
					}
				} else if(splittedSearchStr.length > 2) {
					List<String> toList = new ArrayList<>(Arrays.asList(splittedSearchStr));
					if(toList.contains("id") && toList.contains("companyName") && toList.contains("phone") 
							&& request.getId()!=null && !request.getId().isEmpty()
							&& request.getCompanyName()!=null && !request.getCompanyName().isEmpty()
							&& request.getPhone()>0) {
						shipperList.addAll(shipperRepository.findShipperByIdAndCompanyNameAndPhone(request.getId(),request.getCompanyName(),request.getPhone()));
					}
				}
				else {
					if(splittedSearchStr[0].equals("id") && request.getId()!=null && !request.getId().isEmpty()) {
						shipperList.addAll(shipperRepository.findShipperById(request.getId()));
					} else if (splittedSearchStr[0].equals("companyName") && request.getCompanyName()!=null && !request.getCompanyName().isEmpty()) {
						shipperList.addAll(shipperRepository.findShipperByCompanyName(request.getCompanyName()));
					} else if (splittedSearchStr[0].equals("phone") && request.getPhone()!=null && request.getPhone()>0) {
						shipperList.addAll(shipperRepository.findShipperByPhone(request.getPhone()));
					}
				}
					
			} else {
				String searchStr = request.getSearchBy();
				if(searchStr.equals("id") && request.getId()!=null && !request.getId().isEmpty()) {
					shipperList.addAll(shipperRepository.findShipperById(request.getId()));
				} else if (searchStr.equals("companyName") && request.getCompanyName()!=null && !request.getCompanyName().isEmpty()) {
					shipperList.addAll(shipperRepository.findShipperByCompanyName(request.getCompanyName()));
				} else if (searchStr.equals("phone") && request.getPhone()!=null && request.getPhone() > 0) {
					shipperList.addAll(shipperRepository.findShipperByPhone(request.getPhone()));
				}
			}
		}
		shipperList.forEach(prod->response.getShippers().add(prod));
		return response;
	}
}
