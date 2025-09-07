package com.doan.nhansu.users.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "education")
public class EducationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "education_seq")
    @SequenceGenerator(name = "education_seq", sequenceName = "education_sequence", allocationSize = 1)

    @Column(name = "EDUCATION_ID")
    Long id;
    @Column(name = "NAME")
    String name;
    @Column(name = "VALUE")
    String value;
}
