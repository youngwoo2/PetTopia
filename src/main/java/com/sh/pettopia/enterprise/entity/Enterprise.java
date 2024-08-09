package com.sh.pettopia.enterprise.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_enterprise")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS) // 부모 클래스 생성하지 않고 자식클래스 당 테이블 하나씩만 생성
@Data
@Setter(AccessLevel.PRIVATE) // new로 객체생성해야함
@NoArgsConstructor
@AllArgsConstructor
//@Builder
public abstract class Enterprise {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE) // 상속.TABLE_PER_CLASS이면, IDENTITY 전략은 사용할수 없다.
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "enterprise_seq")
//    @SequenceGenerator(name = "enterprise_seq", sequenceName = "enterprise_seq", allocationSize = 1)
    private Long entId; // 사업자 등록번호 (하지만 우리는 auto_increment)
    @Column(nullable = false)
    private String entName; // 업체명
    @Column(nullable = false)
    private String entPhone; // 전화번호
    @Column(nullable = false)
    private String entAddress; // 주소
    @Column(nullable = false)
    private String officeHours; // 영업시간 09:00-18:00 형식으로 쓰기
}