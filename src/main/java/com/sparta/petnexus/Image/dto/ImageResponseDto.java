package com.sparta.petnexus.Image.dto;

import com.sparta.petnexus.Image.entity.Image;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "image 조회 응답 DTO")
public class ImageResponseDto {

    @Schema(description = "image id", example = "1")
    private Long id;

    @Schema(description = "imag URL")
    private String imageUrl;

    public static ImageResponseDto of(Image image) {
        return ImageResponseDto.builder()
                .id(image.getId())
                .imageUrl(image.getImageUrl())
                .build();
    }
}
