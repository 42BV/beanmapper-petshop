package io.beanmapper.controller;

import io.beanmapper.builders.*;
import io.beanmapper.model.Address;
import io.beanmapper.model.Owner;
import io.beanmapper.model.Pet;
import io.beanmapper.repository.OwnerRepository;
import io.beanmapper.service.OwnerService;
import mockit.Deencapsulation;
import mockit.Injectable;
import mockit.Mocked;
import mockit.StrictExpectations;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class OwnerControllerTest extends AbstractControllerTest {

    @Injectable private OwnerService ownerService;
    @Mocked private OwnerRepository ownerRepository;
    private OwnerBuilder ownerBuilder = new OwnerBuilder();
    private AddressBuilder addressBuilder = new AddressBuilder();
    private PetBuilder petBuilder = new PetBuilder();
    private PetTypeBuilder petTypeBuilder = new PetTypeBuilder();

    @Before
    public void setup() {
        OwnerController ownerController = new OwnerController();
        initWebClient(ownerController);
        Deencapsulation.setField(ownerController, ownerService);
        Deencapsulation.setField(ownerController, mockMvcBeanMapper.getBeanMapper());
        registerRepository(ownerRepository, Owner.class);
    }

    @Test
    public void findAllTest() throws Exception {
        // Data
        Address address1 = addressBuilder.id(1L).street("DogStreet").number(42).city("Amsterdam").build().entity;
        Address address2 = addressBuilder.id(1L).street("CatStreet").number(24).city("Gouda").build().entity;
        AbstractBuilder.Build dog = petTypeBuilder.type("Dog").familyName("Canidae").build();
        PetBuilder.PetBuild pet1 = petBuilder.id(1L).nickname("Snuf").birthDate(LocalDate.now()).sex(Pet.Sex.MALE).type(dog).build();
        PetBuilder.PetBuild pet2 = petBuilder.id(2L).nickname("Loebas").birthDate(LocalDate.of(2010,6,6)).sex(Pet.Sex.FEMALE).type(dog).build();
        AbstractBuilder.Build owner1 = ownerBuilder.id(1L).firstName("Henk").prefix("de").lastName("Punt").address(address1).petbuilds(Arrays.asList(pet1, pet2)).build();
        AbstractBuilder.Build owner2 = ownerBuilder.id(2L).firstName("Piet").prefix("").lastName("Boom").address(address2).petbuilds(new ArrayList<>()).build();

        // Expected result
        String expectedJsonResponse = objectMapper.writeValueAsString(Arrays.asList(owner1.result, owner2.result));

        new StrictExpectations() {{
            ownerService.findAll();
            result = new ArrayList<>(Arrays.asList(owner1.entity, owner2.entity));
        }};

        RequestBuilder request = MockMvcRequestBuilders.get("/owners");
        webClient.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(mvcResult -> Assert.assertEquals(expectedJsonResponse, mvcResult.getResponse().getContentAsString()));
    }
}
