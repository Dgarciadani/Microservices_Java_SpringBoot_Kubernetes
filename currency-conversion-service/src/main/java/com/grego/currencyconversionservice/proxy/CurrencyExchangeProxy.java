package com.grego.currencyconversionservice.proxy;

import com.grego.currencyconversionservice.domain.CurrencyConversion;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
//this is a static method
//@FeignClient(name = "currency-exchange", url = "localhost:8000")
//this is a dynamic method, use load balancer. Use the name of the microservice to balance the request
@FeignClient(name = "currency-exchange")
public interface CurrencyExchangeProxy {

    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public CurrencyConversion retrieveExchangeValue(@PathVariable String from, @PathVariable String to);

}
