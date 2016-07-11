package io.beanmapper.controller;

import io.beanmapper.BeanMapper;
import io.beanmapper.result.PetTypeResult;
import io.beanmapper.service.PetTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("/pet-types")
public class PetTypeController {

    @Autowired private BeanMapper beanMapper;
    @Autowired private PetTypeService petTypeService;

    @RequestMapping(method = RequestMethod.GET)
    public Collection<PetTypeResult> findAll() {
        return beanMapper.map(petTypeService.findAll(), PetTypeResult.class);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Object findOne(@PathVariable Long id) {
        return beanMapper.wrapConfig()
                .downsizeTarget(Collections.singletonList("type"))
                .setTargetClass(PetTypeResult.class)
                .build()
                .map(petTypeService.findOne(id));
    }
}