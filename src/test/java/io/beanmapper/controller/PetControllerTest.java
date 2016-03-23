package io.beanmapper.controller;

import io.beanmapper.builders.*;
import io.beanmapper.form.PetForm;
import io.beanmapper.model.Address;
import io.beanmapper.model.Owner;
import io.beanmapper.model.Pet;
import io.beanmapper.model.PetType;
import io.beanmapper.repository.PetRepository;
import io.beanmapper.repository.PetTypeRepository;
import io.beanmapper.service.PetService;
import mockit.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class PetControllerTest extends AbstractControllerTest {

    @Injectable private PetService petService;
    @Mocked private PetRepository petRepository;
    @Mocked private PetTypeRepository petTypeRepository;
    private PetBuilder petBuilder = new PetBuilder();
    private PetTypeBuilder petTypeBuilder = new PetTypeBuilder();
    private OwnerBuilder ownerBuilder = new OwnerBuilder();
    private AddressBuilder addressBuilder = new AddressBuilder();

    @Before
    public void setup() {
        PetController petController = new PetController();
        initWebClient(petController);
        Deencapsulation.setField(petController, petService);
        Deencapsulation.setField(petController, mockMvcBeanMapper.getBeanMapper());
        registerRepository(petRepository, Pet.class);
        registerRepository(petRepository, PetType.class);
    }

    @Test
    public void findAllTest() throws Exception {
        // Data
        PetBuilder.PetBuild pet1 = createPet("Snuf", LocalDate.now(), Pet.Sex.MALE);
        PetBuilder.PetBuild pet2 = createPet("Loebas", LocalDate.of(2010,6,6), Pet.Sex.FEMALE);

        // Expected result
        String expectedJsonResponse = objectMapper.writeValueAsString(Arrays.asList(pet1.petNameAndAgeResult, pet2.petNameAndAgeResult));

        new StrictExpectations() {{
            petService.findAll();
            result = new ArrayList<>(Arrays.asList(pet1.entity, pet2.entity));
        }};

        RequestBuilder request = MockMvcRequestBuilders.get("/pets");
        webClient.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(mvcResult -> Assert.assertEquals(expectedJsonResponse, mvcResult.getResponse().getContentAsString()));
    }

    @Test
    public void findOneTest() throws Exception {
        // Data
        PetBuilder.PetBuild pet = createPet("Snuf", LocalDate.now(), Pet.Sex.MALE);
        // Expected result
        String expectedJsonResponse = objectMapper.writeValueAsString(pet.result);

        new StrictExpectations() {{
            petService.findOne(1L);
            result = pet.entity;
        }};

        RequestBuilder request = MockMvcRequestBuilders.get("/pets/1");
        webClient.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(mvcResult -> Assert.assertEquals(expectedJsonResponse, mvcResult.getResponse().getContentAsString()));
    }

    @Test
    public void findByTypeTest() throws Exception {
        // Data
        PetBuilder.PetBuild pet = createPet("Snuf", LocalDate.now(), Pet.Sex.MALE);
        // Expected result
        String expectedJsonResponse = objectMapper.writeValueAsString(Collections.singletonList(pet.result));

        new StrictExpectations() {{
            petService.findByType("dog");
            result = new ArrayList<>(Collections.singletonList(pet.entity));
        }};

        RequestBuilder request = MockMvcRequestBuilders.get("/pets/type/dog");
        webClient.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(mvcResult -> Assert.assertEquals(expectedJsonResponse, mvcResult.getResponse().getContentAsString()));
    }

    @Test
    public void createTest() throws Exception {
        // Data
        PetBuilder.PetBuild pet = createPet("Snuf", LocalDate.now(), Pet.Sex.MALE);
        // Expected result
        String expectedJsonResponse = objectMapper.writeValueAsString(pet.result);

        new Expectations() {{
            petService.save((Pet) any);
            result = pet.entity;
        }};

        RequestBuilder request = MockMvcRequestBuilders.post("/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pet.form));

        webClient.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(mvcResult -> Assert.assertEquals(expectedJsonResponse, mvcResult.getResponse().getContentAsString()));

        new Verifications() {{
            PetForm expectedPet = pet.form;
            Pet capturePet;
            petService.save(capturePet = withCapture());
            Assert.assertNull(capturePet.getId());
            Assert.assertEquals(expectedPet.nickname, capturePet.getNickname());
            Assert.assertEquals(expectedPet.birthDate, capturePet.getBirthDate());
            Assert.assertEquals(expectedPet.sex, capturePet.getSex());
            Assert.assertEquals(expectedPet.petTypeId, capturePet.getType().getId());
            Assert.assertNull(capturePet.getOwner());
        }};
    }

    @Test
    public void updateTest() throws Exception {
        // Data
        PetBuilder.PetBuild oldPet = createPet("Snuf", LocalDate.now(), Pet.Sex.MALE);
        PetBuilder.PetBuild newPet = createPet("Kees", LocalDate.of(2010,6,6), Pet.Sex.FEMALE);
        // Expected result
        String expectedJsonResponse = objectMapper.writeValueAsString(newPet.result);

        new Expectations() {{
            petRepository.findOne(1L);
            result = oldPet.entity;

            petService.save((Pet) any);
            result = newPet.entity;
        }};

        RequestBuilder request = MockMvcRequestBuilders.put("/pets/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newPet.form));

        webClient.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(mvcResult -> Assert.assertEquals(expectedJsonResponse, mvcResult.getResponse().getContentAsString()));

        new Verifications() {{
            Pet capturePet;
            petService.save(capturePet = withCapture());
            compareEntity(newPet.entity, capturePet);
        }};
    }

    @Test
    public void partialUpdateTest() throws Exception {
        // Data
        PetBuilder.PetBuild oldPet = createPet("Snuf", LocalDate.of(2010,6,6), Pet.Sex.MALE);
        PetBuilder.PetBuild newPet = createPet("Kees", LocalDate.of(2010,6,6), Pet.Sex.MALE);
        // Expected result
        String form = "{\"nickname\":\"Kees\"}";
        String expectedJsonResponse = objectMapper.writeValueAsString(newPet.result);

        new Expectations() {{
            petRepository.findOne(1L);
            result = oldPet.entity;

            petService.save((Pet) any);
            result = newPet.entity;
        }};

        RequestBuilder request = MockMvcRequestBuilders.patch("/pets/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(form);

        webClient.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(mvcResult -> Assert.assertEquals(expectedJsonResponse, mvcResult.getResponse().getContentAsString()));

        new Verifications() {{
            Pet capturePet;
            petService.save(capturePet = withCapture());
            compareEntity(newPet.entity, capturePet);
        }};
    }

    @Test
    public void deleteTest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.delete("/pets/1");
        webClient.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private void compareEntity(Pet expectedPet, Pet capturePet) {
        Assert.assertEquals(expectedPet.getId(), capturePet.getId());
        Assert.assertEquals(expectedPet.getNickname(), capturePet.getNickname());
        Assert.assertEquals(expectedPet.getBirthDate(), capturePet.getBirthDate());
        Assert.assertEquals(expectedPet.getSex(), capturePet.getSex());
        // Type
        Assert.assertEquals(expectedPet.getType().getId(), capturePet.getType().getId());
        Assert.assertEquals(expectedPet.getType().getType(), capturePet.getType().getType());
        Assert.assertEquals(expectedPet.getType().getFamilyName(), capturePet.getType().getFamilyName());
        // Owner
        Assert.assertEquals(expectedPet.getOwner().getId(), capturePet.getOwner().getId());
        Assert.assertEquals(expectedPet.getOwner().getFirstName(), capturePet.getOwner().getFirstName());
        Assert.assertEquals(expectedPet.getOwner().getPrefix(), capturePet.getOwner().getPrefix());
        Assert.assertEquals(expectedPet.getOwner().getLastName(), capturePet.getOwner().getLastName());
    }

    private PetBuilder.PetBuild createPet(String nickname, LocalDate birthDate, Pet.Sex sex) {
        Address address = addressBuilder.id(1L).street("DogStreet").number(42).city("Amsterdam").build().entity;
        Owner owner = ownerBuilder.id(1L).firstName("Henk").prefix("van").lastName("Boom").address(address).build().entity;
        AbstractBuilder.Build dog = petTypeBuilder.id(1L).type("Dog").familyName("Canidae").build();
        return petBuilder.id(1L).nickname(nickname).birthDate(birthDate).sex(sex).type(dog).owner(owner).build();
    }
}
