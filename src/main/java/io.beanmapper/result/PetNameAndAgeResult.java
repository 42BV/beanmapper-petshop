package io.beanmapper.result;

import io.beanmapper.annotations.BeanProperty;

/**
 * Result that show the name and the age of a pet.
 * The age is calculated bij the AgeCalculator which is a BeanConverter.
 * When mapping to this result you should first add the converter.
 */
public class PetNameAndAgeResult {

    public String nickname;
    @BeanProperty(name = "birthDate")
    public Integer age;
}
