package io.beanmapper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.beanmapper.BeanMapper;
import io.beanmapper.form.PetForm;
import io.beanmapper.model.Pet;
import io.beanmapper.result.PetNameAndAgeResult;
import io.beanmapper.result.PetResult;
import io.beanmapper.service.PetService;
import io.beanmapper.support.AgeCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Controller to show create, read, update & delete use cases for BeanMapper.
 * @see PetController for same use cases but using BeanMapper together with BeanMapper-Spring module.
 */
@RestController
@RequestMapping("/pets-simple")
public class SimplePetController {

    @Autowired private BeanMapper beanMapper;
    @Autowired private PetService petService;
    @Autowired private ObjectMapper objectMapper;

    @RequestMapping(method = RequestMethod.GET)
    public Collection<PetNameAndAgeResult> findAll() {
        return beanMapper.wrapConfig()
                .addConverter(new AgeCalculator())
                .build()
                .map(petService.findAll(), PetNameAndAgeResult.class);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PetResult findOne(@PathVariable Long id) {
        return convert(petService.findOne(id));
    }

    @RequestMapping(value = "/type/{name}", method = RequestMethod.GET)
    public Collection<PetResult> findByType(@PathVariable String name) {
        return beanMapper.map(petService.findByType(name), PetResult.class);
    }

    @RequestMapping(method = RequestMethod.POST)
    public PetResult create(@RequestBody PetForm petForm) {
        Pet newPet = beanMapper.map(petForm, Pet.class);
        return convert(petService.save(newPet));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public PetResult update(@PathVariable Long id, @RequestBody PetForm petForm) {
        Pet oldPet = petService.findOne(id);
        Pet updatedPet = beanMapper.map(petForm, oldPet);
        return convert(petService.save(updatedPet));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public PetResult partialUpdate(@PathVariable Long id, @RequestBody Map<String, Object> inputFields) {
        PetForm petForm = objectMapper.convertValue(inputFields, PetForm.class);
        Pet oldPet = petService.findOne(id);
        Pet updatedPet = beanMapper.wrapConfig()
                .downsizeSource(new ArrayList<>(inputFields.keySet()))
                .build()
                .map(petForm, oldPet);

        return convert(petService.save(updatedPet));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id) {
        petService.delete(id);
    }

    private PetResult convert(Pet pet) {
        return beanMapper.map(pet, PetResult.class);
    }
}
