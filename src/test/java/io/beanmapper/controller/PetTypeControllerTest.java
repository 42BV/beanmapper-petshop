package io.beanmapper.controller;

import io.beanmapper.builders.PetTypeBuilder;
import io.beanmapper.model.PetType;
import io.beanmapper.repository.PetTypeRepository;
import io.beanmapper.result.PetTypeResult;
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
import java.util.List;

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
        List<PetType> petTypes = new ArrayList<>();
        petTypes.add(petTypeBuilder.id(1L).type("Dog").familyName("Canidae").build());
        petTypes.add(petTypeBuilder.id(2L).type("Cat").familyName("Felidae").build());

        List<PetTypeResult> results = new ArrayList<>();
        results.add(petTypeBuilder.type("Dog").familyName("Canidae").result());
        results.add(petTypeBuilder.type("Cat").familyName("Felidae").result());
        String expectedJsonResponse = objectMapper.writeValueAsString(results);

        new StrictExpectations() {{
            petTypeService.findAll();
            result = petTypes;
        }};

        RequestBuilder request = MockMvcRequestBuilders.get("/pet-types");
        webClient.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(mvcResult -> Assert.assertEquals(expectedJsonResponse, mvcResult.getResponse().getContentAsString()));
    }
}
