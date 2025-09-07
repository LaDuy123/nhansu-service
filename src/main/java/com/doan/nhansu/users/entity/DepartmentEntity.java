package com.doan.nhansu.users.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "department")
public class DepartmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "department_seq")
    @SequenceGenerator(name = "department_seq", sequenceName = "department_sequence", allocationSize = 1)
    @Column(name = "DEPARTMENT_ID")
    Long id;
    @Column(name = "NAME")
    String name;
    @Column(name = "VALUE")
    String value;
    @Column(name = "DEPARTMENT_LEVEL")
    Long departmentLevel;
    @Column(name = "PATH")
    String path;
    @Column(name = "PARENT_ID")
    Long parentId;
    @Column(name = "CREATED")
    Date created;
    @Column(name = "CREATEDBY")
    Long createdBy;
    @Column(name = "UPDATED")
    Date updated;
    @Column(name = "UPDATEDBY")
    Long updatedBy;


}
