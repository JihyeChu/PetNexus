package com.sparta.petnexus.user.dto;

import com.sparta.petnexus.user.pet.entity.Pet;
import com.sparta.petnexus.user.pet.entity.PetGender;
import com.sparta.petnexus.user.pet.entity.PetKind;
import com.sparta.petnexus.user.pet.entity.PetType;
import com.sparta.petnexus.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "Pet 생성 DTO")
public class AddPetRequest {

    @Schema(description = "Pet 이름")
    private String petName;

    @Schema(description = "Pet 타입")
    private PetType petType;

    @Schema(description = "Pet 품종")
    private PetKind petKind;

    @Schema(description = "Pet 성별")
    private PetGender petGender;


    public Pet toEntity(User user) {
        return Pet.builder()
                .petName(this.petName)
                .petType(this.petType)
                .petKind(this.petKind)
                .petGender(this.petGender)
                .user(user)
                .build();
    }


}
