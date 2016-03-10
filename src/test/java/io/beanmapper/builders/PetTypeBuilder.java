package io.beanmapper.builders;

import io.beanmapper.model.PetType;
import io.beanmapper.repository.PetTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PetTypeBuilder extends AbstractBuilder<PetType> {

    public PetTypeBuilder() {
        super(null, PetType::new);
    }

    @Autowired
    public PetTypeBuilder(PetTypeRepository petTypeRepository) {
        super(petTypeRepository, PetType::new);
    }

    public PetTypeBuilder id(Long id) {
        entity.setId(id);
        return this;
    }

    public PetTypeBuilder type(String type) {
        entity.setType(type);
        return this;
    }

    public PetTypeBuilder familyName(String familyName) {
        entity.setFamilyName(familyName);
        return this;
    }
}
