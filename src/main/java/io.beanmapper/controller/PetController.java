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

@RestController
@RequestMapping("/pets")
public class PetController {

    @Autowired private BeanMapper beanMapper;
    @Autowired private PetService petService;

    @RequestMapping(method = RequestMethod.GET)
    public Collection<PetNameAndAgeResult> findAll() {
        // TODO Wrap BeanMapper config instead of create a new one
        BeanMapper beanMapper = new BeanMapperBuilder().addConverter(new AgeCalculator()).build();
        return beanMapper.map(petService.findAll(), PetNameAndAgeResult.class);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PetResult findOne(@PathVariable Long id) {
        return beanMapper.map(petService.findOne(id), PetResult.class);
    }

    @RequestMapping(value = "/type/{name}", method = RequestMethod.GET)
    public Collection<PetResult> findByType(@PathVariable String name) {
        return beanMapper.map(petService.findByType(name), PetResult.class);
    }

    @RequestMapping(method = RequestMethod.POST)
    public PetResult create(@MergedForm(value = PetForm.class) Pet pet) {
        return beanMapper.map(petService.save(pet), PetResult.class);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public PetResult update(@MergedForm(value = PetForm.class, mergeId = "id") Pet pet) {
        return beanMapper.map(petService.save(pet), PetResult.class);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public PetResult partialUpdate(@MergedForm(value = PetForm.class, patch = true, mergeId = "id") Pet pet) {
        return beanMapper.map(petService.save(pet), PetResult.class);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id) {
        petService.delete(id);
    }
}
