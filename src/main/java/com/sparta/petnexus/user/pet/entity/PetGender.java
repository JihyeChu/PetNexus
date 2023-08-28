package com.sparta.petnexus.user.pet.entity;

import lombok.Getter;

@Getter
public enum PetGender {

    MALE("남자"),
    FEMALE("여자");

    private final String gender;

    PetGender(String gender){
        this.gender=gender;
    }

}
