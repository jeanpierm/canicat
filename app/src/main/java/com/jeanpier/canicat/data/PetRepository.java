package com.jeanpier.canicat.data;

import com.jeanpier.canicat.data.model.Pet;
import com.jeanpier.canicat.data.network.PetService;

import java.util.List;
import java.util.UUID;

public class PetRepository {
    private final PetService petService;

    public PetRepository(PetService petService) {
        this.petService = petService;
    }

    public List<Pet> getPetsByUserId(UUID userId) {
        return petService.getPetsByUserId(userId);
    }
}
