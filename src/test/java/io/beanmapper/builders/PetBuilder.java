package io.beanmapper.builders;

import io.beanmapper.model.Owner;
import io.beanmapper.model.Pet;
import io.beanmapper.model.PetType;
import io.beanmapper.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class PetBuilder extends AbstractBuilder<Pet> {

    public PetBuilder() {
        super(null, Pet::new);
    }

    @Autowired
    public PetBuilder(PetRepository petRepository) {
        super(petRepository, Pet::new);
    }

    public PetBuilder id(Long id) {
        entity.setId(id);
        return this;
    }

    public PetBuilder nickname(String nickname) {
        entity.setNickname(nickname);
        return this;
    }

    public PetBuilder birthDate(LocalDate birthDate) {
        entity.setBirthDate(birthDate);
        return this;
    }

    public PetBuilder sex(Pet.Sex sex) {
        entity.setSex(sex);
        return this;
    }

    public PetBuilder type(PetType petType) {
        entity.setType(petType);
        return this;
    }

    public PetBuilder owner(Owner owner) {
        entity.setOwner(owner);
        return this;
    }
}
