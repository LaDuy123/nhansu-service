package com.doan.nhansu.timekeep.entity;

import com.doan.nhansu.users.entity.DepartmentEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "shift")
public class ShiftEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shift_seq")
    @SequenceGenerator(name = "shift_seq", sequenceName = "shift_sequence", allocationSize = 1)
    @Column(name = "SHIFT_ID")
    Long id;
    @Column(name = "START_DATE")
    Date startDate;
    @Column(name = "VALUE")
    String value;
    @Column(name = "NAME")
    String name;
    @Column(name = "END_DATE")
    Date endDate;
    @Column(name = "DEPARTMENT_ID")
    Long departmentId;
    @Column(name = "CREATED")
    Date created;
    @Column(name = "CREATEDBY")
    Long createdBy;
    @Column(name = "UPDATED")
    Date updated;
    @Column(name = "UPDATEDBY")
    Long updatedBy;
}
