package io.beanmapper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.beanmapper.BeanMapper;
import io.beanmapper.form.AddressForm;
import io.beanmapper.model.Address;
import io.beanmapper.result.AddressResult;
import io.beanmapper.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Controller to show create, read, update & delete use cases for BeanMapper.
 * @see PetController for same use cases but using BeanMapper together with BeanMapper-Spring module.
 */
@RestController
@RequestMapping("/addresses")
public class AddressController {

    @Autowired private BeanMapper beanMapper;
    @Autowired private AddressService addressService;
    @Autowired private ObjectMapper objectMapper;

    @RequestMapping(method = RequestMethod.GET)
    public Collection<AddressResult> findAll() {
        return beanMapper.map(addressService.findAll(), AddressResult.class);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public AddressResult findOne(@PathVariable Long id) {
        return convert(addressService.findOne(id));
    }

    @RequestMapping(method = RequestMethod.POST)
    public AddressResult create(@RequestBody AddressForm addressForm) {
        Address newAddress = beanMapper.map(addressForm, Address.class);
        return convert(addressService.save(newAddress));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public AddressResult update(@PathVariable Long id, @RequestBody AddressForm addressForm) {
        Address oldAddress = addressService.findOne(id);
        Address updatedAddress = beanMapper.map(addressForm, oldAddress);
        return convert(addressService.save(updatedAddress));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public AddressResult partialUpdate(@PathVariable Long id, @RequestBody Map<String, Object> inputFields) {
        AddressForm addressForm = objectMapper.convertValue(inputFields, AddressForm.class);
        Address oldAddress = addressService.findOne(id);
        Address updatedAddress = beanMapper.wrapConfig()
                .downsizeSource(new ArrayList<>(inputFields.keySet()))
                .build()
                .map(addressForm, oldAddress);

        return convert(addressService.save(updatedAddress));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id) {
        addressService.delete(id);
    }

    private AddressResult convert(Address address) {
        return beanMapper.map(address, AddressResult.class);
    }
}