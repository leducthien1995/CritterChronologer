package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.customer.Customer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class PetService {

    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public List<Pet> findAllPet() {
        return petRepository.findAll();
    }

    public Pet findPetById(long id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new PetNotFoundException("Pet can not found with ID: " + id));
    }

    public Pet savePet(Pet pet){
        Pet savedPet = petRepository.save(pet);
        Customer customer = savedPet.getCustomer();
        List<Pet> pets = customer.getPets();
        if (!pets.contains(savedPet)) {
            pets.add(savedPet);
            customer.setPets(pets);
        }
        return savedPet;
    }
}
