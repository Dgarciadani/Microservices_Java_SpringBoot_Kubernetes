package com.grego.currencyconversionservice.controller;

import com.grego.currencyconversionservice.domain.CurrencyConversion;
import com.grego.currencyconversionservice.proxy.CurrencyExchangeProxy;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;

@RestController
public class CurrencyConversionController {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(CurrencyConversionController.class);
    @Autowired
    private CurrencyExchangeProxy proxy;

    //This is a static method, and use restTemplate
    @GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion calculateCurrencyConversion(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {

        HashMap<String, String> uriVariables = new HashMap<>();
        uriVariables.put("from", from);
        uriVariables.put("to",to);

        //ADD logger to see the request
        LOGGER.info("Calculate currency conversion from {} to {} with quantity {}", from, to, quantity);

       ResponseEntity<CurrencyConversion> entity = new RestTemplate().getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}", CurrencyConversion.class, uriVariables);

        return new CurrencyConversion(entity.getBody().getId(), entity.getBody().getFrom(), entity.getBody().getTo(), quantity, entity.getBody().getConversionMultiple(), quantity.multiply(entity.getBody().getConversionMultiple()), entity.getBody().getEnvironment());
    }


    //this is a dynamic method and it uses proxies
    @GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion calculateCurrencyConversionFeign(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {

        //ADD logger to see the request
        LOGGER.info("Calculate currency conversion from {} to {} with quantity {}", from, to, quantity);

        CurrencyConversion entity = proxy.retrieveExchangeValue(from,to);

        return new CurrencyConversion(entity.getId(), entity.getFrom(), entity.getTo(), quantity, entity.getConversionMultiple(), quantity.multiply(entity.getConversionMultiple()), entity.getEnvironment() + " Feign");
    }


}
