package com.udemy.ms28min.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.udemy.ms28min.Configuration;
import com.udemy.ms28min.bean.LimitsConfiguration;

@RestController
public class LimitsConfigurationController {

	@Autowired
	private Configuration configuration;

	@GetMapping("/limits")
	public LimitsConfiguration retriveLimitsFromConfiguration() {
		return new LimitsConfiguration(configuration.getMaximum(), configuration.getMinimum());
	}
	
	@GetMapping("/limits-fault")
	//project work 1 step 6
	// if any exception throw here then it will go to fallback method
	@HystrixCommand(fallbackMethod="fallbackRetriveConfiguration")
	public LimitsConfiguration retriveLimitsFromConfigurationFaultTolerance() {
		throw new RuntimeException("own exception");
	}
	
	public LimitsConfiguration fallbackRetriveConfiguration() {
		return new LimitsConfiguration(5, 5555);
	}
}
