package com.udemy.ms28min.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.udemy.ms28min.bean.ExchangeValue;

public interface ExchangeValueRepository extends JpaRepository<ExchangeValue, Long>{
	public ExchangeValue findByFromAndTo(String from, String to);
}
