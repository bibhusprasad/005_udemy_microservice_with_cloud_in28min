package com.udemy.ms28min.service;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.udemy.ms28min.bean.CurrencyConversionBean;

/*
 * comment for @RibbonClient as because it will load and balance multiple instance service
 */
//@FeignClient(name="currency-exchange-service", url="http://localhost:8000")
/* 
 * comment for routing through Zuul api gate way.
 * Instead of hard coded pick the service name from zuul api service
 * project work 2 step 11
 */
//@FeignClient(name="currency-exchange-service")

@FeignClient(name="netflix-zuul-api-gateway-server")
@RibbonClient(name="currency-exchange-service")
public interface CurrencyExchangeServiceProxy {
	
	//appened as because we are picking the application name from naming server. project work 2 step 11
	//@GetMapping("/currency-exchange/from/{from}/to/{to}")
	@GetMapping("/currency-exchange-service/currency-exchange/from/{from}/to/{to}")
	public CurrencyConversionBean retriveExchangeValue(@PathVariable String from, @PathVariable String to);
}