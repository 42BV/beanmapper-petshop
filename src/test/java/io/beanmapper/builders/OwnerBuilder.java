package io.beanmapper.builders;

import io.beanmapper.model.Address;
import io.beanmapper.model.Owner;
import io.beanmapper.model.Pet;
import io.beanmapper.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class OwnerBuilder extends AbstractBuilder<Owner> {

    @Autowired
    public OwnerBuilder(OwnerRepository ownerRepository) {
        super(ownerRepository, Owner::new);
    }

    public OwnerBuilder firstName(String firstName) {
        this.entity.setFirstName(firstName);
        return this;
    }

    public OwnerBuilder prefix(String prefix) {
        this.entity.setPrefix(prefix);
        return this;
    }

    public OwnerBuilder lastName(String lastName) {
        this.entity.setLastName(lastName);
        return this;
    }

    public OwnerBuilder address(Address address) {
        this.entity.setAddress(address);
        return this;
    }

    public OwnerBuilder pets(Set<Pet> pets) {
        this.entity.setPets(pets);
        return this;
    }
}
