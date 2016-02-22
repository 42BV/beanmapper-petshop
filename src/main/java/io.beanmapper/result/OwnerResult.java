package io.beanmapper.result;

import io.beanmapper.annotations.BeanCollection;
import io.beanmapper.annotations.BeanCollectionUsage;
import io.beanmapper.annotations.BeanConstruct;
import io.beanmapper.annotations.BeanProperty;

import java.util.List;

@BeanConstruct({"firstName", "prefix", "lastName"})
public class OwnerResult {

    public String fullName;
    @BeanProperty(name = "address.city")
    public String livingPlace;
    @BeanCollection(elementType = PetNameResult.class, beanCollectionUsage = BeanCollectionUsage.CONSTRUCT)
    public List<PetNameResult> pets;

    public OwnerResult(String firstName, String prefix, String lastName) {
        this.fullName = firstName + " " + prefix + " " + lastName;
    }
}
