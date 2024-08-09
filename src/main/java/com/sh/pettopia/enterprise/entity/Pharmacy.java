package com.sh.pettopia.enterprise.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@EqualsAndHashCode(callSuper = true) // callSuper = true를 설정하면, 상위 클래스에 정의된 필드도 equals와 hashCode 메소드의 비교와 해시 계산에 포함됩
@Entity
@Table(name = "tbl_pharmacy")
@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class Pharmacy extends Enterprise {
    // 고유 pk없음

    public Pharmacy(Long entId, String entName, String entPhone, String entAddress, String officeHours) {
        super(entId, entName, entPhone, entAddress, officeHours);
    }
}