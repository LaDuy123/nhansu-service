package com.doan.nhansu.admin.entity;

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
@Table(name = "roles_departments")
public class RoleDepartmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_department_seq")
    @SequenceGenerator(name = "role_department_seq", sequenceName = "role_department_sequence", allocationSize = 1)
    @Column(name = "ROLE_DEPARTMENT_ID")
    Long id;
    @Column(name = "ROLE_ID")
    Long roleId;
    @Column(name = "DEPARTMENT_ID")
    Long departmentId;
    @Column(name = "DESCRIPTION")
    String description;
    @Column(name = "CREATED")
    Date created;
    @Column(name = "CREATEDBY")
    Long createdBy;
    @Column(name = "UPDATED")
    Date updated;
    @Column(name = "UPDATEDBY")
    Long updatedBy;
}
