package com.sh.pettopia.Hojji.pet.dto;

import com.sh.pettopia.Hojji.pet.entity.ParasitePrevention;
import com.sh.pettopia.Hojji.pet.entity.PetGender;
import com.sh.pettopia.Hojji.pet.entity.PetSize;
import com.sh.pettopia.Hojji.pet.entity.VaccinationType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;


@Data
@NotNull
@AllArgsConstructor
@Builder
public class PetRegistResponseDto {
    // 사진
    private String petProfileUrl;

    // 이름
    private String name;

    // 성별
    private PetGender gender;

    // 견종
    private String breed;

    // 몸무게
    private PetSize size;

    // 생일
    private LocalDate birth;

    // 중성화 여부
    private boolean neutered;

    // 예방 접종 여부
    private Set<VaccinationType> vaccinationType;

    // 기생충 예방접종 여부
    private Set<ParasitePrevention> parasitePrevention;

    //  사회성 및 기타 참고사항
    private String socialization;
}