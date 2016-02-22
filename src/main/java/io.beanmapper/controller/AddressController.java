package io.beanmapper.controller;

import io.beanmapper.BeanMapper;
import io.beanmapper.result.AddressResult;
import io.beanmapper.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/addresses")
public class AddressController {

    @Autowired private BeanMapper beanMapper;
    @Autowired private AddressService addressService;

    @RequestMapping(method = RequestMethod.GET)
    public Collection<AddressResult> findAll() {
        return beanMapper.map(addressService.findAll(), AddressResult.class);
    }
}