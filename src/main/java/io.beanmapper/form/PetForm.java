package io.beanmapper.form;

import io.beanmapper.annotations.BeanProperty;

import java.time.LocalDate;

public class PetForm {

    public String nickname;
    public LocalDate birthDate;
    public String sex;
    @BeanProperty(name = "type.id")
    public Long petTypeId;
}
