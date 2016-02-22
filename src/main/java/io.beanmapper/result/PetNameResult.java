package io.beanmapper.result;

import io.beanmapper.annotations.BeanProperty;

public class PetNameResult {

    @BeanProperty(name = "nickname")
    public String name;
}
