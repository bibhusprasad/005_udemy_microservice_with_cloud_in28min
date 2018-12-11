package com.udemy.ms28min.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.udemy.ms28min.bean.CurrencyConversionBean;
import com.udemy.ms28min.service.CurrencyExchangeServiceProxy;

@RestController
public class CurrencyConversionController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyConversionController.class);

	@Autowired
	private CurrencyExchangeServiceProxy proxy;

	@Autowired
	private Environment environment;

	@GetMapping("/currency-convert/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversionBean getConversionValue(@PathVariable String from, @PathVariable String to,
			@PathVariable String quantity) {

		String url = "http://localhost:8000/currency-exchange/from/{from}/to/{to}";
		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("from", from);
		uriVariables.put("to", to);
		ResponseEntity<CurrencyConversionBean> responseEntity = new RestTemplate().getForEntity(url,
				CurrencyConversionBean.class, uriVariables);
		CurrencyConversionBean responseBody = responseEntity.getBody();

		BigDecimal quantityNo = new BigDecimal(quantity);
		CurrencyConversionBean currencyConversionBean = new CurrencyConversionBean(responseBody.getId(), from, to,
				responseBody.getConversionMultiple(), quantityNo,
				quantityNo.multiply(responseBody.getConversionMultiple()), responseBody.getPort());
		return currencyConversionBean;
	}

	@GetMapping("/currency-convert-feign/from/{from}/to/{to}/quantity/{quantity}")
	@HystrixCommand(fallbackMethod = "fallbackRetriveConfiguration")
	public CurrencyConversionBean getConversionValueFeign(@PathVariable String from, @PathVariable String to,
			@PathVariable String quantity) {

		CurrencyConversionBean bean = proxy.retriveExchangeValue(from, to);

		LOGGER.info("Bean : {}", bean);

		BigDecimal quantityNo = new BigDecimal(quantity);
		return new CurrencyConversionBean(bean.getId(), from, to, bean.getConversionMultiple(), quantityNo,
				quantityNo.multiply(bean.getConversionMultiple()), bean.getPort());
	}

	public CurrencyConversionBean fallbackRetriveConfiguration(String from, String to, String quantity) {
		int port = Integer.parseInt(environment.getProperty("local.server.port"));
		BigDecimal quantityNo = new BigDecimal(quantity);
		return new CurrencyConversionBean(0l, from, to, BigDecimal.valueOf(0), quantityNo, BigDecimal.valueOf(0), port);
	}
}
