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
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

@RunWith(Theories.class)
public class PetControllerTest extends AbstractControllerTest {

    @DataPoint public static String PetControllerPath = "/pets";
    @DataPoint public static String SimplePetControllerPath = "/pets-simple";
    @Injectable private PetService petService;
    @Mocked private PetRepository petRepository;
    @Mocked private PetTypeRepository petTypeRepository;
    private PetBuilder petBuilder = new PetBuilder();
    private PetTypeBuilder petTypeBuilder = new PetTypeBuilder();
    private OwnerBuilder ownerBuilder = new OwnerBuilder();
    private AddressBuilder addressBuilder = new AddressBuilder();

    public void setup(String path) {
        Object controller;
        if(path.equals(PetControllerPath)) {
            controller = new PetController();
        } else {
            controller = new SimplePetController();
        }
        initWebClient(controller);
        Deencapsulation.setField(controller, petService);
        Deencapsulation.setField(controller, mockMvcBeanMapper.getBeanMapper());
        if(controller instanceof SimplePetController)
            Deencapsulation.setField(controller, objectMapper);
        registerRepository(petRepository, Pet.class);
        registerRepository(petRepository, PetType.class);
    }

    @Test
    @Theory
    public void findAllTest(String path) throws Exception {
        setup(path);
        // Data
        PetBuilder.PetBuild pet1 = createPet("Snuf", LocalDate.now(), Pet.Sex.MALE);
        PetBuilder.PetBuild pet2 = createPet("Loebas", LocalDate.of(2010,6,6), Pet.Sex.FEMALE);

        // Expected result
        String expectedJsonResponse = objectMapper.writeValueAsString(Arrays.asList(pet1.petNameAndAgeResult, pet2.petNameAndAgeResult));

        new StrictExpectations() {{
            petService.findAll();
            result = new ArrayList<>(Arrays.asList(pet1.entity, pet2.entity));
        }};

        RequestBuilder request = MockMvcRequestBuilders.get(path);
        webClient.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(mvcResult -> assertEquals(expectedJsonResponse, mvcResult.getResponse().getContentAsString()));
    }

    @Test
    @Theory
    public void findOneTest(String path) throws Exception {
        setup(path);
        // Data
        PetBuilder.PetBuild pet = createPet("Snuf", LocalDate.now(), Pet.Sex.MALE);
        // Expected result
        String expectedJsonResponse = objectMapper.writeValueAsString(pet.result);

        new StrictExpectations() {{
            petService.findOne(1L);
            result = pet.entity;
        }};

        RequestBuilder request = MockMvcRequestBuilders.get(path+"/1");
        webClient.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(mvcResult -> assertEquals(expectedJsonResponse, mvcResult.getResponse().getContentAsString()));
    }

    @Test
    @Theory
    public void findByTypeTest(String path) throws Exception {
        setup(path);
        // Data
        PetBuilder.PetBuild pet = createPet("Snuf", LocalDate.now(), Pet.Sex.MALE);
        // Expected result
        String expectedJsonResponse = objectMapper.writeValueAsString(Collections.singletonList(pet.result));

        new StrictExpectations() {{
            petService.findByType("dog");
            result = new ArrayList<>(Collections.singletonList(pet.entity));
        }};

        RequestBuilder request = MockMvcRequestBuilders.get(path+"/type/dog");
        webClient.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(mvcResult -> assertEquals(expectedJsonResponse, mvcResult.getResponse().getContentAsString()));
    }

    @Test
    @Theory
    public void createTest(String path) throws Exception {
        setup(path);
        // Data
        PetBuilder.PetBuild pet = createPet("Snuf", LocalDate.now(), Pet.Sex.MALE);
        // Expected result
        String expectedJsonResponse = objectMapper.writeValueAsString(pet.result);

        new Expectations() {{
            petService.save((Pet) any);
            result = pet.entity;
        }};

        RequestBuilder request = MockMvcRequestBuilders.post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pet.form));

        webClient.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(mvcResult -> assertEquals(expectedJsonResponse, mvcResult.getResponse().getContentAsString()));

        new Verifications() {{
            PetForm expectedPet = pet.form;
            Pet capturePet;
            petService.save(capturePet = withCapture());
            Assert.assertNull(capturePet.getId());
            assertEquals(expectedPet.nickname, capturePet.getNickname());
            assertEquals(expectedPet.birthDate, capturePet.getBirthDate());
            assertEquals(expectedPet.sex, capturePet.getSex());
            assertEquals(expectedPet.petTypeId, capturePet.getType().getId());
            Assert.assertNull(capturePet.getOwner());
        }};
    }

    @Test
    @Theory
    public void updateTest(String path) throws Exception {
        setup(path);
        // Data
        PetBuilder.PetBuild oldPet = createPet("Snuf", LocalDate.now(), Pet.Sex.MALE);
        PetBuilder.PetBuild newPet = createPet("Kees", LocalDate.of(2010,6,6), Pet.Sex.FEMALE);
        // Expected result
        String expectedJsonResponse = objectMapper.writeValueAsString(newPet.result);

        if(path.equals(PetControllerPath)) {
            new Expectations() {{
                petRepository.findOne(1L);
                result = oldPet.entity;

                petService.save((Pet) any);
                result = newPet.entity;
            }};
        } else {
            new Expectations() {{
                petService.findOne(1L);
                result = oldPet.entity;

                petService.save((Pet) any);
                result = newPet.entity;
            }};
        }

        RequestBuilder request = MockMvcRequestBuilders.put(path+"/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newPet.form));

        webClient.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(mvcResult -> assertEquals(expectedJsonResponse, mvcResult.getResponse().getContentAsString()));

        new Verifications() {{
            Pet capturePet;
            petService.save(capturePet = withCapture());
            compareEntity(newPet.entity, capturePet);
        }};
    }

    @Test
    @Theory
    public void partialUpdateTest(String path) throws Exception {
        setup(path);
        // Data
        PetBuilder.PetBuild oldPet = createPet("Snuf", LocalDate.of(2010,6,6), Pet.Sex.MALE);
        PetBuilder.PetBuild newPet = createPet("Kees", LocalDate.of(2010,6,6), Pet.Sex.MALE);
        // Expected result
        String form = "{\"nickname\":\"Kees\"}";
        String expectedJsonResponse = objectMapper.writeValueAsString(newPet.result);

        if(path.equals(PetControllerPath)) {
            new Expectations() {{
                petRepository.findOne(1L);
                result = oldPet.entity;

                petService.save((Pet) any);
                result = newPet.entity;
            }};
        } else {
            new Expectations() {{
                petService.findOne(1L);
                result = oldPet.entity;

                petService.save((Pet) any);
                result = newPet.entity;
            }};
        }

        RequestBuilder request = MockMvcRequestBuilders.patch(path+"/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(form);

        webClient.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(mvcResult -> assertEquals(expectedJsonResponse, mvcResult.getResponse().getContentAsString()));

        new Verifications() {{
            Pet capturePet;
            petService.save(capturePet = withCapture());
            compareEntity(newPet.entity, capturePet);
        }};
    }

    @Test
    @Theory
    public void deleteTest(String path) throws Exception {
        setup(path);
        RequestBuilder request = MockMvcRequestBuilders.delete(path+"/1");
        webClient.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void idToPetTest() {
        setup(PetControllerPath);
        Long id = 1L;
        Pet pet = new Pet();
        pet.setNickname("Snuf");
        pet.setBirthDate(LocalDate.of(2016,1,1));

        new Expectations() {{
            petRepository.findOne(id);
            result = pet;
        }};


        Pet petFromId = mockMvcBeanMapper.getBeanMapper().map(id, Pet.class, true);
        assertEquals("Snuf", petFromId.getNickname());
        assertEquals(LocalDate.of(2016,1,1), petFromId.getBirthDate());
    }

    private void compareEntity(Pet expectedPet, Pet capturePet) {
        assertEquals(expectedPet.getId(), capturePet.getId());
        assertEquals(expectedPet.getNickname(), capturePet.getNickname());
        assertEquals(expectedPet.getBirthDate(), capturePet.getBirthDate());
        assertEquals(expectedPet.getSex(), capturePet.getSex());
        // Type
        assertEquals(expectedPet.getType().getId(), capturePet.getType().getId());
        assertEquals(expectedPet.getType().getType(), capturePet.getType().getType());
        assertEquals(expectedPet.getType().getFamilyName(), capturePet.getType().getFamilyName());
        // Owner
        assertEquals(expectedPet.getOwner().getId(), capturePet.getOwner().getId());
        assertEquals(expectedPet.getOwner().getFirstName(), capturePet.getOwner().getFirstName());
        assertEquals(expectedPet.getOwner().getPrefix(), capturePet.getOwner().getPrefix());
        assertEquals(expectedPet.getOwner().getLastName(), capturePet.getOwner().getLastName());
    }

    private PetBuilder.PetBuild createPet(String nickname, LocalDate birthDate, Pet.Sex sex) {
        Address address = addressBuilder.id(1L).street("DogStreet").number(42).city("Amsterdam").build().entity;
        Owner owner = ownerBuilder.id(1L).firstName("Henk").prefix("van").lastName("Boom").address(address).build().entity;
        AbstractBuilder.Build dog = petTypeBuilder.id(1L).type("Dog").familyName("Canidae").build();
        return petBuilder.id(1L).nickname(nickname).birthDate(birthDate).sex(sex).type(dog).owner(owner).build();
    }
}
