package com.sparta.petnexus.Image.repository;

import com.sparta.petnexus.Image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Boolean existsByImageUrlAndId(String fileName, Long id);
}
