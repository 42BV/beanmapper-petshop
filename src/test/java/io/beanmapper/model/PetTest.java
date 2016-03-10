package io.beanmapper.model;

import io.beanmapper.builders.PetBuilder;
import io.beanmapper.config.BeanMapperBuilder;
import io.beanmapper.form.PetForm;
import io.beanmapper.result.PetNameAndAgeResult;
import io.beanmapper.result.PetNameResult;
import io.beanmapper.result.PetResult;
import io.beanmapper.support.AgeCalculator;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class PetTest extends ModelTest {

    private static final Long ID = 42L;
    public static final String NICKNAME = "Loebas";
    private static final LocalDate BIRTH_DATE = LocalDate.of(2000, 1, 5);
    private static final Pet.Sex SEX = Pet.Sex.MALE;
    private static final PetType TYPE = PetTypeTest.createPetType();
    private static final Owner OWNER = OwnerTest.createOwner();

    @Test
    public void PetToPetResult() {
        PetResult petResult = beanMapper.map(createPet(), PetResult.class);
        assertEquals(NICKNAME, petResult.nickname);
        assertEquals(BIRTH_DATE, petResult.birthDate);
        assertEquals(SEX.toString(), petResult.sex);
        assertEquals(TYPE.getType(), petResult.type.type);
        assertEquals(TYPE.getFamilyName(), petResult.type.familyName);
    }

    @Test
    public void PetToPetNameResult() {
        PetNameResult petNameResult = beanMapper.map(createPet(), PetNameResult.class);
        assertEquals(NICKNAME, petNameResult.name);
    }

    @Test
    public void PetToPetNameAndAgeResult() {
        AgeCalculator ageCalculator = new AgeCalculator();
        PetNameAndAgeResult petNameAndAgeResult = new BeanMapperBuilder()
                .addConverter(ageCalculator)
                .build()
                .map(createPet(), PetNameAndAgeResult.class);

        assertEquals(NICKNAME, petNameAndAgeResult.nickname);
        assertEquals(ageCalculator.doConvert(BIRTH_DATE), petNameAndAgeResult.age);
    }

    @Test
    public void PetFormToPet() {
        PetForm petForm = new PetForm();
        petForm.nickname = NICKNAME;
        petForm.birthDate = BIRTH_DATE;
        petForm.sex = SEX;
        petForm.petTypeId = TYPE.getId();

        Pet pet = beanMapper.map(petForm, Pet.class);
        assertEquals(NICKNAME, pet.getNickname());
        assertEquals(BIRTH_DATE, pet.getBirthDate());
        assertEquals(SEX, pet.getSex());
        assertEquals(TYPE.getId(), pet.getType().getId());
    }

    public static Pet createPet() {
        return new PetBuilder()
                .id(ID)
                .nickname(NICKNAME)
                .birthDate(BIRTH_DATE)
                .sex(SEX)
                .type(TYPE)
                .owner(OWNER)
                .build();
    }
}
