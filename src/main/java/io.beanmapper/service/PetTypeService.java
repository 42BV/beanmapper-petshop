package io.beanmapper.service;

import io.beanmapper.model.PetType;
import io.beanmapper.repository.PetTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetTypeService {

    @Autowired private PetTypeRepository petTypeRepository;

    public List<PetType> findAll() {
        return petTypeRepository.findAll();
    }

    public PetType findOne(Long id) {
        return petTypeRepository.findOne(id);
    }
}
