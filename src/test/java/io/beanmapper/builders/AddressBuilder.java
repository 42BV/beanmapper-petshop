package io.beanmapper.builders;

import io.beanmapper.model.Address;
import io.beanmapper.repository.AddressRepository;
import io.beanmapper.result.AddressResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddressBuilder extends AbstractBuilder<Address, AddressResult> {

    public AddressBuilder() {
        super(null, Address::new);
    }

    @Autowired
    public AddressBuilder(AddressRepository addressRepository) {
        super(addressRepository, Address::new);
    }

    public AddressBuilder id(Long id) {
        this.entity.setId(id);
        return this;
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
