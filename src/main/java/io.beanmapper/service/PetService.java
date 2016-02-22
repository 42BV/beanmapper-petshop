package io.beanmapper.service;

import io.beanmapper.model.Pet;
import io.beanmapper.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class PetService {

    @Autowired private PetRepository petRepository;

    public List<Pet> findAll() {
        return petRepository.findAll();
    }

    public Pet findOne(Long id) {
        return petRepository.findOne(id);
    }

    public List<Pet> findByType(String type) {
        return petRepository.findByTypeTypeIgnoreCase(type);
    }

    public Pet save(Pet pet) {
        return petRepository.save(pet);
    }

    public void delete(Long id) {
        petRepository.delete(id);
    }
}
