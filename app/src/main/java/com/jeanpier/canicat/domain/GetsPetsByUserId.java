package com.jeanpier.canicat.domain;

import com.jeanpier.canicat.data.PetRepository;
import com.jeanpier.canicat.data.model.Pet;

import java.util.List;
import java.util.UUID;

public class GetsPetsByUserId {
    private PetRepository petRepository;
    private UUID userId;

    public GetsPetsByUserId(PetRepository petRepository, UUID userId) {
        this.petRepository = petRepository;
        this.userId = userId;
    }

    public List<Pet> execute() {
        return petRepository.getPetsByUserId(userId);
    }
}
