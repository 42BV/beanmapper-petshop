package io.beanmapper.builders;

import io.beanmapper.model.Address;
import io.beanmapper.model.Owner;
import io.beanmapper.model.Pet;
import io.beanmapper.repository.OwnerRepository;
import io.beanmapper.result.OwnerResult;
import io.beanmapper.result.PetNameResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class OwnerBuilder extends AbstractBuilder<Owner, OwnerResult, Owner> {

    public OwnerBuilder() {
        super(null, Owner::new, OwnerResult::new);
    }

    @Autowired
    public OwnerBuilder(OwnerRepository ownerRepository) {
        super(ownerRepository, Owner::new, OwnerResult::new);
    }

    public OwnerBuilder id(Long id) {
        this.entity.setId(id);
        return this;
    }

    public OwnerBuilder firstName(String firstName) {
        this.entity.setFirstName(firstName);
        this.result.fullName = firstName + " " + entity.getPrefix() + " " + entity.getLastName();
        return this;
    }

    public OwnerBuilder prefix(String prefix) {
        this.entity.setPrefix(prefix);
        this.result.fullName = entity.getFirstName() + " " + prefix + " " + entity.getLastName();
        return this;
    }

    public OwnerBuilder lastName(String lastName) {
        this.entity.setLastName(lastName);
        this.result.fullName = entity.getFirstName() + " " + entity.getPrefix() + " " + lastName;
        return this;
    }

    public OwnerBuilder address(Address address) {
        this.entity.setAddress(address);
        this.result.livingPlace = address.getCity();
        return this;
    }

    public OwnerBuilder pets(Set<Pet> pets) {
        this.entity.setPets(pets);
        return this;
    }

    public OwnerBuilder petbuilds(List<PetBuilder.PetBuild> petBuilds) {
        Set<Pet> pets = new LinkedHashSet<>();
        List<PetNameResult> petNameResults = new ArrayList<>();
        for(PetBuilder.PetBuild petBuild : petBuilds) {
            pets.add(petBuild.entity);
            petNameResults.add(petBuild.petNameResult);
        }
        this.entity.setPets(pets);
        this.result.pets = petNameResults;
        return this;
    }
}
