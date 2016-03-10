package io.beanmapper.model;

import io.beanmapper.builders.AddressBuilder;
import io.beanmapper.form.AddressForm;
import io.beanmapper.result.AddressResult;
import org.junit.Test;
import org.springframework.stereotype.Component;

import static org.junit.Assert.assertEquals;

@Component
public class AddressTest extends ModelTest {

    private static final Long ID = 42L;
    private static final String STREET = "CatStreet";
    private static final int NUMBER = 42;
    private static final String CITY = "Amsterdam";

    @Test
    public void addressToAddressResult() {
        AddressResult addressResult = beanMapper.map(createAddress(), AddressResult.class);
        assertEquals(STREET, addressResult.street);
        assertEquals(NUMBER, addressResult.houseNumber);
        assertEquals(CITY, addressResult.city);
    }

    @Test
    public void addressFormToAddress() {
        AddressForm addressForm = new AddressForm();
        addressForm.street = STREET;
        addressForm.number = NUMBER;
        addressForm.city = CITY;

        Address address = beanMapper.map(addressForm, Address.class);
        assertEquals(STREET, address.getStreet());
        assertEquals(NUMBER, address.getNumber());
        assertEquals(CITY, address.getCity());
    }

    public static Address createAddress() {
        return new AddressBuilder()
                .id(ID)
                .street(STREET)
                .number(NUMBER)
                .city(CITY)
                .build();
    }
}
