package com.doan.nhansu.users.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "contract")
public class ContractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contract_seq")
    @SequenceGenerator(name = "contract_seq", sequenceName = "contract_sequence", allocationSize = 1)
    @Column(name = "CONTRACT_ID")
    Long id;
    @Column(name = "SIGNDATE")
    Date signdate;
    @Column(name = "EFFECTIVEDATE")
    Date effectiveDate;
    @Column(name = "EXPIRATIONDATE")
    Date expirationDate;
    @Column(name = "DURATION")
    String duration;
    @Column(name = "WORKING_TIME")
    String workingTime;
    @Column(name = "WORKING_TIME_MORNING")
    String workingTimeMorning;
    @Column(name = "WORKING_TIME_AFTERNOON")
    String workingTimeAfternoon;
    @Column(name = "CONTRACT_TYPE_ID")
    Long contractTypeId;
    @Column(name = "SIGNING_TIME")
    Long signingTime;
    @Column(name = "SALARY")
    BigDecimal salary;
    @Column(name = "ALLOWANCE")
    BigDecimal allowance;
    @Column(name = "COEFFICIENT")
    BigDecimal coefficient;
    @Column(name = "CURRENT_POSITION")
    Long currentPosition;
    @Column(name = "NEW_POSITION")
    Long newPosition;
    @Column(name = "CURRENT_DEPARTMENT")
    Long currentDepartment;
    @Column(name = "NEW_DEPARTMENT")
    Long newDepartment;
    @Column(name = "WORK_PLACE")
    String workPlace;
    @Column(name = "USER_ID")
    Long userId;
    @Column(name = "USER_MANAGER_ID")
    Long userManagerId;
    @Column(name = "CONTRACT_NUMBER")
    String contractNumber;
    @Column(name = "ISSIGNED")
    String isSigned;
    @Column(name = "CREATED")
    Date created;
    @Column(name = "CREATEDBY")
    Long createdBy;
    @Column(name = "UPDATED")
    Date updated;
    @Column(name = "UPDATEDBY")
    Long updatedBy;



}
