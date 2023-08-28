package com.sparta.petnexus.user.pet.entity;

import com.sparta.petnexus.user.dto.AddPetRequest;
import com.sparta.petnexus.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    private String petName;

    @Column
    private PetType petType;

    @Column
    private PetKind petKind;

    @Column
    private PetGender petGender;

    @ManyToOne(fetch =FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    public void update(AddPetRequest request) {
        this.petName = request.getPetName();
        this.petType = request.getPetType();
        this.petKind = request.getPetKind();
        this.petGender = request.getPetGender();
    }
}
