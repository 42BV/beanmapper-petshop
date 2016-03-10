package io.beanmapper.model;

import io.beanmapper.builders.PetTypeBuilder;
import io.beanmapper.result.PetTypeResult;
import org.junit.Test;
import org.springframework.stereotype.Component;

import static org.junit.Assert.assertEquals;

@Component
public class PetTypeTest extends ModelTest {

    private static final Long ID = 42L;
    private static final String TYPE = "Dog";
    private static final String FAMILY_NAME = "Canidae";

    @Test
    public void PetTypeToPetTypeResult() {
        PetTypeResult petTypeResult = beanMapper.map(createPetType(), PetTypeResult.class);
        assertEquals(TYPE, petTypeResult.type);
        assertEquals(FAMILY_NAME, petTypeResult.familyName);
    }

    public static PetType createPetType() {
        return (PetType) new PetTypeBuilder()
                .id(ID)
                .type(TYPE)
                .familyName(FAMILY_NAME)
                .build().entity;
    }
}
