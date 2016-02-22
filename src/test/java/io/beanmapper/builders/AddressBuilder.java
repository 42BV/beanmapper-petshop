package io.beanmapper.builders;

import io.beanmapper.model.Address;
import io.beanmapper.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddressBuilder extends AbstractBuilder<Address> {

    @Autowired
    public AddressBuilder(AddressRepository addressRepository) {
        super(addressRepository, Address::new);
    }

    public AddressBuilder street(String street) {
        this.entity.setStreet(street);
        return this;
    }

    public AddressBuilder number(int number) {
        this.entity.setNumber(number);
        return this;
    }

    public AddressBuilder city(String city) {
        this.entity.setCity(city);
        return this;
    }
}
