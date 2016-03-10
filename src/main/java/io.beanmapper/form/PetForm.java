package io.beanmapper.form;

import io.beanmapper.annotations.BeanProperty;
import io.beanmapper.model.Pet;

import java.time.LocalDate;

public class PetForm {

    public String nickname;
    public LocalDate birthDate;
    public Pet.Sex sex;
    @BeanProperty(name = "type.id")
    public Long petTypeId;
}
