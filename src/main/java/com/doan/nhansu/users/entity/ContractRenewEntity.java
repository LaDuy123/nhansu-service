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
@Table(name = "contract_renew")
public class ContractRenewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contract_renew_seq")
    @SequenceGenerator(name = "contract_renew_seq", sequenceName = "contract_renew_sequence", allocationSize = 1)
    @Column(name = "CONTRACT_RENEW_ID")
    Long id;
    @Column(name = "NAME")
    String name;
    @Column(name = "VALUE")
    String value;
    @Column(name = "DESCRIPTION")
    String description;
    @Column(name = "START_DATE")
    Date startDate;
    @Column(name = "END_DATE")
    Date endDate;
    @Column(name = "CONTRACT_TYPE_ID")
    Long contractTypeId;
    @Column(name = "CREATED")
    Date created;
    @Column(name = "CREATEDBY")
    Long createdBy;
    @Column(name = "UPDATED")
    Date updated;
    @Column(name = "UPDATEDBY")
    Long updatedBy;
}
