package io.beanmapper.result;

import io.beanmapper.annotations.BeanDefault;
import io.beanmapper.annotations.BeanProperty;

public class AddressResult {

    public String street;
    @BeanProperty(name = "number")
    public int houseNumber;
    @BeanDefault("Zoetermeer")
    public String city;
}
