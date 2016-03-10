package io.beanmapper.controller;

import io.beanmapper.builders.AbstractBuilder;
import io.beanmapper.builders.PetTypeBuilder;
import io.beanmapper.model.PetType;
import io.beanmapper.repository.PetTypeRepository;
import io.beanmapper.service.PetTypeService;
import mockit.Deencapsulation;
import mockit.Injectable;
import mockit.Mocked;
import mockit.StrictExpectations;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;

public class PetTypeControllerTest extends AbstractControllerTest {

    @Injectable private PetTypeService petTypeService;
    @Mocked private PetTypeRepository petTypeRepository;
    private PetTypeBuilder petTypeBuilder = new PetTypeBuilder();

    @Before
    public void setup() {
        PetTypeController petTypeController = new PetTypeController();
        initWebClient(petTypeController);
        Deencapsulation.setField(petTypeController, "petTypeService", petTypeService);
        Deencapsulation.setField(petTypeController, mockMvcBeanMapper.getBeanMapper());
        registerRepository(petTypeRepository, PetType.class);
    }

    @Test
    public void findAllTest() throws Exception {
        // Data
        AbstractBuilder.Build petType1 = petTypeBuilder.id(1L).type("Dog").familyName("Canidae").build();
        AbstractBuilder.Build petType2 = petTypeBuilder.id(2L).type("Cat").familyName("Felidae").build();

        // Expected result
        String expectedJsonResponse = objectMapper.writeValueAsString(Arrays.asList(petType1.result, petType2.result));

        new StrictExpectations() {{
            petTypeService.findAll();
            result = new ArrayList<>(Arrays.asList(petType1.entity, petType2.entity));
        }};

        RequestBuilder request = MockMvcRequestBuilders.get("/pet-types");
        webClient.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(mvcResult -> Assert.assertEquals(expectedJsonResponse, mvcResult.getResponse().getContentAsString()));
    }
}
