package io.beanmapper.builders;

import io.beanmapper.form.PetForm;
import io.beanmapper.model.Owner;
import io.beanmapper.model.Pet;
import io.beanmapper.model.PetType;
import io.beanmapper.repository.PetRepository;
import io.beanmapper.result.PetNameAndAgeResult;
import io.beanmapper.result.PetNameResult;
import io.beanmapper.result.PetResult;
import io.beanmapper.result.PetTypeResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class PetBuilder extends AbstractBuilder<Pet, PetResult, PetForm> {

    private PetNameResult petNameResult;
    private PetNameAndAgeResult petNameAndAgeResult;

    public PetBuilder() {
        super(null, Pet::new, PetResult::new);
    }

    @Autowired
    public PetBuilder(PetRepository petRepository) {
        super(petRepository, Pet::new, PetResult::new);
    }

    public PetBuilder id(Long id) {
        entity.setId(id);
        return this;
    }

    public PetBuilder nickname(String nickname) {
        entity.setNickname(nickname);
        result.nickname = nickname;
        petNameResult.name = nickname;
        petNameAndAgeResult.nickname = nickname;
        return this;
    }

    public PetBuilder birthDate(LocalDate birthDate) {
        entity.setBirthDate(birthDate);
        result.birthDate = birthDate;
        petNameAndAgeResult.age = birthDate.until(LocalDate.now()).getYears();
        return this;
    }

    public PetBuilder sex(Pet.Sex sex) {
        entity.setSex(sex);
        result.sex = sex.toString();
        return this;
    }

    public PetBuilder type(PetType petType) {
        entity.setType(petType);
        return this;
    }

    public PetBuilder type(Build<PetType, PetTypeResult, ?> typeBuild) {
        entity.setType(typeBuild.entity);
        result.type = typeBuild.result;
        return this;
    }

    public PetBuilder owner(Owner owner) {
        entity.setOwner(owner);
        return this;
    }

    @Override
    public PetBuild build() {
        Build build = super.build();
        PetBuild petBuild = new PetBuild(build, petNameResult, petNameAndAgeResult);
        this.petNameResult = new PetNameResult();
        this.petNameAndAgeResult = new PetNameAndAgeResult();
        return petBuild;
    }

    public class PetBuild extends Build<Pet, PetResult, PetForm> {
        public PetNameResult petNameResult;
        public PetNameAndAgeResult petNameAndAgeResult;

        public PetBuild(Build build, PetNameResult petNameResult, PetNameAndAgeResult petNameAndAgeResult) {
            super((Pet)build.entity, (PetResult)build.result, (PetForm)build.form);
            this.petNameResult = petNameResult;
            this.petNameAndAgeResult = petNameAndAgeResult;
        }
    }
}
