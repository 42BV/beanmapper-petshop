package io.beanmapper;

import io.beanmapper.builders.AddressBuilder;
import io.beanmapper.builders.OwnerBuilder;
import io.beanmapper.builders.PetBuilder;
import io.beanmapper.builders.PetTypeBuilder;
import io.beanmapper.model.Address;
import io.beanmapper.model.Owner;
import io.beanmapper.model.Pet;
import io.beanmapper.model.PetType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.init.ScriptException;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class DatabasePopulator implements org.springframework.jdbc.datasource.init.DatabasePopulator {

    @Autowired private PetBuilder petBuilder;
    @Autowired private PetTypeBuilder petTypeBuilder;
    @Autowired private AddressBuilder addressBuilder;
    @Autowired private OwnerBuilder ownerBuilder;

    @Override
    public void populate(Connection connection) throws SQLException, ScriptException {
        PetType dog = petTypeBuilder.type("Dog").familyName("Canidae").save();
        PetType cat = petTypeBuilder.type("Cat").familyName("Felidae").save();
        PetType mouse = petTypeBuilder.type("Mouse").familyName("Muridae").save();

        Address addressOwner1 = addressBuilder.street("CatStreet").number(15).city("Amsterdam").save();
        Address addressOwner2 = addressBuilder.street("MouseWay").number(324).city(null).save();

        Owner owner1 = ownerBuilder.firstName("Esther").prefix("van").lastName("Ouwehand").address(addressOwner1).save();
        Owner owner2 = ownerBuilder.firstName("Frank").lastName("Wassenberg").address(addressOwner2).save();

        petBuilder.nickname("Loebas").birthDate(LocalDate.of(2003, 12, 3)).sex(Pet.Sex.MALE).type(dog).owner(owner1).save();
        petBuilder.nickname("Snuf").birthDate(LocalDate.of(2004, 2, 24)).sex(Pet.Sex.MALE).type(dog).owner(owner1).save();
        petBuilder.nickname("Rudolf").birthDate(LocalDate.of(2000, 1, 19)).sex(Pet.Sex.HERMAPHRODITIC).type(dog).owner(owner2).save();
        petBuilder.nickname("garfield").birthDate(LocalDate.of(1999, 6, 24)).sex(Pet.Sex.FEMALE).type(cat).owner(owner2).save();
        petBuilder.nickname("jerry").birthDate(LocalDate.of(2016, 1, 1)).sex(Pet.Sex.NEUTRAL).type(mouse).owner(owner2).save();
    }
}
