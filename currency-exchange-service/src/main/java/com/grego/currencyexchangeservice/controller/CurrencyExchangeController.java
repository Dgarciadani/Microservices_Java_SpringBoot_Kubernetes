package com.grego.currencyexchangeservice.controller;

import com.grego.currencyexchangeservice.domain.CurrencyExchange;
import com.grego.currencyexchangeservice.reposiroty.CurrencyExchangeRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class CurrencyExchangeController {

     private final static Logger logger = org.slf4j.LoggerFactory.getLogger(CurrencyExchangeController.class);


    //I KNOW THAT THIS IS NOT A GOOD SOLUTION, BUT I AM TRYING TO KEEP IT SIMPLE
    @Autowired
    private CurrencyExchangeRepository currencyExchangeRepository;
    @Autowired
    private Environment environment;

    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public CurrencyExchange retrieveExchangeValue(@PathVariable String from, @PathVariable String to) {
        //this is for log the id of the request
        logger.info("retrieveExchangeValue called with {} to {}", from, to);
        CurrencyExchange currencyExchange = currencyExchangeRepository.findByFromAndTo(from, to);
        String port = environment.getProperty("local.server.port");
        currencyExchange.setEnvironment(port);
        return currencyExchange;
    }

}
