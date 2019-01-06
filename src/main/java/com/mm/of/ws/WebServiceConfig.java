package com.mm.of.ws;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {
	@Bean
	public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
		MessageDispatcherServlet servlet = new MessageDispatcherServlet();
		servlet.setApplicationContext(applicationContext);
		servlet.setTransformWsdlLocations(true);
		return new ServletRegistrationBean(servlet, "/ws/*");
	}
	
	@Bean(name = "products")
	public DefaultWsdl11Definition productWsdl11Definition(XsdSchema productsSchema) {
		DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
		wsdl11Definition.setPortTypeName("ProductListPort");
		wsdl11Definition.setLocationUri("/ws");
		wsdl11Definition.setTargetNamespace("http://mm.io/meetup/product-ws");
		wsdl11Definition.setSchema(productsSchema);
		return wsdl11Definition;
	}
	
	@Bean
	public XsdSchema productsSchema() {
		return new SimpleXsdSchema(new ClassPathResource("products.xsd"));
	}
	
	@Bean(name = "productcategories")
	public DefaultWsdl11Definition productCategoriesWsdl11Definition(XsdSchema productCategoriesSchema) {
		DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
		wsdl11Definition.setPortTypeName("ProductCategoriesPort");
		wsdl11Definition.setLocationUri("/ws");
		wsdl11Definition.setTargetNamespace("http://mm.io/meetup/productcategories-ws");
		wsdl11Definition.setSchema(productCategoriesSchema);
		return wsdl11Definition;
	}
	
	@Bean
	public XsdSchema productCategoriesSchema() {
		return new SimpleXsdSchema(new ClassPathResource("productcategories.xsd"));
	}
	
	@Bean(name = "shippers")
	public DefaultWsdl11Definition shippersWsdl11Definition(XsdSchema shippersSchema) {
		DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
		wsdl11Definition.setPortTypeName("ShippersPort");
		wsdl11Definition.setLocationUri("/ws");
		wsdl11Definition.setTargetNamespace("http://mm.io/meetup/shippers-ws");
		wsdl11Definition.setSchema(shippersSchema);
		return wsdl11Definition;
	}
	
	@Bean
	public XsdSchema shippersSchema() {
		return new SimpleXsdSchema(new ClassPathResource("shippers.xsd"));
	}
	
	@Bean(name = "vendors")
	public DefaultWsdl11Definition vendorsWsdl11Definition(XsdSchema vendorsSchema) {
		DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
		wsdl11Definition.setPortTypeName("VendorsPort");
		wsdl11Definition.setLocationUri("/ws");
		wsdl11Definition.setTargetNamespace("http://mm.io/meetup/vendors-ws");
		wsdl11Definition.setSchema(vendorsSchema);
		return wsdl11Definition;
	}
	
	@Bean
	public XsdSchema vendorsSchema() {
		return new SimpleXsdSchema(new ClassPathResource("vendors.xsd"));
	}
}