package io.beanmapper.controller;

import io.beanmapper.builders.AbstractBuilder;
import io.beanmapper.builders.AddressBuilder;
import io.beanmapper.model.Address;
import io.beanmapper.repository.AddressRepository;
import io.beanmapper.service.AddressService;
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

public class AddressControllerTest extends AbstractControllerTest {

    @Injectable private AddressService addressService;
    @Mocked private AddressRepository addressRepository;
    private AddressBuilder addressBuilder = new AddressBuilder();

    @Before
    public void setup() {
        AddressController addressController = new AddressController();
        initWebClient(addressController);
        Deencapsulation.setField(addressController, "addressService", addressService);
        Deencapsulation.setField(addressController, mockMvcBeanMapper.getBeanMapper());
        registerRepository(addressRepository, Address.class);
    }

    @Test
    public void findAllTest() throws Exception {
        // Data
        AbstractBuilder.Build address1 = addressBuilder.id(1L).street("DogStreet").number(42).city("Gouda").build();
        AbstractBuilder.Build address2 = addressBuilder.id(2L).street("CatStreet").number(24).city("Amsterdam").build();

        // Expected result
        String expectedJsonResponse = objectMapper.writeValueAsString(Arrays.asList(address1.result, address2.result));

        new StrictExpectations() {{
            addressService.findAll();
            result = new ArrayList<>(Arrays.asList(address1.entity, address2.entity));
        }};

        RequestBuilder request = MockMvcRequestBuilders.get("/addresses");
        webClient.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(mvcResult -> Assert.assertEquals(expectedJsonResponse, mvcResult.getResponse().getContentAsString()));
    }
}
