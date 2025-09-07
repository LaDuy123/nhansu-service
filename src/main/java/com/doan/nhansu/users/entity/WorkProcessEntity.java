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
@Table(name = "workprocess")
public class WorkProcessEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "work_seq")
    @SequenceGenerator(name = "work_seq", sequenceName = "work_sequence", allocationSize = 1)
    @Column(name = "WORKPROCESS_ID")
    Long id;
    @Column(name = "USER_ID")
    String userId;
    @Column(name = "VALUE")
    String value;
    @Column(name = "START_DATE")
    Date startDate;
    @Column(name = "END_DATE")
    Date endDate;
    @Column(name = "DEPARTMENT_ID")
    String departmentId;
    @Column(name = "POSITION_ID")
    String positionId;
    @Column(name = "CREATED")
    Date created;
    @Column(name = "CREATEDBY")
    Long createdBy;
    @Column(name = "UPDATED")
    Date updated;
    @Column(name = "UPDATEDBY")
    Long updatedBy;
}
