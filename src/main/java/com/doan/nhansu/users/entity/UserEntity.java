package com.doan.nhansu.users.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "Users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_sequence", allocationSize = 1)
    @Column(name = "USER_ID")
    Long id;
    @Column(name = "ROLE_ID")
    Long roleId;
    @Column(name = "VALUE")
    String value;
    @Column(name = "USERNAME")
    String username;
    @Column(name = "FULLNAME")
    String fullName;
    @Column(name = "PASSWORD")
    String password;
    @Column(name = "FIRSTNAME")
    String firstname;
    @Column(name = "LASTNAME")
    String lastname;
    @Column(name = "DOB")
    Date dob;
    @Column(name = "PHONE")
    String phone;
    @Column(name = "GENDER")
    Long gender;
    @Column(name = "EMAIL")
    String email;
    @Column(name = "ID_CARD_NUMBER")
    String idCardNumber;
    @Column(name = "ISSUED_DATE")
    Date issuedDate;
    @Column(name = "ISSUED_PLACE")
    String issuedPlace;
    @Column(name = "BIRTH_PLACE")
    String birthPlace;
    @Column(name = "PERMANENT_ADDRESS")
    String permanentAddress;
    @Column(name = "CURRENT_ADDRESS")
    String currentAddress;
    @Column(name = "WORKING_STATUS")
    Long workingStatus;
    @Column(name = "RELIGION")
    Long religion;
    @Column(name = "ETHNIC")
    String ethnic;
    @Column(name = "NATIONALITY")
    String nationality;
    @Column(name = "BANK")
    String bank;
    @Column(name = "BANK_TYPE")
    String bankType;
    @Column(name = "BANK_NUMBER")
    String bankNumber;
    @Column(name = "TAX_CODE")
    String taxCode;
    @Column(name = "MARITAL_STATUS")
    Long maritalStatus;
    @Column(name = "DEPARTMENT_ID")
    Long departmentId;
    @Column(name = "POSITION_ID")
    Long positionId;
    @Column(name = "EDUCATION_ID")
    Long educationId;
    @Column(name = "CREATED")
    Date created;
    @Column(name = "CREATEDBY")
    Long createdBy;
    @Column(name = "UPDATED")
    Date updated;
    @Column(name = "UPDATEDBY")
    Long updatedBy;
    @Column(name = "ISACTIVE")
    String isActive;

}
