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

import io.mm.meetup.shipper_ws.Shipper;

@Component
public class ShipperRepository {
	private static final List<Shipper> shippers = new LinkedList<>();

	@PostConstruct
	public void initData() throws FileNotFoundException, IOException {
		Resource resource = new ClassPathResource("shippers.csv");
		try (Reader reader = new InputStreamReader(resource.getInputStream())) {
			//Skip Headers
			CsvParser.stream(reader).skip(1).forEach(line -> {
				Shipper shipper = new Shipper();
				shipper.setShipperId(line[SHIPPERSCSVCOLS.shipperId.ordinal()]);
				shipper.setCompanyName(line[SHIPPERSCSVCOLS.companyName.ordinal()]);
				shipper.setPhone(Long.parseLong(line[SHIPPERSCSVCOLS.phone.ordinal()]));
				
				shippers.add(shipper);
			});
		}

	}

	public List<Shipper> findShipperByCompanyName(String companyName) {
		Assert.notNull(companyName, "The shipper's company name must not be null");
		return shippers.stream().filter(res -> companyName.equals(res.getCompanyName())).collect(Collectors.toList());
	}
	
	public List<Shipper> findShipperById(String id) {
		Assert.notNull(id, "The shipper's id must not be null");
		return shippers.stream().filter(res -> id.equals(res.getShipperId())).collect(Collectors.toList());
	}
	
	public List<Shipper> findShipperByPhone(long phone) {
		Assert.notNull(phone, "The shipper's phone must not be null");
		return shippers.stream().filter(res -> phone==res.getPhone()).collect(Collectors.toList());
	}
	
	public List<Shipper> findShipperByIdAndCompanyName(String id,String companyName) {
		Assert.notNull(id, "The shipper's id must not be null");
		Assert.notNull(companyName, "The shipper's company name must not be null");
		return shippers.stream().filter(res -> id.equals(res.getShipperId()) && companyName.equals(res.getCompanyName())).collect(Collectors.toList());
	}
	
	public List<Shipper> findShipperByIdAndPhone(String id,long phone) {
		Assert.notNull(id, "The shipper's id must not be null");
		Assert.notNull(phone, "The shipper's phone must not be null");
		return shippers.stream().filter(res -> id.equals(res.getShipperId()) && phone==res.getPhone()).collect(Collectors.toList());
	}
	
	public List<Shipper> findShipperByCompanyNameAndPhone(String companyName,long phone) {
		Assert.notNull(companyName, "The shipper's company name must not be null");
		Assert.notNull(phone, "The shipper's phone must not be null");
		return shippers.stream().filter(res -> companyName.equals(res.getCompanyName()) && phone==res.getPhone()).collect(Collectors.toList());
	}
	
	public List<Shipper> findShipperByIdAndCompanyNameAndPhone(String id,String companyName,long phone) {
		Assert.notNull(id, "The shipper's id must not be null");
		Assert.notNull(companyName, "The shipper's company name must not be null");
		Assert.notNull(phone, "The shipper's phone must not be null");
		return shippers.stream().filter(res -> id.equals(res.getShipperId()) && companyName.equals(res.getCompanyName()) && phone==res.getPhone()).collect(Collectors.toList());
	}
	
	public List<Shipper> getAllShippers() {
		return shippers;
	}
}

enum SHIPPERSCSVCOLS {
    shipperId, companyName, phone
}