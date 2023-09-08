package com.sparta.petnexus.user.entity;

import com.sparta.petnexus.common.security.info.ProviderType;
import com.sparta.petnexus.user.pet.entity.Pet;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String username;

    @Column
    private String nickname;

    @Column
    private ProviderType providerType;

    @OneToMany(mappedBy = "user")
    private List<Pet> pet = new ArrayList<>();

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    public User(String email, String username, String nickname, ProviderType providerType,
            UserRoleEnum userRoleEnum) {
        this.email = email;
        this.username = username;
        this.nickname = nickname;
        this.providerType = providerType;
        this.role = userRoleEnum;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updateUsername(String nickname) {
        this.nickname = nickname;
    }
}
