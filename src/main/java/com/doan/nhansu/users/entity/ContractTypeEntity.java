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
@Table(name = "contract_type")
public class ContractTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contract_type_seq")
    @SequenceGenerator(name = "contract_type_seq", sequenceName = "contract_type_sequence", allocationSize = 1)
    @Column(name = "CONTRACT_TYPE_ID")
    Long id;
    @Column(name = "NAME")
    String name;
    @Column(name = "VALUE")
    String value;
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
