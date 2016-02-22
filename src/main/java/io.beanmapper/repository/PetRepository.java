package io.beanmapper.repository;

import io.beanmapper.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    List<Pet> findByTypeTypeIgnoreCase(String type);
}
