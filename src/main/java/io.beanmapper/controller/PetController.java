package io.beanmapper.controller;

import io.beanmapper.BeanMapper;
import io.beanmapper.config.BeanMapperBuilder;
import io.beanmapper.form.PetForm;
import io.beanmapper.model.Pet;
import io.beanmapper.result.PetNameAndAgeResult;
import io.beanmapper.result.PetResult;
import io.beanmapper.service.PetService;
import io.beanmapper.spring.web.MergedForm;
import io.beanmapper.support.AgeCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * Controller to show create, read, update & delete use cases for BeanMapper.
 * This controller uses BeanMapper-Spring module for making create & update a lot easier.
 * @see AddressController for same use cases but without BeanMapper-Spring module.
 */
@RestController
@RequestMapping("/pets")
public class PetController {

    @Autowired private BeanMapper beanMapper;
    @Autowired private PetService petService;

    @RequestMapping(method = RequestMethod.GET)
    public Collection<PetNameAndAgeResult> findAll() {
        // TODO use BeanMapper builder to wrap config
//        return beanMapper.wrapConfig()
//                .addConverter(new AgeCalculator())
//                .build()
//                .map(petService.findAll(), PetNameAndAgeResult.class);

        BeanMapper beanMapper = new BeanMapperBuilder().addConverter(new AgeCalculator()).build();
        return beanMapper.map(petService.findAll(), PetNameAndAgeResult.class);
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
    public PetResult create(@MergedForm(value = PetForm.class) Pet pet) {
        return convert(petService.save(pet));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public PetResult update(@MergedForm(value = PetForm.class, mergeId = "id") Pet pet) {
        return convert(petService.save(pet));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public PetResult partialUpdate(@MergedForm(value = PetForm.class, patch = true, mergeId = "id") Pet pet) {
        return convert(petService.save(pet));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id) {
        petService.delete(id);
    }

    private PetResult convert(Pet pet) {
        return beanMapper.map(pet, PetResult.class);
    }
}
