package com.sparta.petnexus.user.pet.repository;

import com.sparta.petnexus.user.pet.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {

}
