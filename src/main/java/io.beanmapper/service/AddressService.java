package io.beanmapper.service;

import io.beanmapper.model.Address;
import io.beanmapper.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    @Autowired private AddressRepository addressRepository;

    public List<Address> findAll() {
        return addressRepository.findAll();
    }

    public Address findOne(Long id) {
        return addressRepository.findOne(id);
    }

    public Address save(Address Address) {
        return addressRepository.save(Address);
    }

    public void delete(Long id) {
        addressRepository.delete(id);
    }
}