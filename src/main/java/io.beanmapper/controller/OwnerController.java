package io.beanmapper.controller;

import io.beanmapper.BeanMapper;
import io.beanmapper.result.OwnerResult;
import io.beanmapper.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/owners")
public class OwnerController {

    @Autowired private BeanMapper beanMapper;
    @Autowired private OwnerService ownerService;

    @RequestMapping(method = RequestMethod.GET)
    public Collection<OwnerResult> findAll() {
        return beanMapper.map(ownerService.findAll(), OwnerResult.class);
    }
}