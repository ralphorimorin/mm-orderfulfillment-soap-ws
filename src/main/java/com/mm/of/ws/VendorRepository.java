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

import io.mm.meetup.vendor_ws.Vendor;

@Component
public class VendorRepository {
	private static final List<Vendor> vendors = new LinkedList<>();

	@PostConstruct
	public void initData() throws FileNotFoundException, IOException {
		Resource resource = new ClassPathResource("vendors.csv");
		try (Reader reader = new InputStreamReader(resource.getInputStream())) {
			//Skip Headers
			CsvParser.stream(reader).skip(1).forEach(line -> {
				Vendor vendor = new Vendor();
				vendor.setVendorId(line[VENDORSCSVCOLS.vendorId.ordinal()]);
				vendor.setVendorName(line[VENDORSCSVCOLS.vendorName.ordinal()]);
				vendor.setStreet(line[VENDORSCSVCOLS.street.ordinal()]);
				vendor.setCity(line[VENDORSCSVCOLS.city.ordinal()]);
				vendor.setState(line[VENDORSCSVCOLS.state.ordinal()]);
				vendor.setCountry(line[VENDORSCSVCOLS.country.ordinal()]);
				vendor.setZipCode(Integer.parseInt(line[VENDORSCSVCOLS.zipCode.ordinal()]));
				vendor.setPhone(Long.parseLong(line[VENDORSCSVCOLS.phone.ordinal()]));

				vendors.add(vendor);
			});
		}

	}

	public List<Vendor> findVendorByName(String vendorName) {
		Assert.notNull(vendorName, "The vendor's name must not be null");
		return vendors.stream().filter(res -> vendorName.equals(res.getVendorName())).collect(Collectors.toList());
	}
	
	public List<Vendor> findVendorById(String id) {
		Assert.notNull(id, "The vendor's id must not be null");
		return vendors.stream().filter(res -> id.equals(res.getVendorId())).collect(Collectors.toList());
	}
	
	public List<Vendor> findVendorByPhone(long phone) {
		Assert.notNull(phone, "The vendor's phone must not be null");
		return vendors.stream().filter(res -> phone==res.getPhone()).collect(Collectors.toList());
	}
	
	public List<Vendor> findVendorByIdAndCompanyName(String id,String companyName) {
		Assert.notNull(id, "The vendor's id must not be null");
		Assert.notNull(companyName, "The shipper's company name must not be null");
		return vendors.stream().filter(res -> id.equals(res.getVendorId()) && companyName.equals(res.getVendorName())).collect(Collectors.toList());
	}
	
	public List<Vendor> findVendorByIdAndPhone(String id,long phone) {
		Assert.notNull(id, "The vendor's id must not be null");
		Assert.notNull(phone, "The vendor's phone must not be null");
		return vendors.stream().filter(res -> id.equals(res.getVendorId()) && phone==res.getPhone()).collect(Collectors.toList());
	}
	
	public List<Vendor> findVendorByCompanyNameAndPhone(String companyName,long phone) {
		Assert.notNull(companyName, "The vendor's company name must not be null");
		Assert.notNull(phone, "The vendor's phone must not be null");
		return vendors.stream().filter(res -> companyName.equals(res.getVendorName()) && phone==res.getPhone()).collect(Collectors.toList());
	}
	
	public List<Vendor> findVendorByIdAndCompanyNameAndPhone(String id,String companyName,long phone) {
		Assert.notNull(id, "The vendor's id must not be null");
		Assert.notNull(companyName, "The vendor's company name must not be null");
		Assert.notNull(phone, "The vendor's phone must not be null");
		return vendors.stream().filter(res -> id.equals(res.getVendorId()) && companyName.equals(res.getVendorName()) && phone==res.getPhone()).collect(Collectors.toList());
	}
	
	public List<Vendor> getAllVendors() {
		return vendors;
	}
}

enum VENDORSCSVCOLS {
	vendorId, vendorName, street, city, state, country, zipCode, phone
}
