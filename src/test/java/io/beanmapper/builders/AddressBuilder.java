package io.beanmapper.builders;

import io.beanmapper.form.AddressForm;
import io.beanmapper.model.Address;
import io.beanmapper.repository.AddressRepository;
import io.beanmapper.result.AddressResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddressBuilder extends AbstractBuilder<Address, AddressResult, AddressForm> {

    public AddressBuilder() {
        super(null, Address::new, AddressResult::new, AddressForm::new);
    }

    @Autowired
    public AddressBuilder(AddressRepository addressRepository) {
        super(addressRepository, Address::new, AddressResult::new, AddressForm::new);
    }

    public AddressBuilder id(Long id) {
        this.entity.setId(id);
        return this;
    }

    public AddressBuilder street(String street) {
        this.entity.setStreet(street);
        this.result.street = street;
        this.form.street = street;
        return this;
    }

    public AddressBuilder number(int number) {
        this.entity.setNumber(number);
        this.result.houseNumber = number;
        this.form.number = number;
        return this;
    }

    public AddressBuilder city(String city) {
        this.entity.setCity(city);
        this.result.city = city;
        this.form.city = city;
        return this;
    }
}
