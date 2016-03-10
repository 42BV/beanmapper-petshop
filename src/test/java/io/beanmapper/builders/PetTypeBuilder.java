package io.beanmapper.builders;

import io.beanmapper.model.PetType;
import io.beanmapper.repository.PetTypeRepository;
import io.beanmapper.result.PetTypeResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PetTypeBuilder extends AbstractBuilder<PetType, PetTypeResult> {

    public PetTypeBuilder() {
        super(null, PetType::new, PetTypeResult::new);
    }

    @Autowired
    public PetTypeBuilder(PetTypeRepository petTypeRepository) {
        super(petTypeRepository, PetType::new, PetTypeResult::new);
    }

    public PetTypeBuilder id(Long id) {
        entity.setId(id);
        return this;
    }

    public PetTypeBuilder type(String type) {
        entity.setType(type);
        result.type = type;
        return this;
    }

    public PetTypeBuilder familyName(String familyName) {
        entity.setFamilyName(familyName);
        result.familyName = familyName;
        return this;
    }
}
