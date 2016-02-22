package io.beanmapper.service;

import io.beanmapper.model.Owner;
import io.beanmapper.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OwnerService {

    @Autowired private OwnerRepository ownerRepository;

    public List<Owner> findAll() {
        return ownerRepository.findAll();
    }
}
