package io.beanmapper.controller;

import io.beanmapper.builders.AbstractBuilder;
import io.beanmapper.builders.AddressBuilder;
import io.beanmapper.form.AddressForm;
import io.beanmapper.model.Address;
import io.beanmapper.repository.AddressRepository;
import io.beanmapper.service.AddressService;
import mockit.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
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
        Deencapsulation.setField(addressController, addressService);
        Deencapsulation.setField(addressController, mockMvcBeanMapper.getBeanMapper());
        Deencapsulation.setField(addressController, objectMapper);
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
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(mvcResult -> Assert.assertEquals(expectedJsonResponse, mvcResult.getResponse().getContentAsString()));
    }

    @Test
    public void findOneTest() throws Exception {
        // Data
        AbstractBuilder.Build address1 = addressBuilder.id(1L).street("DogStreet").number(42).city("Gouda").build();
        // Expected result
        String expectedJsonResponse = objectMapper.writeValueAsString(address1.result);

        new StrictExpectations() {{
           addressService.findOne(1L);
            result = address1.entity;
        }};

        RequestBuilder request = MockMvcRequestBuilders.get("/addresses/1");
        webClient.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(mvcResult -> Assert.assertEquals(expectedJsonResponse, mvcResult.getResponse().getContentAsString()));
    }

    @Test
    public void createTest() throws Exception {
        // Data
        AbstractBuilder.Build address = addressBuilder.id(1L).street("DogStreet").number(42).city("Amsterdam").build();
        // Expected result
        String expectedJsonResponse = objectMapper.writeValueAsString(address.result);

        new Expectations() {{
            addressService.save((Address) any);
            result = address.entity;
        }};

        RequestBuilder request = MockMvcRequestBuilders.post("/addresses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(address.form));

        webClient.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(mvcResult -> Assert.assertEquals(expectedJsonResponse, mvcResult.getResponse().getContentAsString()));

        new Verifications() {{
            AddressForm expectedAddress = (AddressForm) address.form;
            Address captureAddress;
            addressService.save(captureAddress = withCapture());
            Assert.assertNull(captureAddress.getId());
            Assert.assertEquals(expectedAddress.street, captureAddress.getStreet());
            Assert.assertEquals(expectedAddress.number, captureAddress.getNumber());
            Assert.assertEquals(expectedAddress.city, captureAddress.getCity());
        }};
    }

    @Test
    public void updateTest() throws Exception {
        // Data
        AbstractBuilder.Build oldAddress = addressBuilder.id(1L).street("DogStreet").number(42).city("Amsterdam").build();
        AbstractBuilder.Build newAddress = addressBuilder.id(1L).street("CatStreet").number(12).city("Gouda").build();
        // Expected result
        String expectedJsonResponse = objectMapper.writeValueAsString(newAddress.result);

        new Expectations() {{
            addressService.findOne(1L);
            result = oldAddress.entity;

            addressService.save((Address) any);
            result = newAddress.entity;
        }};

        RequestBuilder request = MockMvcRequestBuilders.put("/addresses/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newAddress.form));

        webClient.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(mvcResult -> Assert.assertEquals(expectedJsonResponse, mvcResult.getResponse().getContentAsString()));

        new Verifications() {{
            Address captureAddress;
            addressService.save(captureAddress = withCapture());
            compareEntity((Address) newAddress.entity, captureAddress);
        }};
    }

    @Test
    public void partialUpdateTest() throws Exception {
        // Data
        AbstractBuilder.Build oldAddress = addressBuilder.id(1L).street("DogStreet").number(42).city("Amsterdam").build();
        AbstractBuilder.Build newAddress = addressBuilder.id(1L).street("CatStreet").number(42).city("Amsterdam").build();
        // Expected result
        String form = "{\"street\":\"CatStreet\"}";
        String expectedJsonResponse = objectMapper.writeValueAsString(newAddress.result);

        new Expectations() {{
            addressService.findOne(1L);
            result = oldAddress.entity;

            addressService.save((Address) any);
            result = newAddress.entity;
        }};

        RequestBuilder request = MockMvcRequestBuilders.patch("/addresses/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(form);

        webClient.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(mvcResult -> Assert.assertEquals(expectedJsonResponse, mvcResult.getResponse().getContentAsString()));

        new Verifications() {{
            Address captureAddress;
            addressService.save(captureAddress = withCapture());
            compareEntity((Address) newAddress.entity, captureAddress);
        }};
    }

    @Test
    public void deleteTest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.delete("/addresses/1");
        webClient.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private void compareEntity(Address expectedAddress, Address captureAddress) {
        Assert.assertEquals(expectedAddress.getId(), captureAddress.getId());
        Assert.assertEquals(expectedAddress.getStreet(), captureAddress.getStreet());
        Assert.assertEquals(expectedAddress.getNumber(), captureAddress.getNumber());
        Assert.assertEquals(expectedAddress.getCity(), captureAddress.getCity());
    }
}
