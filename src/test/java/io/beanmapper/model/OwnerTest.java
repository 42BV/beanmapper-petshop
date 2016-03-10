package io.beanmapper.model;

import io.beanmapper.builders.OwnerBuilder;
import io.beanmapper.result.OwnerResult;
import org.junit.Test;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@Component
public class OwnerTest extends ModelTest {

    private static final Long ID = 42L;
    private static final String FIRST_NAME = "Henk";
    private static final String PREFIX = "de";
    private static final String LAST_NAME = "Leeuw";
    private static final Address ADDRESS = AddressTest.createAddress();
    private static final Set<Pet> PETS = new HashSet<>(
            Arrays.asList(
                    PetTest.createPet(),
                    PetTest.createPet()));

    @Test
    public void ownerToOwnerResult() {
        OwnerResult ownerResult = beanMapper.map(createOwner(), OwnerResult.class);
        assertEquals(FIRST_NAME +" "+ PREFIX +" "+ LAST_NAME, ownerResult.fullName);
        assertEquals(ADDRESS.getCity(), ownerResult.livingPlace);
        assertEquals(PETS.size(), ownerResult.pets.size());
        assertEquals(PetTest.NICKNAME, ownerResult.pets.get(0).name);
        assertEquals(PetTest.NICKNAME, ownerResult.pets.get(1).name);
    }

    public static Owner createOwner() {
        return (Owner) new OwnerBuilder()
                .id(ID)
                .firstName(FIRST_NAME)
                .prefix(PREFIX)
                .lastName(LAST_NAME)
                .address(ADDRESS)
                .pets(PETS)
                .build().entity;
    }
}
